package com.ptapp.bean;

/**
 * This class is our model and contains the data we will save in the database
 * and show in the user interface.
 */
public class MsgProfileBean {
    // private variables
    private long id;
    private String name;
    private String chatId;
    private int count;
    private int isGroup;

    // Empty constructor
    public MsgProfileBean() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(int isGroup) {
        this.isGroup = isGroup;
    }

}
