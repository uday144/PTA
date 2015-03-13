package com.ptapp.io.model;

import com.google.gson.annotations.SerializedName;

/**
 * This class is our model and contains the data we will save in the database
 * and show in the user interface.
 */
public class Student {

    public int StudentId;
    public String Allergies;
    public String dob;
    public String Email;
    public String FirstName;
    public String Gender;
    public String LastName;
    public String Mobile;
    public String CountryISOCode;
    public String SpecialInstructions;
    public int AddressId;
    public int BranchId;
    public String ImageURL;
    public int UserId;
    public String EnrollmentNo;
    @SerializedName("JabberId")
    public String Jid;

	// Empty constructor
	public Student() {

	}


}
