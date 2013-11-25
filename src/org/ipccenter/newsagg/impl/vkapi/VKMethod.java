package org.ipccenter.newsagg.impl.vkapi;

/**
 * Created with IntelliJ IDEA.
 * User: darya
 * Date: 05.11.13
 * Time: 12:43
 * To change this template use File | Settings | File Templates.
 */

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class VKMethod {
    private String methodName;
    private VKAuth auth;
    private Map<String, String> params;

    private static final Logger LOG = LoggerFactory.getLogger(VKMethod.class);

    public VKMethod(String methodName, VKAuth auth) {
        this.methodName = methodName;
        this.auth = auth;
        params = new HashMap<String, String>();
    }

    public VKMethod addParam(String key, String value) {
        String currentValue = params.get(key);
        String newValue = (currentValue == null ? "" : currentValue + ",") + value;
        params.put(key, newValue);
        return this;
    }

    public String execute() throws IOException {
        LOG.info("Data: {}", params);
        LOG.info("Data: access_token:{}", auth.getAccessToken());
        final Connection connect = Jsoup.connect("https://api.vk.com/method/" + methodName)
                //.data("api_id", "4017304")
                //.data("method", methodName)
                //.data("sig", makeSig())
                //.data("sid", "")
                .data(params)
                .data("access_token", auth.getAccessToken())
                .ignoreContentType(true);
        LOG.info("Connection request: {}", connect.request().data());
        Connection.Response result = connect
                .execute();
        return result.body();
    }

    public String getMethodName() {
        return methodName;
    }
    
    /*public String makeSig() throws NoSuchAlgorithmException{
        StringBuilder sig = new StringBuilder();
        sig.append(auth.getUserID())
                .append("api_id=4017304")
                .append("method=").append(methodName)
                .append("KJRPvWDdaD5hcPxXtsn4");
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] input = String.valueOf(sig).getBytes();
        input = md.digest(input);
        return input.toString();
    }*/

    public Map<String, String> getParams() {
        return params;
    }

    @Override
    public String toString() {
        return "VKMethod{" + "methodName=" + methodName + ", params=" + params + '}';
    }
}
