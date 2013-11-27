package org.ipccenter.newsagg.gson;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author darya
 */
public class Feed {
    private FeedItem[] items;

    private int new_offset;

    public FeedItem[] getItems() {
        return items;
    }

    public int getNewOffset() {
        return new_offset;
    }

    @Override
    public String toString() {
        return "Feed{" + "items=" + items + ", new_offset=" + new_offset + '}';
    }
}
