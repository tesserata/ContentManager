package org.ipccenter.newsagg.gson;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: darya
 * Date: 06.11.13
 * Time: 2:54
 * To change this template use File | Settings | File Templates.
 */
public class FeedItem {
    @SerializedName("date")
    private String date;
    @SerializedName("post_id")
    private String postID;
    @SerializedName("source_id")
    private StringBuilder sourceID;
    @SerializedName("post_type")
    private String postType;
    @SerializedName("copy_owner_id")
    private StringBuilder ownerID;
    @SerializedName("copy_post_id")
    private String copyPostID;
    @SerializedName("text")
    private String text;

    public boolean isCopy(){
        if (postType.equalsIgnoreCase("copy")) return true; else return false;
    }

    public Date getDate(){
        Date d = new Date();
        d.setTime(Long.valueOf(date));
        return d;
    }
    public String getPostAddress(){
        if (sourceID.toString().contains("-")) sourceID.deleteCharAt(0);
        return sourceID.toString() + '_' + postID;
    }
    public String getPostType(){ return postType;}
    public String getCopyPostAddress(){
        if (ownerID.toString().contains("-")) ownerID.deleteCharAt(0);
        return ownerID.toString() + '_' + copyPostID;
    }
    public String getText(){return text;}
}
