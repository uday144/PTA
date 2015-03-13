package com.ptapp.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.ptapp.bean.ClassBean;

import com.ptapp.bean.FeeBean;
import com.ptapp.bean.MessagesBean;
import com.ptapp.bean.ParentBean;
import com.ptapp.bean.SchoolBean;
import com.ptapp.io.model.Student;
import com.ptapp.bean.TagsBean;
import com.ptapp.bean.TransportBean;
import com.ptapp.bo.StudentBO;
import com.ptapp.bo.StudentsBO;
import com.ptapp.dao.ClassDAO;
import com.ptapp.dao.FeeDAO;
import com.ptapp.dao.MessagesDAO;
import com.ptapp.dao.ParentDAO;
import com.ptapp.dao.SchoolDAO;
import com.ptapp.dao.StudentDAO;

import com.ptapp.dao.TransportDAO;
import com.ptapp.app.SchooloApp;
import com.ptapp.utils.CommonMethods;
import com.ptapp.utils.SharedPrefUtil;

import java.util.List;
import java.util.Map;

/**
 * Service handler for first time feed/load. - parse JSON and saves initial data
 * into database.
 */

public class FTLoadSH {
    private static final String TAG = "PTAppUI - FTLoadSH";
    private static FTLoadSH objFTLoadSH = null;
    private Context context;
    private static SharedPreferences sharedPref;

    /*
     * A private Constructor prevents any other class from instantiating.
     */
    private FTLoadSH(Context context) {
        this.context = context;
    }

    public static FTLoadSH getInstance(Context context) {
        if (objFTLoadSH == null) {
            objFTLoadSH = new FTLoadSH(context);
            sharedPref = SchooloApp.sharedPref;
        }
        return objFTLoadSH;
    }

    // Get the FTLoad feed - JSON String.
    private String getFTJSONString() {
        ServiceProxy sp = new ServiceProxy();
        return sp.getFTFeed();
    }

    // Parsing and inserting into DB.
    public void parseJSON() {
        // try {
        String js = getFTJSONString();
        // TODO:remove this writing to log file, before launch.
        CommonMethods.writeToMyLogFile(js);
        Log.v(TAG, "res: " + js);

        Gson gs = new Gson();
        StudentsBO ff = gs.fromJson(js, StudentsBO.class);

        // list of student contexts.
        // If parents and school are same for the student, theirs respective
        // sections in the JSON string will be null.

        processFTFeed(ff);

        // } catch (Exception ex) {
        // Log.e(TAG, ex.getMessage());
        // }

    }

    /**
     * set a random color to each student, save it in the shared
     * prefrerences, if not present earlier.
     */
    private void assignColorsToStudents(StudentsBO result) {
        Map<Integer, StudentBO> map = result.getStudentsAll();
        if (map != null) {
            for (Map.Entry<Integer, StudentBO> entry : map.entrySet()) {

                // set random color, if there is no color saved
                // previously
                // in shared preferences.
                /*sharedPref = ((SchooloApp) getApplicationContext())
                        .getSharedPref();*/
                /*int spColor = sharedPref.getInt( entry.getKey(), 0);

                if (spColor == 0) {

                    // generate a random color
                    Random rnd = new Random();
                    int color = Color.argb(255, rnd.nextInt(256),
                            rnd.nextInt(256), rnd.nextInt(256));

                    // save this color to shared preferences
                    SharedPreferences.Editor edColor = sharedPref.edit();
                    edColor.putInt(entry.getKey(), color);
                    boolean resColor = edColor.commit();
                    if (resColor) {
                        Log.i(TAG,
                                "Default color has been set in SP for studentId: "
                                        + entry.getKey());
                    } else {
                        Log.i(TAG,
                                "Couldn't set default color in SP for studentId: "
                                        + entry.getKey());
                    }
                }*/
            }
        } else {
            Log.wtf(TAG, "map is null.");
        }
    }


    /**
     * @param ff
     */
    public void processFTFeed(StudentsBO ff) {

        Map<Integer, StudentBO> stus = ff.getStudentsAll();
        int s = stus.size();
        Log.v(TAG, "Num of StudentContextBO: " + s);

		/*
         * First add parents and school infor first and then add the other
		 * tables because, in JSON parents and school value can be null for
		 * other kids of same parents or school.
		 */

        // Ids to use repeatedly, to set the values in DB for subsequent kids.
        String p1Id = "";
        String p2Id = "";
        String gId = ""; // guardian ID.
        String schId = "";
        /*for (StudentBO item : stus.values()) {
            Log.i(TAG, "inserting parents and school info first....");


        }*/

        for (StudentBO item : stus.values()) {
            Log.i(TAG, "inserting into database, first load data...");
            // StudentContextBO sc = new StudentContextBO();

            SchoolBean school = item.getStudentContextBO(context).getSchool();
            Student student = item.getStudentContextBO(context)
                    .getStudent();
            ClassBean schClass = item.getStudentContextBO(context)
                    .getSchoolClass();
            /*List<Course> courses = item.getStudentContextBO(context)
                    .getLstCourses();
            List<Educator> educators = item.getStudentContextBO(context)
                    .getLstEducators();*/
            // TODO: FIX THE parents and guardian prob. find a better way to
            // distinguish the father, momther and guardian
            List<ParentBean> parents = item.getStudentContextBO(context)
                    .getLstParents();
            List<MessagesBean> messages = item.getStudentContextBO(context).getLstMessages();
            List<TagsBean> tags = item.getStudentContextBO(context).getLstTags();
            FeeBean fee = item.getStudentContextBO(context).getFee();
            TransportBean transport = item.getStudentContextBO(context)
                    .getTransport();


            if (school != null) {
                // add school to DB
                SchoolDAO daoSch = new SchoolDAO(context);
                /*if (daoSch.getSchool(school.getId()) == null) {
                    daoSch.addSchool(school);
                }*/

                schId = school.getId();
            }

            // add courses to DB
            /*if (courses != null) {
                Log.i(TAG, "no of Courses: " + courses);

                for (Course c : courses) {
                    c.setStudentId(student.getId());
                    c.setClassId(schClass.getId());
                    CourseDAO daoCrs = new CourseDAO(context);
                    //daoCrs.addCourse(c);
                }
            } else {
                Log.i(TAG, "no of Courses: null");
            }*/

            // add educators to DB
            /*if (educators != null) {
                Log.i(TAG, "no of Educators: " + educators);

                for (Educator e : educators) {
                    EducatorDAO daoE = new EducatorDAO(context);
                   // daoE.addEducator(e);
                }
            } else {
                Log.i(TAG, "no of Educators: null");
            }*/

            // add parents to DB
            if (parents != null) {
                Log.i(TAG, "no of Parents: " + parents.size());

                // add parents to DB
                for (ParentBean p : parents) {
                    ParentDAO daoP = new ParentDAO(context);
                    /*if (daoP.getParent(p.getId()) == null) { //if not exists
                        daoP.addParent(p);
                    }*/
                }

                p1Id = parents.get(0).getId();
                p2Id = parents.get(1).getId();
                if (parents.size() > 2) {
                    gId = parents.get(2).getId();
                }
            } else {
                Log.i(TAG, "no of Parents: null");
            }

            //add messages to DB
            if (messages != null) {
                Log.i(TAG, "no of Messages: " + messages);

                for (MessagesBean m : messages) {
                    MessagesDAO daoM = new MessagesDAO(context);
                    //daoM.addMessage(m);
                }
            } else {
                Log.i(TAG, "no of Messages: null");
            }

            //add tags to DB
            if (tags != null) {
                Log.i(TAG, "no of Tags: " + tags);

                for (TagsBean t : tags) {
                    /*TagsDAO daoTag = new TagsDAO(context);
                    daoTag.addTag(t);*/
                }
            }

            // add student to DB
            /*student.setClassId(schClass.getId());
            student.setParent1Id(p1Id);
            student.setParent2Id(p2Id);
            student.setGuardian_Id(gId);*/
            StudentDAO daoStu = new StudentDAO(context);
            //daoStu.addStudent(student);

            // add fee to DB
            if (fee != null) {
                FeeDAO daoFee = new FeeDAO(context);
                //daoFee.addFee(fee);
            }

            // add transport to DB
            if (transport != null) {
                TransportDAO daoTransport = new TransportDAO(context);
                //daoTransport.addTransport(transport);
            }

            // add class to DB
            schClass.setSchoolId(schId);
            //schClass.setStudentId(student.getId());
            ClassDAO daoCls = new ClassDAO(context);
            /*if (daoCls.getSchoolClass(schClass.getId()) == null) {
                daoCls.addSchoolClass(schClass);
            }*/

        }

        Log.i(TAG, "First time feed has been inserted into DB.");
        SharedPrefUtil.setPrefFirstTimeLaunch(context, false);
        assignColorsToStudents(ff);
        Log.i(TAG, "First time launch steps have been completed.");

        // for testing.
        CommonMethods.takeDbBackup(context);
    }

}
