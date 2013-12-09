/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ipccenter.newsagg.impl.vkapi;

import java.io.IOException;
import java.util.List;
import org.ipccenter.newsagg.entity.News;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author darya
 */
public class VKPusher {
    
    public static final Logger LOG = LoggerFactory.getLogger(VKPusher.class);
    
    private VKAuth auth;
    
    public VKPusher(VKAuth auth){
        this.auth = auth;
    }
    
    public void postCurrent(News news, String destination) throws IOException{
        VKMethod post = new VKMethod("wall.post", auth);
        String ownerID = null;
        switch(destination){
            case("Main"): ownerID = "-17708";
            case("01x"): ownerID = "-16716609";
            case("11x"): ownerID = "-28501321";
            case("21x"): ownerID = "-40060965";
            case("31x"): ownerID = "-55239167";
            case("minitrue"): ownerID = "-20339856";
        }
                
        post.addParam("owner_id", String.valueOf(ownerID))
                .addParam("from_group", "1")
                .addParam("message", news.getContent());
        
        String response = post.execute();
    }
    
    public void repost(News news, String destination) throws IOException{
        VKMethod repost = new VKMethod("wall.repost", auth);
        String groupID = null;
        switch(destination){
            case("Main"): groupID = "17708";
            case("01x"): groupID = "16716609";
            case("11x"): groupID = "28501321";
            case("21x"): groupID = "40060965";
            case("31x"): groupID = "55239167";
            case("minitrue"): groupID = "20339856";
        }
        String object = news.getUrl().substring(news.getUrl().lastIndexOf('/')+1);
        repost.addParam("object", object).addParam("group_id", groupID).addParam("message", "Test again");
        
        String response = repost.execute();
        
    }
    
    public void postNew(String message, String destination) throws IOException{
        VKMethod post = new VKMethod("wall.post", auth);
        String ownerID = null;
        switch(destination){
            case("Main"): ownerID = "-17708";
            case("01x"): ownerID = "-16716609";
            case("11x"): ownerID = "-28501321";
            case("21x"): ownerID = "-40060965";
            case("31x"): ownerID = "-55239167";
            case("minitrue"): ownerID = "-20339856";
        }
                
        post.addParam("owner_id", String.valueOf(ownerID))
                .addParam("from_group", "1")
                .addParam("signed", "1")
                .addParam("message", message);
        
        String response = post.execute();
    }
    
    public void postToMyPage(String message) throws IOException{
        VKMethod post = new VKMethod("wall.post", auth);
        String ownerID = auth.getUserID();
        post.addParam("owner_id", ownerID)
                .addParam("services", "twitter");
        post.addParam("message", message.toString());
        
        String response = post.execute();
    }
    
}
