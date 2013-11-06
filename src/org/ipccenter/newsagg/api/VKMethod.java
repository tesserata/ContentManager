package org.ipccenter.newsagg.api;

/**
 * Created with IntelliJ IDEA.
 * User: darya
 * Date: 05.11.13
 * Time: 12:43
 * To change this template use File | Settings | File Templates.
 */

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class VKMethod {
    private String methodName;
    private VKAuth auth;
    private Map<String, String> params;

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
        Connection.Response result = Jsoup.connect("https://api.vk.com/method/" + methodName)
                .data(params)
                .data("access_token", auth.getAccessToken())
                .ignoreContentType(true)
                .execute();
        return result.body();
    }

    public String getMethodName() {
        return methodName;
    }

    public Map<String, String> getParams() {
        return params;
    }

    @Override
    public String toString() {
        return "VKMethod{" + "methodName=" + methodName + ", params=" + params + '}';
    }
}
