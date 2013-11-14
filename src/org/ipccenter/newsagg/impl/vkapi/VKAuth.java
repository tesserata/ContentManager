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
    private String clientID;
    private String email;
    private String password;
    private String redirectURL = DEFAULT_REDIRECT_URL;
    private String accessToken;
    private String userID;
    private boolean authSuccessful = false;
    private boolean authProcessed = false;

    public VKAuth(String clientID, String email, String password) {
        this.clientID = clientID;
        this.email = email;
        this.password = password;
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

    public String getClientID() {
        return clientID;
    }

    public String getAccessToken() {
        checkAuthSuccess();
        return accessToken;
    }

    public String getUserID() {
        checkAuthSuccess();
        return userID;
    }

    public boolean isAuthSuccessful() {
        return authSuccessful;
    }

    private String getEmail() {
        return email;
    }

    private String getPassword() {
        return password;
    }

    public void authenticate() {
        try {
            Map<String, String> params = new HashMap<String, String>();
            if (getClientID() == null || getPassword() == null || getEmail() == null) {
                throw new IllegalStateException("Please specify credential");
            }

            params.put("client_id", getClientID());
            params.put("redirect_uri", redirectURL == null ? DEFAULT_REDIRECT_URL : redirectURL);
            params.put("scope", "wall,friends");
            params.put("display", "page");
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
            LOGGER.debug("Next parameters will be sent: {}", paramsToSend);
            paramsToSend.put("email", email);
            paramsToSend.put("pass", password);
            Connection.Response execute = Jsoup.connect(action)
                    .data(paramsToSend)
                    .execute();
            String url = execute.url().toString();

            if (!url.contains("access_token")) {
                LOGGER.warn("\"access_token\" absents. Probably credentials are wrong");
                LOGGER.trace("Response body: {}", execute.body());
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
}