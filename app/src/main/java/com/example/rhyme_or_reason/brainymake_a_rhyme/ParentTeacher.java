package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Class designed to house all attributes about a Parent or a Teacher. Parents/Teachers do not have
 * UUIDs; however, their usernames (name) must be unique. All parents and teachers will have a password,
 * and students can be associated with the account.
 */
public class ParentTeacher implements Serializable {

    private String name;
    private String password;
    private ArrayList<Student> addedStudents;

    public ParentTeacher(String name, String password)
    {
        this.name = name;
        this.password = password;
        this.addedStudents = new ArrayList<Student>();
    }

    public void addStudent(Student newStudent)
    {
        addedStudents.add(newStudent);
    }

    public String getPassword()
    {
        return password;
    }

    public String getName()
    {
        return name;
    }

    public ArrayList<Student> getStudents()
    {
        return addedStudents;
    }

    public void saveParentTeacher(Context context)
    {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        Set<String> currUsernames = appSharedPrefs.getStringSet("Usernames", new HashSet<String>());

        if (!currUsernames.contains(name)) {
            currUsernames.add(name); // Add if a new student
        }

        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();

        prefsEditor.putStringSet("Usernames", currUsernames); // UUIDs list with the new UUID appended

        String json = gson.toJson(this);
        prefsEditor.putString(name, json);
        prefsEditor.commit();
    }

    public static ArrayList<ParentTeacher> retrieveParentTeachers(Context context) {

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        Gson gson = new Gson();

        Set<String> currUsernames = appSharedPrefs.getStringSet("Usernames", new HashSet<String>());

        String[] currUsernamesArray = currUsernames.toArray(new String[currUsernames.size()]);

        ArrayList<ParentTeacher> toReturn = new ArrayList<>();

        // Checking against the size of the set but checking against array element)
        for (int index = 0; index < currUsernames.size(); ++index) {
            String tempParentTeacherJson = appSharedPrefs.getString(currUsernamesArray[index], "");
            toReturn.add(gson.fromJson(tempParentTeacherJson, ParentTeacher.class));
        }

        return toReturn;

        //String json = appSharedPrefs.getString("SavedRhyme" + rhymeNumString, "");
        //return gson.fromJson(json, RhymeTemplate.class);
    }

    public static Set<String> getUsernames(Context context)
    {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        Gson gson = new Gson();

        Set<String> usernames = appSharedPrefs.getStringSet("Usernames", new HashSet<String>());

        return usernames;
    }

}