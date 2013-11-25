
import com.google.gson.annotations.SerializedName;
import org.ipccenter.newsagg.gson.FeedItem;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author darya
 */
public class Feed {
    @SerializedName("items")
    private FeedItem[] items;

    public FeedItem[] getItems() {
        return items;
    }   
    
    @Override
    public String toString() {
        return "Feed{" + "items=" + items + '}';
    }
}
