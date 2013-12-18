/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ipccenter.newsagg.bean.jsf;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.commons.lang3.StringEscapeUtils;
import static org.ipccenter.newsagg.impl.twitterapi.TwitterPuller.LOG;
import org.ipccenter.newsagg.impl.vkapi.VKAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;

/**
 *
 * @author darya
 */
@ManagedBean
@SessionScoped
public class AuthorizationBean {
    
    public static final Logger LOG = LoggerFactory.getLogger(AuthorizationBean.class);
    
    private String vkAccessToken;
    private String vkID;
    private VKAuth vkAuth;
    private String vkAuthUrl;
    
    private String twiPin;
    private Twitter twiAuth;
    private String twiAuthUrl;
    
    public AuthorizationBean() throws TwitterException{
        this.vkAuth = new VKAuth();
        this.twiAuth = new TwitterFactory().getInstance();
        this.twiAuth.setOAuthConsumer("X6CTYpAt53aq71iBtTEMQ", "ApTAXimRwQ0361gc8aCDhqZAKcnXFTgwigrN445opI");
        buildTwiAuthUrl();
        buildVKAuthUrl();
    }
    
    public void buildTwiAuthUrl() throws TwitterException {
        RequestToken rt = twiAuth.getOAuthRequestToken();
        twiAuthUrl = rt.getAuthorizationURL();
    }

    private void buildVKAuthUrl() {
        StringBuilder sb = new StringBuilder();
        sb.append("http://oauth.vk.com/oauth/authorize?");
        sb.append("client_id").append("=").append("4017304").append("&");
        sb.append("redirect_uri").append("=").append(StringEscapeUtils.escapeHtml4("http://oauth.vk.com/blank.html")).append("&");
        sb.append("scope").append("=").append("wall,friends,messages").append("&");
        sb.append("display").append("=").append("page").append("&");
        sb.append("response_type").append("=").append("token");
        vkAuthUrl = sb.toString();
    }

   public void toAuthVK() {
        this.vkAuth.setAccessToken(this.vkAccessToken);
        this.vkAuth.setUserID(this.vkID);
    }

    public void toAuthTwi() throws TwitterException {
        this.twiAuth.getOAuthAccessToken(this.twiPin);
    }
    
    public void toAuthorize() throws TwitterException{
        LOG.info("Access token: {}", vkAccessToken);
        LOG.info("ID: {}", vkID);
        LOG.info("PIN: {}", twiPin);
        toAuthVK();
        toAuthTwi();
        LOG.info("TwiAuth: {}", twiAuth);
        LOG.info("VKAuth: {}", vkAuth);
    }
    
    public void clear(){
        this.vkAccessToken = null;
        this.vkID = null;
        this.vkAuth = null;
        
        this.twiAuth = null;
        this.twiPin = null;
    }

    public String getVkAccessToken() {
        return vkAccessToken;
    }

    public void setVkAccessToken(String vkAccessToken) {
        this.vkAccessToken = vkAccessToken;
    }

    public String getVkID() {
        return vkID;
    }

    public void setVkID(String vkID) {
        this.vkID = vkID;
    }

    public VKAuth getVkAuth() {
        return vkAuth;
    }

    public void setVkAuth(VKAuth vkAuth) {
        this.vkAuth = vkAuth;
    }

    public String getVkAuthUrl() {
        return vkAuthUrl;
    }

    public void setVkAuthUrl(String vkAuthUrl) {
        this.vkAuthUrl = vkAuthUrl;
    }

    public String getTwiPin() {
        return twiPin;
    }

    public void setTwiPin(String twiPin) {
        this.twiPin = twiPin;
    }

    public Twitter getTwiAuth() {
        return twiAuth;
    }

    public void setTwiAuth(Twitter twiAuth) {
        this.twiAuth = twiAuth;
    }

    public String getTwiAuthUrl() {
        return twiAuthUrl;
    }

    public void setTwiAuthUrl(String twiAuthUrl) {
        this.twiAuthUrl = twiAuthUrl;
    }
    
    
    
    
}
