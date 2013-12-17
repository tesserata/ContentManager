package org.ipccenter.newsagg.impl.vkapi;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ipccenter.newsagg.entity.InterfaceConfig;
import org.ipccenter.newsagg.entity.News;
import org.ipccenter.newsagg.entity.Parameter;
import org.ipccenter.newsagg.gson.Feed;
import org.ipccenter.newsagg.gson.FeedItem;
import org.ipccenter.newsagg.gson.Response;
import org.ipccenter.newsagg.interfaces.Puller;
import org.ipccenter.newsagg.utils.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author spitty
 */
public final class VKInteractionPuller implements Puller {

    private static final Logger LOG = LoggerFactory.getLogger(VKInteractionPuller.class);
    private InterfaceConfig config;
    private final VKAuth auth;
    private Date lastUpdatedTime;

    public VKInteractionPuller() {
        auth = new VKAuth();
    }

    public void setAccessToken(String accessToken) {

        auth.setAccessToken(accessToken);
        saveParameter("access_token", accessToken);
    }

    public void setUserID(String userID) {
        auth.setUserID(userID);
        saveParameter("user_id", userID);
    }

    private void saveParameter(String key, String value) {
        if (config == null) {
            throw new IllegalStateException("field [config] is empty. Probably initialize() method wasn't be called");
        }
        Map<String, Parameter> parameters = fillParametersMap(config.getParameters());
        Parameter param;
        if (parameters.containsKey(key)) {
            param = parameters.get(key);
            LOG.debug("Update existing parameter [{}]", param);
            param.setValue(value);
        } else {
            param = new Parameter();
            param.setKey(key);
            param.setValue(value);
            param.setConfig(config);
            config.getParameters().add(param);
            LOG.debug("Add new parameter [{}]", param);
        }
    }

    @Override
    public void initialize() {
        config = new InterfaceConfig();
        config.setClassName(VKInteractionPuller.class.getName());
        lastUpdatedTime = new Date(System.currentTimeMillis() - 30 * 60 * 1000);
    }

    @Override
    public InterfaceConfig saveConfiguration() {
        return config;
    }

    @Override
    public void restoreConfiguration(InterfaceConfig config) {
        this.config = config;
        Map<String, Parameter> parameters = fillParametersMap(config.getParameters());
        setAccessToken(parameters.get("access_token").getValue());
        setUserID(parameters.get("user_id").getValue());
        lastUpdatedTime = new Date(System.currentTimeMillis() - 30 * 60 * 1000);
    }

    private Map<String, Parameter> fillParametersMap(Collection<Parameter> params) {
        Map<String, Parameter> result = new HashMap<>();
        for (Parameter p : params) {
            LOG.trace("Set parameter {}", p);
            if (result.containsKey(p.getKey())) {
                LOG.warn("Parameters with key [{}] already defined", p.getKey());
            }
            result.put(p.getKey(), p);
        }
        return result;
    }

    @Override
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

    @Override
    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

}
