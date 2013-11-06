package org.ipccenter.newsagg.gson;

/**
 * Created with IntelliJ IDEA.
 * User: darya
 * Date: 05.11.13
 * Time: 14:42
 * To change this template use File | Settings | File Templates.
 */
public class Response {

    private FeedItem[] response;
    private Error error;

    public FeedItem[] getResponse() {
        return response;
    }

    public Error getError() {
        return error;
    }
}
