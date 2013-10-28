package api;

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

    public static final String AUTHORIZE_ADDRESS = "http://oauth.vk.com/authorize";
    public final static String DEFAULT_REDIRECT_URL = "http://oauth.vk.com/blank.html";
    public final static String ACCESS_TOKEN_URL = "https://oauth.vk.com/access_token";

    public String clientID;
    public String clientSecret;
    public String scope;
    public String redirectUrl = DEFAULT_REDIRECT_URL;
    public String display;

    public String login;
    public String password;

    public String accessToken;

    public boolean authSuccess;

    void authorize() throws IOException, IllegalAccessException {
        Map requestParams = new HashMap<String, String>();
        try {
            requestParams.put("client_id", clientID);
            requestParams.put("scope", scope);
            requestParams.put("redirect_url", redirectUrl);
            requestParams.put("display", display);
            requestParams.put("responce_type", "token");
            Connection getRequest = Jsoup.connect(AUTHORIZE_ADDRESS).data(requestParams);
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
            accessTokenRequestParams.put("client_secret", clientSecret);
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


}
