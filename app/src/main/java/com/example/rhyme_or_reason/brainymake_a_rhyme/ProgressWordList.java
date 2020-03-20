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
import android.widget.Button;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ProgressWordList extends AppCompatActivity {

    ArrayAdapter<String> arrayAdapter;
    Button overallProgBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_wordlist);
        overallProgBtn = findViewById(R.id.OverallProgressButton);
        overallProgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), TotalProgress.class);
                startActivityForResult(myIntent, 0);
            }
        });

        ArrayList<String> attemptedwords = new ArrayList<>(MainMenu.attemptsMap.keySet());
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
                myIntent.putExtra("Word", wordFromList);
                startActivityForResult(myIntent, 0);
            }
        });
    }

    public void ClickedBackButton(View view) {
        onBackPressed();
    }

}
