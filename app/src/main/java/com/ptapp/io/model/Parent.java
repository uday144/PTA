package com.ptapp.io.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lifestyle on 10-01-15.
 */
public class Parent {

    public int ParentId;
    public String FirstName;
    public String LastName;
    public String Email;
    public String Gender;
    public String Mobile;
    public String CountryISOCode;
    public String Qualification;
    public int AddressId;
    public String ImageURL;
    public int UserId;
    @SerializedName("JabberId")
    public String Jid;

    public Parent() {
    }
}
