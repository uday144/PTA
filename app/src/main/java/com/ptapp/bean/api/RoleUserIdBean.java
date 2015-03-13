package com.ptapp.bean.api;

/**
 * Save result on return of Register API
 */
//TODO:Check if this class is required or not and delete it , if not
public class RoleUserIdBean {

    private String role;
    private String userId;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}


