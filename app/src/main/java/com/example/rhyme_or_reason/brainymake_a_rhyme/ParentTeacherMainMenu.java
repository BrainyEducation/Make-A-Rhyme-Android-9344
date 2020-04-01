package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ParentTeacherMainMenu extends AppCompatActivity {

    ArrayAdapter<String> arrayAdapter;
    Teacher currTeacher;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_teacher_main_menu);

        loadIntentsAndViews();

        currTeacher = new Teacher();

        //ArrayList<String> studentNames = currTeacher.getStudentNameList();
        ArrayList<String> studentNames = new ArrayList<>();
        studentNames.add(username); // Just for testing
        arrayAdapter = new ArrayAdapter(this, R.layout.progress_individual_words, studentNames);
        //SavePref(attemptedwords);

        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(arrayAdapter);
        //LoadFromPref();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String studentName = adapterView.getItemAtPosition(i).toString();
                // Link to Eric's analytics screen for that student
                /*
                Intent myIntent = new Intent(view.getContext(), StudentProgress.class);
                myIntent.putExtra("Word", wordFromList);
                startActivityForResult(myIntent, 0);
                */
            }
        });
    }

    public void ClickedAddStudent(View v) {
        Intent newIntent = new Intent(this, AddStudent.class);
        startActivityForResult(newIntent, 1);
    }

    public void ClickedBackButton(View v) {
        onBackPressed();
    }

    public void ClickedClassAnalytics(View v) {
        // Link to Eric's page with averaged stats
    }

    public void loadIntentsAndViews() {
        username = (String) getIntent().getStringExtra("username");
    }
}
