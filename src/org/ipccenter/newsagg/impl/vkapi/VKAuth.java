package org.ipccenter.newsagg.impl.vkapi;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author spitty
 */
public class VKAuth {

    private static final Logger LOGGER = LoggerFactory.getLogger(VKAuth.class);
    public static final String DEFAULT_REDIRECT_URL = "http://oauth.vk.com/blank.html";
    private static String clientID = "4017304";
    //private String email;
    //private String password;
    private String redirectURL = DEFAULT_REDIRECT_URL;
    private String accessToken;
    private String userID;
    private boolean authSuccessful = false;
    private boolean authProcessed = false;
    private static String authURL;

    public VKAuth() {
        return;
    }

    public VKAuth(String clientID, String email, String password) {
        this.clientID = clientID;
        //this.email = email;
        //this.password = password;
        authProcessed = false;
        authSuccessful = false;
        accessToken = null;
    }

    public VKAuth(String accessToken, String userID) {
        this.accessToken = accessToken;
        this.userID = userID;
        authSuccessful = true;
        authProcessed = true;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getClientID() {
        return clientID;
    }

    public String getAccessToken() {
        //checkAuthSuccess();
        return accessToken;
    }

    public String getUserID() {
        //checkAuthSuccess();
        return userID;
    }

    public boolean isAuthSuccessful() {
        return authSuccessful;
    }

    /*private String getEmail() {
        return email;
    }

    private String getPassword() {
        return password;
    }*/
    
    /*private void buildAuthUrl(){
        StringBuilder sb = new StringBuilder();
        sb.append("http://oauth.vk.com/oauth/authorize?");
        sb.append("client_id").append("=").append("4017304").append("&");
        sb.append("redirect_uri").append("=").append(StringEscapeUtils.escapeHtml4(DEFAULT_REDIRECT_URL)).append("&");
        sb.append("scope").append("=").append("wall,friends").append("&");
        sb.append("display").append("=").append("page").append("&");
        sb.append("response_type").append("=").append("token");
        authURL = sb.toString();
    }*/

    public static String getAuthURL() {
        return authURL;
    }

    public void authenticate() {
        try {
            Map<String, String> params = new HashMap<String, String>();
            if (getClientID() == null) {
                throw new IllegalStateException("Please specify credential");
            }

            params.put("client_id", getClientID());
            params.put("redirect_uri", redirectURL == null ? DEFAULT_REDIRECT_URL : redirectURL);
            params.put("scope", "wall,friends");
            params.put("display", "popup");
            params.put("response_type", "token");
            LOGGER.debug("Try to obtain page by next params: {}", params);
            Connection getLoginPageRequest = Jsoup.connect("http://oauth.vk.com/oauth/authorize");
            getLoginPageRequest.data(params);
            Document doc = getLoginPageRequest.get();

            LOGGER.trace("Login page (raw HTML): {}", doc);
            Element form = doc.select("form").first();
            String action = form.attr("action");
            LOGGER.debug("Action: " + action);

            Elements inputs = doc.select("input[name]");
            Map<String, String> paramsToSend = new HashMap<String, String>();
            for (Element e : inputs) {
                paramsToSend.put(e.attr("name"), e.attr("value"));
                LOGGER.debug("{} = \"{}\"", e.attr("name"), e.attr("value"));
            }
//            LOGGER.info("Next parameters will be sent: {}", paramsToSend);
            Connection.Response execute = Jsoup.connect(action)
                    .data(paramsToSend)
                    .execute();
            String url = execute.url().toString();
//            String __q_hash = url.substring(url.indexOf("q_hash")+6);

            LOGGER.info("Response URL: {}", url);

//            Document requestGrantPage = execute.parse();
//            Element form2 = requestGrantPage.select("form").first();
//            String action2 = form2.attr("action");
//            
//            LOGGER.info("MALO Request Grant Page: " + requestGrantPage);
//            LOGGER.info("MALO Action: " + action2);
//            Connection.Response execute2 = Jsoup.connect(action2).method(Connection.Method.POST)
//                    .execute();
//            LOGGER.info("MALO Login page (raw HTML): {}", execute2);
//            String url2 = execute2.url().toString();
//            LOGGER.info("MALO Response URL: {}", url2);
            /*Map<String, String> authParams = new HashMap<String, String>();
            authParams.put("role", "fast");
            authParams.put("redirect", "1");
            authParams.put("to", "");
            authParams.put("s", "1");
            authParams.put("__q_hash", __q_hash);
            Connection.Response authExec = Jsoup.connect(action)
                    .data(authParams)
                    .execute();
            
            String authUrl = authExec.url().toString();
            LOGGER.info("AuthExec url: {}", authUrl);*/

            if (!url.contains("access_token")) {
                LOGGER.warn("\"access_token\" absents. Probably credentials are wrong");
                LOGGER.info("Response body: {}", execute.body());
                Document errorLoginPage = execute.parse();
                String warnMessage = errorLoginPage.select("div.service_msg_warning").text();
                authProcessed = true;
                authSuccessful = false;
                throw new IllegalArgumentException("Authentification fails. Please check credentials. VK warn message is "
                        + "\"" + warnMessage + "\"");
            }

            String urlParams = url.substring(url.indexOf("#") + 1);
            LOGGER.debug("URL params: {}", urlParams);
            Map<String, String> result = new HashMap<String, String>();
            for (String keyValue : urlParams.split("&")) {
                String s[] = keyValue.split("=");
                if (s.length == 2) {
                    result.put(s[0], s[1]);
                }
            }
            LOGGER.debug("Parsed params: {}", result);


            accessToken = result.get("access_token");
            userID = result.get("user_id");
            LOGGER.debug("Access token: {}", accessToken);
            LOGGER.debug("User ID: {}", userID);
            authSuccessful = true;
        } catch (IOException ex) {
            LOGGER.error("Can't authenticate", ex);
            authSuccessful = false;
        }
        authProcessed = true;
    }

    private void checkAuthSuccess() throws IllegalStateException {
        if (!authProcessed) {
            authenticate();
        }
        if (!authSuccessful) {
            throw new IllegalStateException("Can't authenticate with specified credentials");
        }
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
} 