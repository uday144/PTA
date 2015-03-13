package com.ptapp.dao;

import android.content.Context;

import com.ptapp.bo.StudentContextBO;

/**
 * All CRUD(Create, Read, Update, Delete) Operations
 */
public class StudentContextDAO {
    private String TAG = "StudentContextDAO";

    private Context context;

    // Database fields
    // private SQLiteDatabase db;
    // private PTAppDbHelper dbHelper;

    /**
     * To access the database, instantiate the subclass of SQLiteOpenHelper,
     * i.e. PTAppDbHelper
     */
    public StudentContextDAO(Context context) {
        this.context = context;
        // dbHelper = new PTAppDbHelper(context);
    }

    public void close() {
        // dbHelper.close();
    }

    public StudentContextBO getStudentContext(String studentId) {
        StudentContextBO boStuContext = null;
        StudentDAO daoStu = new StudentDAO(context);
        ClassDAO daoClass = new ClassDAO(context);
        CourseDAO daoCourse = new CourseDAO(context);
        EducatorDAO daoEducator = new EducatorDAO(context);
        ParentDAO daoParent = new ParentDAO(context);
        FeeDAO daoFee = new FeeDAO(context);
        TransportDAO daoTransport = new TransportDAO(context);

        /*try {
            StudentBean boStu = daoStu.getStudent(studentId);
            if (boStu != null) {
                ClassBean boClas = daoClass.getSchoolClass(boStu.getClassId());
                ArrayList<Course> lst_Courses = daoCourse
                        .getStudentCourses(studentId, boStu.getClassId());
                ArrayList<Educator> lst_Educators = daoEducator
                        .getStudentEducators(lst_Courses);
                ArrayList<ParentBean> lst_Parents = daoParent.getParents();
                FeeBean f = daoFee.getFee(boClas.getFeeId());
                TransportBean trs = daoTransport.getTransport(boClas
                        .getTransportId());

                boStuContext = new StudentContextBO();
                boStuContext.setStudent(boStu);
                boStuContext.setSchoolClass(boClas);
                boStuContext.setLstCourses(lst_Courses);
                boStuContext.setLstEducators(lst_Educators);
                boStuContext.setLstParents(lst_Parents);
                boStuContext.setFee(f);
                boStuContext.setTransport(trs);
            }
        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                Log.e(TAG, ex.getMessage());
            } else {
                Log.e(TAG, "get studentContext - No error msg for this error.");
            }
        }*/
        return boStuContext;
    }
}
