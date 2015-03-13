package com.ptapp.bo;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores list of Student(kid) contexts.
 */
public class StudentsBO {

    // Let's use HashMap: It's NOT thread safe but better performance wise.
    // As long as we are not using multi-threading, we're good
    private Map<Integer, StudentBO> students = new HashMap<Integer, StudentBO>();

    public StudentsBO() {

    }

    // Returns complete list as HashMap
    public Map<Integer, StudentBO> getStudentsAll() {
        return students;
    }

    /**
     * Returns specific instance
     */
    public StudentBO getStudent(int kidStudentId) {
        return students.get(kidStudentId);
    }

    public void setStudentsAll(Map<Integer, StudentBO> students) {
        this.students = students;
    }

}
