/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ipccenter.newsagg.bean.jsf;

import org.apache.commons.lang3.StringEscapeUtils;
import org.ipccenter.newsagg.bean.ejb.NewsBean;
import org.ipccenter.newsagg.entity.News;
import org.ipccenter.newsagg.impl.twitterapi.TwitterPuller;
import org.ipccenter.newsagg.impl.vkapi.VKAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.IOException;
import java.util.*;
import javax.inject.Inject;
import org.ipccenter.newsagg.bean.ejb.ConfiguratorBean;
import org.ipccenter.newsagg.impl.twitterapi.TwitterPusher;
import org.ipccenter.newsagg.impl.vkapi.VKInteractionPuller;
import org.ipccenter.newsagg.impl.vkapi.VKPuller;
import org.ipccenter.newsagg.impl.vkapi.VKPusher;
import org.ipccenter.newsagg.interfaces.Puller;

/**
 * @author darya
 */
@ManagedBean
@SessionScoped
public class NewsManagedBean {

    private static final Logger LOG = LoggerFactory.getLogger(NewsManagedBean.class);
    @EJB
    NewsBean ejbNews;
    
    @Inject
    ConfiguratorBean timerBean;

    @Inject
    AuthorizationBean authorizationBean;
    
    public News displayedNews;
    private String postedContent;
    private VKPuller vkPuller;
    private TwitterPuller twitterPuller;
    
    
    public NewsManagedBean(){
        LOG.info("Twitter puller initializing from NMB...");
        //this.twitterPuller = new TwitterPuller();
        this.vkPuller = new VKPuller();
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
        return "showNews";
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
    
    /*public String getTwiToken() throws TwitterException {
        try {
//            StringBuffer callbackURL = request.getRequestURL();
//            int index = callbackURL.lastIndexOf("/");
//            callbackURL.replace(index, callbackURL.length(), "").append("/catch_auth.jsp");
            String callbackURL = "http://localhost:8080/NewsAggregator/faces/catch_auth.jsp";

            RequestToken requestToken = twitter.getOAuthRequestToken(callbackURL.toString());
//            request.getSession().setAttribute("requestToken", requestToken);
//            response.sendRedirect(requestToken.getAuthenticationURL());
            LOG.info("Request token URL: {}", requestToken.getAuthenticationURL());
            return requestToken.getAuthenticationURL();
        } catch (TwitterException e) {
            throw e;
        }
    }*/

 
//    public void updateFeed() throws Exception{
//        for (Puller p: timerBean.getPullers())
//            for (News news: p.getNews())
//                ejbNews.persist(news);
//    }
    
    public void updateFeed() throws Exception{
        for (News news: vkPuller.getNews())
            ejbNews.persist(news);
        for (News news :twitterPuller.getPostsList())
            ejbNews.persist(news);
        
    }
            
//    public void updateVkFeed() throws IOException {
//        LOG.info("Access token before requesting news: {}", vkauth.getAccessToken());
//        vk.checkFeed();
//        //vk.findPosts();
//        for (News news : vk.getPostsList()) {
//            ejbNews.persist(news);
//        }
//
//    }
//
//    public void updateTwitter() throws TwitterException {
//        TwitterPuller pull = new TwitterPuller(twitter);
//        pull.findPosts();
//        for (News news : pull.getPostsList()) {
//            ejbNews.persist(news);
//        }
//    }
    
    
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
}
