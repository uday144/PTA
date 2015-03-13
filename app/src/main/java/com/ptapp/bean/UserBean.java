package com.ptapp.bean;

/**
 * This class is our model and contains the data we will save in the database
 * and show in the user interface.
 */
public class UserBean {
    // private variables
    private long id;
    private String userId;
    private String userType;


    // Empty constructor
    public UserBean() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

}
