package com.ptapp.bean.api;

import java.util.ArrayList;

/**
 * Created by lifestyle on 20-12-14.
 */
//TODO:Check if this class is required or not, if not, then delete it.
public class UserIdsBean {

    ArrayList<RoleUserIdBean> users;

    public ArrayList<RoleUserIdBean> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<RoleUserIdBean> users) {
        this.users = users;
    }


}
