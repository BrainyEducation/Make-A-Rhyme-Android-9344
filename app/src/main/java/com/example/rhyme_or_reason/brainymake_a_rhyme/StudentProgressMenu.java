package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class StudentProgressMenu extends AppCompatActivity {

    String uuid;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_progress_menu);

        loadIntentsAndViews();
    }

    /**
     * Launches the parent's view of a student's created rhymes
     */
    public void ClickedCreatedRhymes(View v)
    {
        Intent myIntent = new Intent(v.getContext(), ParentTeacherCreatedRhymes.class);
        myIntent.putExtra("name", name);
        myIntent.putExtra("uuid", uuid);
        startActivityForResult(myIntent, 0);
    }

    public void ClickedQuizAnalytics(View v)
    {
        Intent myIntent = new Intent(v.getContext(), ProgressWordList.class);
        myIntent.putExtra("name", name);
        myIntent.putExtra("uuid", uuid);
        startActivityForResult(myIntent, 0);
    }

    /**
     * Creates the buttons that serve as options for the word being spoken
     */
    public void loadIntentsAndViews() {
        uuid = getIntent().getExtras().get("uuid").toString();
        name = getIntent().getExtras().get("name").toString();
    }

    public void ClickedBackButton(View view) {
        onBackPressed();
    }
}
