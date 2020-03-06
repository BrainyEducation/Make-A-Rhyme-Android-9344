package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ProgressWordList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_wordlist);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.progress_individual_words, MainActivity.wordsAttempted);

        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(arrayAdapter);

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

}
