package com.example.rhyme_or_reason.brainymake_a_rhyme;

import java.util.ArrayList;
import java.util.UUID;

public class Teacher {

    // ArrayList<Student> studentList;
    private ArrayList<String> studentNameList;
    private ArrayList<UUID> studentUUIDList;
    private String password = "Password";

    public Teacher()
    {
        this.studentNameList = new ArrayList<>();
        this.studentUUIDList = new ArrayList<>();
    }

    public void addStudent(String name, UUID identifier) {

        this.studentNameList.add(name);
        this.studentUUIDList.add(identifier);
    }

    public ArrayList<String> getStudentNameList() {
        return studentNameList;
    }

    public ArrayList<UUID> getStudentUUIDList() {
        return studentUUIDList;
    }

    public String getPassword() {
        return password;
    }
}
