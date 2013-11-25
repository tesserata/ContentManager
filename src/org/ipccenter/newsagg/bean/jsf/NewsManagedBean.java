/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ipccenter.newsagg.bean.jsf;


import org.apache.commons.lang3.StringEscapeUtils;
import org.ipccenter.newsagg.bean.ejb.NewsBean;
import org.ipccenter.newsagg.entity.News;
import org.ipccenter.newsagg.impl.vkapi.VKAuth;
import org.ipccenter.newsagg.impl.vkapi.VKPuller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @author darya
 */
@ManagedBean
@SessionScoped
public class NewsManagedBean {

    private String generatedContent;

    private static final Logger LOG = LoggerFactory.getLogger(NewsManagedBean.class);
    @EJB
    NewsBean ejbNews;

    private static Map<Integer, String> statuses;
    private VKAuth vkauth = new VKAuth();

    private String vkAuthURL = buildAuthUrl();
    private String vkAccessToken;
    private String userID;


    static {
        statuses = new LinkedHashMap<Integer, String>();
        statuses.put(0, "New");
        statuses.put(1, "Posted");
        statuses.put(-1, "Ignored");
    }

    /**
     * Creates a new instance of NewsManagedBean
     */
    public NewsManagedBean() {
    }

    public String getVkAuthURL() {
        LOG.info("vkAuthURL: {}", vkAuthURL);
        return vkAuthURL;
    }

    public Collection<String> getStatuses() {
        return statuses.values();
    }

    public void setGeneratedContent(String generatedContent) {
        this.generatedContent = generatedContent;
    }

    public String getGeneratedContent() {
        return generatedContent;
    }

    public List<News> getNews() {
        return ejbNews.getAllNews();
    }

    public List<News> getPostedNews() {
        return ejbNews.getPostedNews();
    }

    public List<News> getNewNews() {
        return ejbNews.getNewNews();
    }

    public List<News> getIgnoredNews() {
        return ejbNews.getIgnoredNews();
    }

    public void generateNews() {
        LOG.info("generateNews() has been invoked");
        Date date = new Date();
        News news = new News(generatedContent, "some url", "editor", date);
        ejbNews.persist(news);
    }

    public int getNewsCount() {
        return ejbNews.getAllNews().size();
    }

    public int getPostedNewsCount() {
        return ejbNews.getPostedNews().size();
    }

    public int getNewNewsCount() {
        return ejbNews.getNewNews().size();
    }

    public int getIgnoredNewsCount() {
        return ejbNews.getIgnoredNews().size();
    }

    public String checkStatus(News news) {
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

    public void clearDB() {
        ejbNews.clearDataBase();
    }

    public String showContent(News item) {
        if (item.getContent().length() < 50) {
            return item.getContent();
        } else {
            return item.getContent().substring(0, 50).concat("...");
        }
    }

    public void deleteNews(News news) {
        ejbNews.deleteNews(news);
    }

    private String buildAuthUrl() {
        StringBuilder sb = new StringBuilder();
        sb.append("http://oauth.vk.com/oauth/authorize?");
        sb.append("client_id").append("=").append("4017304").append("&");
        sb.append("redirect_uri").append("=").append(StringEscapeUtils.escapeHtml4("http://oauth.vk.com/blank.html")).append("&");
        sb.append("scope").append("=").append("wall,friends").append("&");
        sb.append("display").append("=").append("page").append("&");
        sb.append("response_type").append("=").append("token");
        return sb.toString();
    }


    public String toAuthVK() {
        LOG.info("Set access token to {}", this.vkAccessToken);
        this.vkauth.setAccessToken(this.vkAccessToken);
        this.vkauth.setUserID(this.userID);
        return "showNews";
    }

    public void setVkAccessToken(String accessToken) {
        LOG.info("Call setVkAccessToken({})", accessToken);
        this.vkAccessToken = accessToken;
    }

    public String getVkAccessToken() {
        return this.vkAccessToken;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        if (userID == null) {
            userID = "11508656";
        }
        return userID;
    }

    public void getFriends() throws IOException, NoSuchAlgorithmException {
        VKPuller vk = new VKPuller(vkauth);
        vk.getFriends();
    }

    public void updateVkFeed() throws IOException {
        LOG.info("Access token before requesting news: {}", vkauth.getAccessToken());
        VKPuller vk = new VKPuller(vkauth);
        vk.checkFeed();
        vk.findPosts();
        for (News news : vk.getPostsList()) {
            ejbNews.persist(news);
        }

    }
}
