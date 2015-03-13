package com.ptapp.bean;

/**
 * This class is our model and contains the data we will save in the database
 * and show in the user interface.
 */
public class MessagesBean {
    // private variables
    private long id;
    private String msgText;
    private String fromCID;
    private String toCID;
    private String at;
    private String status;

    // Empty constructor
    public MessagesBean() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public String getFromCID() {
        return fromCID;
    }

    public void setFromCID(String fromCID) {
        this.fromCID = fromCID;
    }

    public String getToCID() {
        return toCID;
    }

    public void setToCID(String toCID) {
        this.toCID = toCID;
    }

    public String getAt() {
        return at;
    }

    public void setAt(String at) {
        this.at = at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
