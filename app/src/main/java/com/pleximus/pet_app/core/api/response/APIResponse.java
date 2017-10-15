package com.pleximus.pet_app.core.api.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pleximus on 21/04/17.
 */

public class APIResponse {

    @SerializedName("error")
    private String error;
    @SerializedName("data")
    private String data;
    @SerializedName("message")
    private String message;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
