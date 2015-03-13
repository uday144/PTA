/**
 *
 */
package com.ptapp.bo;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

/**
 * This class contains info required to represent a student. The detailed info
 * is available in StudentContextBO.
 */
public class StudentBO {

    @SerializedName("Name")
    private String name;

    @SerializedName("Gender")
    private String gender;

    @SerializedName("UserId")
    private int userId;

    @SerializedName("EnrollmentNo")
    private String rollNumber;

    @SerializedName("Class")
    private String stuClass;

    @SerializedName("ImageURL")
    private String imageUrl;

    @SerializedName("SchoolName")
    private String school;

    // unique id in database. Will be used to populate StudentContextBO whenever
    // required at later point.
    @SerializedName("Id")
    private int studentId;


    // mycontext will be lazy loaded: meaning only when the getter is invoked
    private StudentContextBO mycontext;


    public StudentBO() {

    }

    public StudentContextBO getStudentContextBO(Context context) {
        if (null == mycontext) {
            // Lazy loading: populate and return only when the instance is
            // requested
            //mycontext = new StudentContextDAO(context).getStudentContext(studentId);

        }
        return mycontext;
    }

    public void setStudentContextBO(StudentContextBO context) {
        this.mycontext = context;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStuClass() {
        return stuClass;
    }

    public void setStuClass(String stuClass) {
        this.stuClass = stuClass;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
}
