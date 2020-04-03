package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ProgressWordList extends AppCompatActivity {

    ArrayAdapter<String> arrayAdapter;
    String uuid = "";
    Student currStudent;
    String studentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_wordlist);

        uuid = getIntent().getExtras().get("uuid").toString();
        Log.d("message", uuid);
        if (uuid.equals("empty")) {
            studentName = getIntent().getExtras().get("name").toString();
            getStudentFromName();
        } else {
            getStudentFromUUID();
        }
        getStudentFromUUID();
        //currStudent.loadMap(this.getApplicationContext(), uuid);
        Log.d("student", currStudent.getName());
        Log.d("map", currStudent.getAttemptsMap().keySet().toString());
        ArrayList<String> attemptedwords = new ArrayList<String>(currStudent.getAttemptsMap().keySet());
        arrayAdapter = new ArrayAdapter(this, R.layout.progress_individual_words, attemptedwords);
        //SavePref(attemptedwords);

        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(arrayAdapter);
        //LoadFromPref();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String wordFromList = adapterView.getItemAtPosition(i).toString();
                Intent myIntent = new Intent(view.getContext(), StudentProgress.class);
                myIntent.putExtra("word", wordFromList);
                myIntent.putExtra("uuid", uuid);
                startActivityForResult(myIntent, 0);
            }
        });
    }

    public void ClickedBackButton(View view) {
        onBackPressed();
    }

    public void getStudentFromUUID() {
        ArrayList<Student> allStudents = Student.retrieveStudents(this.getApplicationContext());

        for (int index = 0; index < allStudents.size(); ++index) {
            if (allStudents.get(index).getUuid().equals(uuid)) {
                currStudent = allStudents.get(index); // HERE we assign the student so we can save on exit.
                break;
            }
        }
    }

    public void getStudentFromName() {
        ArrayList<Student> allStudents = Student.retrieveStudents(this.getApplicationContext());

        for (int index = 0; index < allStudents.size(); ++index) {
            if (allStudents.get(index).getName().equals(studentName)) {
                currStudent = allStudents.get(index); // HERE we assign the student so we can save on exit.
                break;
            }
        }
    }

}
