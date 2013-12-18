/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ipccenter.newsagg.impl.vkapi;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import org.ipccenter.newsagg.bean.jsf.AuthorizationBean;
import org.ipccenter.newsagg.entity.News;
import org.ipccenter.newsagg.gson.Feed;
import org.ipccenter.newsagg.gson.FeedItem;
import org.ipccenter.newsagg.gson.Response;
import org.ipccenter.newsagg.utils.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author darya
 */
public class VKPuller{ 
    
    @Inject
    AuthorizationBean authorizationBean;

    private static final Logger LOG = LoggerFactory.getLogger(VKInteractionPuller.class);
    private final VKAuth auth;
    private Date lastUpdatedTime;
    
    public VKPuller(){
        this.auth = authorizationBean.getVkAuth();
    }
    
    public List<News> getNews() throws Exception {
        LOG.info("VKInteractionPuller.getNews() is called. Last updated time: {}", lastUpdatedTime);
        List<News> result = new ArrayList<>();
        VKMethod getFeed = new VKMethod("newsfeed.get", auth);
        String filters = "post,note";
        String startTime = String.valueOf(getLastUpdatedTime().getTime() / 1000);
        getFeed.addParam("filters", filters)
                .addParam("start_time", startTime)
                .addParam("return_banned", "0")
                .addParam("count", "10")
                .addParam("offset", "0");
        LOG.debug("Search method params: {}", getFeed.getParams());
        String rawFeed = getFeed.execute();
        LOG.trace("Response in plain text: {}", rawFeed);
        Gson gson = new Gson();
        Response feedResponse = gson.fromJson(rawFeed, Response.class);
        LOG.debug("Response: {}", feedResponse);
        if (feedResponse.getError() != null) {
            LOG.warn("Cannot get response. Error message is: {}", feedResponse.getError().getErrorMessage());
            throw new IllegalArgumentException(feedResponse.getError().getErrorMessage());
        }
        Feed feedList = feedResponse.getResponse();
        LOG.trace("Feedlist: {}", feedList);
        FeedItem[] feedItem = gson.fromJson(gson.toJson(feedList), Feed.class).getItems();
        LOG.debug("Feeditems: {}", new Object[]{feedItem});
        for (FeedItem item : feedItem) {
            result.add(parsePost(item));
        }
        return result;
    }

    public News parsePost(FeedItem feedItem) {
        News post = new News();
        post.setSource("vk.com");
        post.setContent(feedItem.getText());
        post.setDate(feedItem.getDate());
        if (feedItem.isCopy()) {
            post.setUrl("http://vk.com/" + feedItem.getCopyPostAddress());
        } else {
            post.setUrl("http://vk.com/" + feedItem.getPostAddress());
        }
        post.setStatus(Status.NEW.getId());
        LOG.trace("Parsed post: {}", post);
        lastUpdatedTime.setTime(System.currentTimeMillis());
        return post;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    
}
