package org.ipccenter.newsagg.api;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.*;

import org.ipccenter.newsagg.News;
import org.ipccenter.newsagg.gson.FeedItem;
import org.ipccenter.newsagg.gson.Response;


/**
 * Created with IntelliJ IDEA.
 * User: darya
 * Date: 28.10.13
 * Time: 22:30
 * To change this template use File | Settings | File Templates.
 */
public class VKPuller implements Puller {

    List<News> postsList = new ArrayList<News>();

    public String findPost(Date date) throws IOException {
        VKAuth auth = new VKAuth("11508656", "", "");
        VKMethod getFeeds = new VKMethod("newsfeed.get", auth);
        String filters = "post,note";
        String startTime = String.valueOf(date.getTime());
        String feeds = getFeeds.addParam("filters", filters).addParam("start_time", startTime).execute();
        Gson gson = new Gson();
        Response feedsResponse =  gson.fromJson(feeds, Response.class);
        if (feedsResponse.getError() != null)
            throw new IllegalArgumentException(feedsResponse.getError().getErrorMessage());
        FeedItem[] feedList = feedsResponse.getResponse();
        for(FeedItem feed : feedList)
            parsePost(feed);
        return null;
    }          // finds posts with keywords, returns URL to parsing

    public void parsePost(FeedItem feedItem){
        News post = new News();
        post.source = News.Sources.VKONTAKTE;
        post.content = feedItem.getText();
        post.date = feedItem.getDate();
        if (feedItem.isCopy())
            post.url = "vk.com/" + feedItem.getCopyPostAddress();
        else
            post.url = "vk.com/" + feedItem.getPostAddress();
        post.status = 0;
        postsList.add(post);
    } //make new DB element, set up all the fields by parsing html(?)


}
