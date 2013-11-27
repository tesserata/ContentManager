package org.ipccenter.newsagg.gson;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: darya
 * Date: 06.11.13
 * Time: 2:54
 * To change this template use File | Settings | File Templates.
 */

public class FeedItem {

    private static final Logger LOG = LoggerFactory.getLogger(FeedItem.class);

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

    public boolean isCopy() {
        return postType.equalsIgnoreCase("copy");
    }

    public Date getDate() {
        Date d = new Date();
        LOG.info("Date unix value: {}", date);
        LOG.info("Date value: {}", d);
        return d;
    }

    public String getPostAddress() {
        return "wall" + sourceID.toString() + '_' + postID;
    }

    public String getPostType() {
        return postType;
    }

    public String getCopyPostAddress() {
        return "wall" + ownerID.toString() + '_' + copyPostID;
    }

    public String getText() {
        return StringEscapeUtils.unescapeHtml3(text);
    }

}
