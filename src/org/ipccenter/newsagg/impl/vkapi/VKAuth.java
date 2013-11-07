package org.ipccenter.newsagg.impl.vkapi;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: darya
 * Date: 24.10.13
 * Time: 2:16
 * To change this template use File | Settings | File Templates.
 */

public class VKAuth {

    private static final String AUTHORIZE_URL = "http://oauth.vk.com/authorize";
    private static final String DEFAULT_REDIRECT_URL = "http://oauth.vk.com/blank.html";
    private static final String ACCESS_TOKEN_URL = "https://oauth.vk.com/access_token";

    private String clientID;
    private String redirectUrl = DEFAULT_REDIRECT_URL;

    private String login;
    private String password;

    private String accessToken;

    private boolean authSuccess;

    public VKAuth(String clientID, String login, String password) {
        this.clientID = clientID;
        this.login = login;
        this.password = password;
        authSuccess = false;
    }

    void authorize() throws IllegalAccessException {
        Map<String, String> requestParams = new HashMap<String, String>();
        try {
            requestParams.put("client_id", clientID);
            requestParams.put("scope", "wall,friends");
            requestParams.put("redirect_url", redirectUrl);
            requestParams.put("display", "page");
            requestParams.put("response_type", "token");
            Connection getRequest = Jsoup.connect(AUTHORIZE_URL).data(requestParams);
            String action = getRequest.get().select("form").attr("action");
            Map paramsSend = new HashMap<String, String>();
            for (Element elem : getRequest.get().select("input[name]")) {
                paramsSend.put(elem.attr("name"), elem.attr("value"));
            }
            paramsSend.put("login", login);
            paramsSend.put("password", password);
            Connection.Response responce = Jsoup.connect(action).data(paramsSend).execute();
            String codeUrl = responce.url().toString();
            String code = codeUrl.substring(codeUrl.indexOf("=") + 1);
            if (codeUrl.contains("error")) {
                throw new IllegalAccessException("Authentification error");
            }
            Map<String, String> accessTokenRequestParams = new HashMap<String, String>();
            accessTokenRequestParams.put("client_id", clientID);
            accessTokenRequestParams.put("code", code);
            accessTokenRequestParams.put("redirect_url", DEFAULT_REDIRECT_URL);
            Connection accessTokenRequest = Jsoup.connect(ACCESS_TOKEN_URL).data(accessTokenRequestParams);
            Connection.Response accessTokenResponce =
                    Jsoup.connect(accessTokenRequest.get().select("form").attr("action")).data(accessTokenRequestParams).execute();
            accessToken = accessTokenResponce.parse().select("access_token").val();
            authSuccess = true;
        } catch (IOException e) {
            authSuccess = false;
        }

        if (!authSuccess) throw new IllegalAccessException("Can't authentificate");

    }

    public String getAccessToken(){
        if (authSuccess)
            return accessToken;
        return null;
    }

    public String getLogin(){
        if (authSuccess)
            return login;
        return null;
    }

    public String getPassword(){
        if (authSuccess)
            return password;
        return null;
    }

    public String getRedirectUrl(){
        if (authSuccess)
            return redirectUrl;
        return null;
    }

}
