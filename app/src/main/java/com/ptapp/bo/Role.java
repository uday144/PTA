package com.ptapp.bo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by lifestyle on 09-12-14.
 */
public class Role {

    @SerializedName("Role")
    private String role;

    @SerializedName("RoleMessage")
    private String displayMsg;

    @SerializedName("Children")
    ArrayList<StudentBO> kids;

    @SerializedName("Name")
    private String name;

    @SerializedName("Gender")
    private String gender;      //F or M

    @SerializedName("SchoolName")
    private String school;

    @SerializedName("Classes")
    private ArrayList<String> classes;

    @SerializedName("StaffType")
    private String staffType;

    @SerializedName("ImageURL")
    private String imageUrl;


    //Either parentId or staffId or studentId, depending upon the role
    @SerializedName("Id")
    private int id;

    public String getDisplayMsg() {
        return displayMsg;
    }

    public void setDisplayMsg(String displayMsg) {
        this.displayMsg = displayMsg;
    }


    public ArrayList<String> getClasses() {
        return classes;
    }

    public void setClasses(ArrayList<String> classes) {
        this.classes = classes;
    }

    public String getStaffType() {
        return staffType;
    }

    public void setStaffType(String staffType) {
        this.staffType = staffType;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public ArrayList<StudentBO> getKids() {
        return kids;
    }

    public void setKids(ArrayList<StudentBO> kids) {
        this.kids = kids;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
