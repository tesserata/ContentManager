package org.ipccenter.newsagg;

import org.ipccenter.newsagg.gson.FeedItem;

import java.io.IOException;


/**
 * Created with IntelliJ IDEA.
 * User: darya
 * Date: 24.10.13
 * Time: 21:41
 * To change this template use File | Settings | File Templates.
 */
public interface Puller {
    enum KEYWORDS {ФРТК, МФТИ, Физтех, РТ, паяльник, радио}

    void checkFeed() throws IOException;

    void findPosts() throws IOException;

    void parsePost(FeedItem feedItem);

}
