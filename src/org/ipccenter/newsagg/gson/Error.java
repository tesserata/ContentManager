package org.ipccenter.newsagg.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created with IntelliJ IDEA.
 * User: darya
 * Date: 06.11.13
 * Time: 11:11
 * To change this template use File | Settings | File Templates.
 */
public class Error {
    @SerializedName("error_code")
    private int errorCode;
    @SerializedName("error_msg")
    private String errorMessage;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
