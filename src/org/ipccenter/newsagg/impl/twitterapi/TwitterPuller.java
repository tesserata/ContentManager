/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ipccenter.newsagg.impl.twitterapi;


import org.ipccenter.newsagg.Puller;
import org.ipccenter.newsagg.entity.News;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author darya
 */
public class TwitterPuller implements Puller {


    public static final Logger LOG = LoggerFactory.getLogger(TwitterPuller.class);
    private Twitter twitter;
    private List<News> postsList = new ArrayList<News>();

    public TwitterPuller(Twitter twitter) {
        this.twitter = twitter;
    }

    public void findPosts() throws TwitterException {
        StringBuilder searchURL = new StringBuilder();
        searchURL.append("https://api.twitter.com/1.1/search/tweets.json&q=");
        QueryResult searchResult = null;
        List<Status> posts = new ArrayList<Status>();
        Query q = new Query();
        LOG.info("Query: {}", q.getQuery());
        List<String> requests = new ArrayList<String>();
        requests.add("ФРТК");
        requests.add("МФТИ");
        requests.add("Физтех");
        for (String request : requests) {
            q.query(request.toString());
            LOG.info("New query: {}", q.getQuery());
            searchResult = twitter.search(q);
            posts.addAll(searchResult.getTweets());
        }
        LOG.info("Posts amount: {}", posts.size());
        for (Status status : posts) {
            parsePost(status);
        }
    }

    public void checkFeed() {
    }

    ;

    public void parsePost(Status status) {
        News post = new News();
        post.setSource("Twitter");
        post.setStatus(0);
        post.setContent(status.getText());
        StringBuilder url = new StringBuilder();
        url.append("http://twitter.com/")
                .append(status.getUser().getScreenName())
                .append("/statuses/")
                .append(String.valueOf(status.getId()));
        post.setUrl(url.toString());
        post.setDate(status.getCreatedAt());
        //post.setAuthor(status.getUser().getName());
        postsList.add(post);
    }

    public List<News> getPostsList() {
        return postsList;
    }


    public void testPost() throws TwitterException {
        Status status;
        status = twitter.updateStatus("Если этот твит запощен, то мое приложение работает");
    }
}
