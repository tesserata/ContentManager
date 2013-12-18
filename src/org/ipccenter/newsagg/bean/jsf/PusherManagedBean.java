/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ipccenter.newsagg.bean.jsf;

import java.io.IOException;
import javax.inject.Inject;
import org.ipccenter.newsagg.impl.twitterapi.TwitterPusher;
import org.ipccenter.newsagg.impl.vkapi.VKPusher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.TwitterException;

/**
 *
 * @author darya
 */


public class PusherManagedBean {
    
    public static final Logger LOG = LoggerFactory.getLogger(PusherManagedBean.class);
    
    @Inject
    AuthorizationBean authorizationBean;
    
    @Inject
    NewsManagedBean newsManagedBean;
    
    private TwitterPusher twiPush;
    private VKPusher vkPush;
    private String vkDestination;
    
    public PusherManagedBean(){
        this.twiPush = new TwitterPusher((authorizationBean.getTwiAuth()));
        this.vkPush = new VKPusher(authorizationBean.getVkAuth());
    }
    
    public void retweet() throws TwitterException{
        LOG.info("Retweet method called");
        twiPush.retweet(newsManagedBean.displayedNews);
     }
    
    public void toFav() throws TwitterException{
        LOG.info("toFav method called");
        twiPush.toFav(newsManagedBean.displayedNews);
    }

    public void twitterPostCurrent() throws TwitterException{
        twiPush.postNews(newsManagedBean.displayedNews);
    }
    
    public void vkPostCurrent() throws IOException{
        vkPush.postCurrent(newsManagedBean.displayedNews, vkDestination);
        newsManagedBean.changeStatus(newsManagedBean.displayedNews, "Posted");
    }
    
    public void vkRepost() throws IOException{
        vkPush.repost(newsManagedBean.displayedNews, vkDestination);
        vkDestination = null;
        newsManagedBean.changeStatus(newsManagedBean.displayedNews, "Posted");
    }

    public void setVkDestination(String vkDestination) {
        this.vkDestination = vkDestination;
    }
    
}
