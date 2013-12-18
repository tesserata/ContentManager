/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ipccenter.newsagg.bean.jsf;

import java.io.IOException;
import java.util.Date;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import org.ipccenter.newsagg.bean.ejb.NewsBean;
import org.ipccenter.newsagg.entity.News;
import org.ipccenter.newsagg.impl.twitterapi.TwitterPusher;
import org.ipccenter.newsagg.impl.vkapi.VKAuth;
import org.ipccenter.newsagg.impl.vkapi.VKPusher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 *
 * @author darya
 */
@ManagedBean
@RequestScoped

public class GenerationBean {

    private static final Logger LOG = LoggerFactory.getLogger(GenerationBean.class);
    @EJB
    NewsBean ejbNews;
    @Inject
    AuthorizationBean authorizationBean;

    private String generatedContent;
    
    private String twitterGeneratedContent;
    
    private String vkGeneratedContent;
    private String vkDestination;
    private String vkHashTag;

    private TwitterPusher twiPush;
    private VKPusher vkPush;
    
    public GenerationBean(){
        this.twiPush = new TwitterPusher(authorizationBean.getTwiAuth());
        this.vkPush = new VKPusher(authorizationBean.getVkAuth());
    }

    public String getTwitterGeneratedContent() {
        return twitterGeneratedContent;
    }

    public void setTwitterGeneratedContent(String twitterGeneratedContent) {
        this.twitterGeneratedContent = twitterGeneratedContent;
    }

    public String getVkGeneratedContend() {
        return vkGeneratedContent;
    }

    public void setVkGeneratedContend(String vkGeneratedContent) {
        this.vkGeneratedContent = vkGeneratedContent;
    }

    public String getGeneratedContent() {
        return generatedContent;
    }

    public void setGeneratedContent(String value) {
        LOG.info("setGeneratedContent(\"{}\")", value);//, new Exception());
        this.generatedContent = value;
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
}
