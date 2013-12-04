/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ipccenter.newsagg.impl.twitterapi;

import org.ipccenter.newsagg.entity.News;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 *
 * @author darya
 */
public class TwitterPusher {
    
    private static final Logger LOG = LoggerFactory.getLogger(TwitterPusher.class);
    
    private Twitter twitter;
    
    public TwitterPusher(Twitter twitter){
        this.twitter = twitter;
    }
    
    public void retweet(News news) throws TwitterException{
        if (!news.getSource().equals("Twitter")){
            LOG.info("Wrong source: {}", news.getSource());
            return;
        }
        String id = news.getUrl().substring(news.getUrl().lastIndexOf('/')+1).toString();
        Status status;
        status = twitter.retweetStatus(Long.getLong(id));
    }
    
    public void toFav(News news) throws TwitterException{
        if (!news.getSource().equals("Twitter")) return;
        String id = news.getUrl().substring(news.getUrl().lastIndexOf('/')+1).toString();
        Status status;
        LOG.info("Tweet ID: {}", id);
        status = twitter.createFavorite(Long.getLong(id));
    }
    
    public void post(String text) throws TwitterException{
        Status status = twitter.updateStatus(text);
    }
    
}
