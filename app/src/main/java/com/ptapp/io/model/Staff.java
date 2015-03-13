package com.ptapp.io.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lifestyle on 10-01-15.
 */
public class Staff {

    public int StaffId;
    public String DisplayName;
    public String FirstName;
    public String LastName;
    public String Email;
    public boolean IsAdmin;
    public String Mobile;
    public String CountryISOCode;
    public String Password;
    public int AddressId;
    public String QualificationLevel;
    public String QualificationName;
    public String StaffTypeDescription;
    public String StaffTypeCode;
    public int BranchId;
    public String ImageURL;
    public int UserId;
    @SerializedName("JabberId")
    public String Jid;

    public Staff(){}
}
