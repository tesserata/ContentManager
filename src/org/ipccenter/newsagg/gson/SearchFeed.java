package org.ipccenter.newsagg.gson;

import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author darya
 */
public class SearchFeed {
    private int items;
    private SearchFeedItem[] response;
    private int new_offset;

    public SearchFeedItem[] getItems() {
        return response;
    }

    public int getNewOffset() {
        return new_offset;
    }
    
    @Override
    public String toString() {
        return "SearchFeed{" + "response=" + response + ", new_offset=" + new_offset + '}';
    }


}
