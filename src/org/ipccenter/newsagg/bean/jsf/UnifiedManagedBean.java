package org.ipccenter.newsagg.bean.jsf;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.commons.lang3.StringEscapeUtils;
import org.ipccenter.newsagg.bean.ejb.NewsBean;
import org.ipccenter.newsagg.entity.News;
import org.ipccenter.newsagg.impl.twitterapi.TwitterPuller;
import org.ipccenter.newsagg.impl.twitterapi.TwitterPusher;
import org.ipccenter.newsagg.impl.vkapi.VKAuth;
import org.ipccenter.newsagg.impl.vkapi.VKPuller;
import org.ipccenter.newsagg.impl.vkapi.VKPusher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;

/**
 *
 * @author darya
 */

@ManagedBean
@SessionScoped

public class UnifiedManagedBean {
    
    private static final Logger LOG = LoggerFactory.getLogger(UnifiedManagedBean.class);
    
    @EJB
    NewsBean ejbNews;
    
    public News displayedNews;
    private String postedContent;
    private VKPuller vkPuller;
    private TwitterPuller twitterPuller;
    
    private String vkAccessToken;
    private String vkID;
    private VKAuth vkAuth;
    private String vkAuthUrl;
    
    private String twiPin;
    private Twitter twiAuth;
    private String twiAuthUrl;
    
    private TwitterPusher twiPush;
    private VKPusher vkPush;
    
    private String generatedContent;
    
    private String twitterGeneratedContent;
    
    private String vkGeneratedContent;
    private String vkDestination;
    private String vkHashTag;
    
    public UnifiedManagedBean() throws TwitterException{
        this.vkAuth = new VKAuth();
        this.twiAuth = TwitterFactory.getSingleton();
        this.twiAuth.setOAuthConsumer("X6CTYpAt53aq71iBtTEMQ", "ApTAXimRwQ0361gc8aCDhqZAKcnXFTgwigrN445opI");
        buildTwiAuthUrl();
        buildVKAuthUrl();
    }
    
    public void testAuth() throws TwitterException{
        this.twiAuth.getOAuthAccessToken(twiPin);
        this.twiPush = new TwitterPusher(twiAuth);
        twiPush.post("Test");
}
    
   public void buildTwiAuthUrl() throws TwitterException {
        RequestToken rt = twiAuth.getOAuthRequestToken();
        twiAuthUrl = rt.getAuthorizationURL();
    }

    private void buildVKAuthUrl() {
        StringBuilder sb = new StringBuilder();
        sb.append("http://oauth.vk.com/oauth/authorize?");
        sb.append("client_id").append("=").append("4017304").append("&");
        sb.append("redirect_uri").append("=").append(StringEscapeUtils.escapeHtml4("http://oauth.vk.com/blank.html")).append("&");
        sb.append("scope").append("=").append("wall,friends,messages").append("&");
        sb.append("display").append("=").append("page").append("&");
        sb.append("response_type").append("=").append("token");
        vkAuthUrl = sb.toString();
    }
    
    public void toAuth() throws TwitterException{
        this.vkAuth.setAccessToken(vkAccessToken);
        this.vkAuth.setUserID(vkID);
        testAuth();
        this.vkPush = new VKPusher(vkAuth);
        
        this.twitterPuller = new TwitterPuller(twiAuth);
        this.vkPuller = new VKPuller(vkAuth);
    }
    
    public List<News> getNews() {
        return ejbNews.getAllNews();
    }

    public int getNewsCount() {
        return ejbNews.getAllNews().size();
    }
    
    public String getPostedContent() {
        return postedContent;
    }

    public void setPostedContent(String postedContent) {
        LOG.info("setPostedContent(\"{}\")", postedContent);
        this.postedContent = postedContent;
    }

    public String checkStatus(News news) {
        LOG.info("News: {}", news);
        if(news == null) {
            return null;
        }
        switch (news.getStatus()) {
            case (0):
                return "New";
            case (1):
                return "Posted";
            case (-1):
                return "Ignored";
        }

        return null;
    }

    public void changeStatus(News news, String status) {
        if (status.equalsIgnoreCase("New")) news.setStatus(0);
        if (status.equalsIgnoreCase("Posted")) news.setStatus(1);
        if (status.equalsIgnoreCase("Ignored")) news.setStatus(-1);
        news = ejbNews.merge(news);
    }
    
    public String toIgnore(){
        changeStatus(displayedNews, "Ignored");
        return "allNews";
    }
    
    public void clearDB() {
        ejbNews.clearDataBase();
    }

    public String showContent(News item) {
        if (item.getContent() == null) return "";
        else{
            if (item.getContent().length() < 50) {
                return item.getContent();
            } else {
                return item.getContent().substring(0, 50).concat("...");
            }
        }
    }

    public void chooseDisplayedNews(News displayedNews) {
        LOG.info("Selected news: {}",displayedNews);
        this.displayedNews = displayedNews;
        LOG.info("Displayed news: {}", this.displayedNews);
    }

    public News getDisplayedNews() {
        return displayedNews;
    }

    public void deleteNews(News news) {
        ejbNews.deleteNews(news);
    }
    
    public void updateFeed() throws Exception{
        LOG.info("Updating feed...");
        for (News news: vkPuller.getNews())
            ejbNews.persist(news);
        for (News news :twitterPuller.getPostsList())
            ejbNews.persist(news);
    }
    
    public String disableTwitter(){
        if (displayedNews == null) return "true";
        else{
            if (displayedNews.getSource().equals("Twitter")) return "false"; else return "true";
        }
    }
    
    public String disableVK(){
        if (displayedNews == null) return "true";
        else{
            if (displayedNews.getSource().equals("vk.com")) return "false"; else return "true";
        }
    }   
    
    public void generateNews(String content) {
        LOG.info("postedContent during generateNews() is {}", generatedContent);
        Date date = new Date();
        News news = new News(content, "", "Editor", date);
        ejbNews.persist(news);
    }
    
    public void generatePushedNews(News news, String destination){}
    
    public void generateTwitterPost() throws TwitterException{
        LOG.info("Text: {}", twitterGeneratedContent);
        twiPush.post(twitterGeneratedContent);
        generateNews(twitterGeneratedContent);
        twitterGeneratedContent = null;
        
    }
    
    public void generateVkPost() throws IOException{
        if (vkHashTag != null) vkGeneratedContent.concat("\n\n" + vkHashTag);
        vkPush.postNew(vkGeneratedContent, vkDestination);
        generateNews(vkGeneratedContent);
        vkGeneratedContent = null;
    }
    
    public void retweet() throws TwitterException{
        LOG.info("Retweet method called");
        twiPush.retweet(displayedNews);
     }
    
    public void toFav() throws TwitterException{
        LOG.info("toFav method called");
        twiPush.toFav(displayedNews);
    }

    public void twitterPostCurrent() throws TwitterException{
        twiPush.postNews(displayedNews);
    }
    
    public void vkPostCurrent() throws IOException{
        vkPush.postCurrent(displayedNews, vkDestination);
        changeStatus(displayedNews, "Posted");
    }
    
    public void vkRepost() throws IOException{
        vkPush.repost(displayedNews, vkDestination);
        vkDestination = null;
        changeStatus(displayedNews, "Posted");
    }
    
    
    
    
    public String getVkAccessToken() {
        return vkAccessToken;
    }

    public void setVkAccessToken(String vkAccessToken) {
        this.vkAccessToken = vkAccessToken;
    }

    public String getVkID() {
        return vkID;
    }

    public void setVkID(String vkID) {
        this.vkID = vkID;
    }

    public String getVkAuthUrl() {
        return vkAuthUrl;
    }

    public void setVkAuthUrl(String vkAuthUrl) {
        this.vkAuthUrl = vkAuthUrl;
    }

    public String getTwiPin() {
        return twiPin;
    }

    public void setTwiPin(String twiPin) {
        this.twiPin = twiPin;
    }

    public String getTwiAuthUrl() {
        return twiAuthUrl;
    }

    public void setTwiAuthUrl(String twiAuthUrl) {
        this.twiAuthUrl = twiAuthUrl;
    }

    public String getGeneratedContent() {
        return generatedContent;
    }

    public void setGeneratedContent(String generatedContent) {
        this.generatedContent = generatedContent;
    }

    public String getTwitterGeneratedContent() {
        return twitterGeneratedContent;
    }

    public void setTwitterGeneratedContent(String twitterGeneratedContent) {
        this.twitterGeneratedContent = twitterGeneratedContent;
    }

    public String getVkGeneratedContent() {
        return vkGeneratedContent;
    }

    public void setVkGeneratedContent(String vkGeneratedContent) {
        this.vkGeneratedContent = vkGeneratedContent;
    }

    public String getVkDestination() {
        return vkDestination;
    }

    public void setVkDestination(String vkDestination) {
        this.vkDestination = vkDestination;
    }

    public String getVkHashTag() {
        return vkHashTag;
    }

    public void setVkHashTag(String vkHashTag) {
        this.vkHashTag = vkHashTag;
    }

}
