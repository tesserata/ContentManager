/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ipccenter.newsagg.gson;

import com.google.gson.annotations.SerializedName;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author darya
 */
public class SearchFeedItem {
    private static final Logger LOG = LoggerFactory.getLogger(FeedItem.class);

    @SerializedName("date")
    private String date;
    @SerializedName("id")
    private String postID;
    @SerializedName("owner_id")
    private StringBuilder ownerID;
    @SerializedName("text")
    private String text;

    
    public Date getDate() {
        Date d = new Date();
        return d;
    }

    public String getPostID() {
        return postID;
    }

    public StringBuilder getOwnerID() {
        return ownerID;
    }

    public String getText() {
        return text;
    }
}
