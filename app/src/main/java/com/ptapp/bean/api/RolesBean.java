package com.ptapp.bean.api;

import com.google.gson.annotations.SerializedName;
import com.ptapp.bo.Role;

import java.util.ArrayList;

public class RolesBean {

    @SerializedName("SuccessMessage")
    private String successMsg;

    @SerializedName("UserId")
    private int userId;

    @SerializedName("Roles")
    private ArrayList<Role> roles;




    public String getSuccessMsg() {
        return successMsg;
    }

    public void setSuccessMsg(String successMsg) {
        this.successMsg = successMsg;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public ArrayList<Role> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<Role> roles) {
        this.roles = roles;
    }
}


