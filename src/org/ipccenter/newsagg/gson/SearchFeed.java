package org.ipccenter.newsagg.gson;

import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author darya
 */
public class SearchFeed {
    private Pair<Integer, FeedItem[]> response;
    private int new_offset;

    public FeedItem[] getResponse() {
        return response.getRight();
    }

    public int getNewOffset() {
        return new_offset;
    }
    
    @Override
    public String toString() {
        return "SearchFeed{" + "response=" + response + ", new_offset=" + new_offset + '}';
    }


}
