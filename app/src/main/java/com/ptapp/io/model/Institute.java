package com.ptapp.io.model;

/**
 * This class is our model and contains the data we will save in the database
 * and show in the user interface.
 */
public class Institute {
    // public variables

    public int InstituteId;
    public boolean Active;
    public String Email;
    public String InstituteName;
    public boolean IsRegistered;
    public String Phone;
    public String CountryISOCode;
    public String Website;
    public int AddressId;
    public String InstituteTypeDescription;
    public String InstituteTypeCode;


    // Empty constructor
    public Institute() {

    }

}
