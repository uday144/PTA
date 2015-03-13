package com.ptapp.bo;

import com.ptapp.bean.ClassBean;
import com.ptapp.bean.FeeBean;
import com.ptapp.bean.MessagesBean;
import com.ptapp.bean.ParentBean;
import com.ptapp.bean.SchoolBean;
import com.ptapp.bean.TagsBean;
import com.ptapp.bean.TransportBean;
import com.ptapp.io.model.Student;

import java.util.ArrayList;

public class StudentContextBO {

    private ArrayList<ParentBean> lstParents;
    private Student student;
    private ClassBean schoolClass;
    /*private ArrayList<Course> lstCourses;
    private ArrayList<Educator> lstEducators;*/
    private ArrayList<MessagesBean> lstMessages;
    private ArrayList<TagsBean> lstTags;
    private SchoolBean school;
    private FeeBean fee;
    private TransportBean transport;


    public StudentContextBO() {

    }

    public ArrayList<ParentBean> getLstParents() {
        return lstParents;
    }

    public void setLstParents(ArrayList<ParentBean> lstParents) {
        this.lstParents = lstParents;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public ClassBean getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(ClassBean schoolClass) {
        this.schoolClass = schoolClass;
    }

    /*public ArrayList<Course> getLstCourses() {
        return lstCourses;
    }

    public void setLstCourses(ArrayList<Course> lstCourses) {
        this.lstCourses = lstCourses;
    }

    public ArrayList<Educator> getLstEducators() {
        return lstEducators;
    }

    public void setLstEducators(ArrayList<Educator> lstEducators) {
        this.lstEducators = lstEducators;
    }*/

    public ArrayList<MessagesBean> getLstMessages() {
        return lstMessages;
    }

    public void setLstMessages(ArrayList<MessagesBean> lstMessages) {
        this.lstMessages = lstMessages;
    }

    public SchoolBean getSchool() {
        return school;
    }

    public void setSchool(SchoolBean school) {
        this.school = school;
    }

    public FeeBean getFee() {
        return fee;
    }

    public void setFee(FeeBean fee) {
        this.fee = fee;
    }

    public TransportBean getTransport() {
        return transport;
    }

    public void setTransport(TransportBean transport) {
        this.transport = transport;
    }

    public ArrayList<TagsBean> getLstTags() {
        return lstTags;
    }

    public void setLstTags(ArrayList<TagsBean> lstTags) {
        this.lstTags = lstTags;
    }
}
