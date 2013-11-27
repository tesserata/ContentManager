package org.ipccenter.newsagg.impl.vkapi;

import com.google.gson.Gson;
import org.ipccenter.newsagg.Puller;
import org.ipccenter.newsagg.entity.News;
import org.ipccenter.newsagg.gson.Feed;
import org.ipccenter.newsagg.gson.FeedItem;
import org.ipccenter.newsagg.gson.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA. User: darya Date: 28.10.13 Time: 22:30 To change
 * this template use File | Settings | File Templates.
 */
public class VKPuller implements Puller {


    private VKAuth auth;

    private static final Logger LOG = LoggerFactory.getLogger(VKPuller.class);

    private List<News> postsList = new ArrayList<News>();
    private int offset = 0;

    public VKPuller(VKAuth auth) {
        this.auth = auth;
    }

    public List<News> getPostsList() {
        return postsList;
    }

    public int getOffset() {
        return offset;
    }

    public void getFriends() throws IOException, NoSuchAlgorithmException {
        VKMethod getFriends = new VKMethod("newsfeed.get", auth);
//        getFriends.addParam("uid", auth.getUserID());
        String friends = getFriends.execute();
        LOG.info("Friends: {}", friends);
    }

    public void checkFeed() throws IOException {
        VKMethod getFeed = new VKMethod("newsfeed.get", auth);
        String filters = "post,note";
        //String startTime = String.valueOf(getLastUpdateTime() - 12 * 60 * 60 * 100);
        getFeed.addParam("filters", filters)
                .addParam("return_banned", "0")
                .addParam("count", "10")
                .addParam("offset", String.valueOf(offset));
        // .addParam("start_time", startTime);
        LOG.info("Search method params: {}", getFeed.getParams());
        String rawFeed = getFeed.execute();
        LOG.info("Response in plain text: {}", rawFeed);
        Gson gson = new Gson();
        Response feedResponse = gson.fromJson(rawFeed, Response.class);
        LOG.info("Responce: {}", feedResponse);
        if (feedResponse.getError() != null) {
            LOG.warn("Cannot get response. Error message is: {}", feedResponse.getError().getErrorMessage());
            throw new IllegalArgumentException(feedResponse.getError().getErrorMessage());
        }
        Feed feedList = feedResponse.getResponse();
        offset = feedList.getNewOffset();
        LOG.info("Feedlist: {}", feedList);
        FeedItem[] feedItem = gson.fromJson(gson.toJson(feedList), Feed.class).getItems();
        LOG.info("Feeditems: {}", feedItem);
        for (FeedItem item : feedItem) {
            parsePost(item);
        }

    }

    public void findPosts() throws IOException {
        VKMethod searchFeed = new VKMethod("newsfeed.search", auth);
        List<String> requests = Arrays.asList(KEYWORDS.values().toString());
        String count = "50";
        for (String request : requests) {
            searchFeed.addParam("q", request).addParam("count", count).addParam("offset", String.valueOf(offset));
            String feed = searchFeed.execute();
            Gson gson = new Gson();
            Response searchFeedResponse = gson.fromJson(feed, Response.class);
            if (searchFeedResponse.getError() != null) {
                throw new IllegalArgumentException(searchFeedResponse.getError().getErrorMessage());
            }
            Feed feedList = searchFeedResponse.getResponse();
            FeedItem[] feedItem = gson.fromJson(gson.toJson(feedList), Feed.class).getItems();
        }

    }

    public void parsePost(FeedItem feedItem) {
        News post = new News();
        post.setSource("vkontakte");
        post.setContent(feedItem.getText());
        post.setDate(feedItem.getDate());
        if (feedItem.isCopy()) {
            post.setUrl("http://vk.com/" + feedItem.getCopyPostAddress());
        } else {
            post.setUrl("http://vk.com/" + feedItem.getPostAddress());
        }
        post.setStatus(0);
        postsList.add(post);
    }

}
