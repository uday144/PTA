package com.ptapp.bean.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lifestyle on 09-03-15.
 */
public class ResponseMessage {

    @SerializedName("Message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
