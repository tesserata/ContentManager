package org.ipccenter.newsagg.gson;


/**
 * Created with IntelliJ IDEA.
 * User: darya
 * Date: 05.11.13
 * Time: 14:42
 * To change this template use File | Settings | File Templates.
 */
public class Response {

    private Feed response;
    private SearchFeed searchResponse;
    private Error error;

    public Feed getResponse() {
        return response;
    }
    
    public SearchFeed getSearchResponse(){
        return searchResponse;
    }

    public Error getError() {
        return error;
    }
}
