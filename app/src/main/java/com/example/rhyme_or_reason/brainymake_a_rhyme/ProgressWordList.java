package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ProgressWordList extends AppCompatActivity {

    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_wordlist);
        ArrayList<String> attemptedwords = new ArrayList<>(MainActivity.attemptsMap.keySet());
        arrayAdapter = new ArrayAdapter(this, R.layout.progress_individual_words, attemptedwords);

        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(arrayAdapter);
        LoadFromPref();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String wordFromList = adapterView.getItemAtPosition(i).toString();
                arrayAdapter.add(wordFromList);
                arrayAdapter.notifyDataSetChanged();
                SavePref("List", wordFromList);
                Intent myIntent = new Intent(view.getContext(), StudentProgress.class);
                myIntent.putExtra("Word", wordFromList);
                startActivityForResult(myIntent, 0);
            }
        });
    }

    protected void SavePref(String str, String val) {
        SharedPreferences data = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = data.edit();
        editor.putString(str, val);
        editor.commit();
    }

    protected void LoadFromPref() {
        SharedPreferences data = PreferenceManager.getDefaultSharedPreferences(this);
        String dataset = data.getString("", "");
        arrayAdapter.add(dataset);
        arrayAdapter.notifyDataSetChanged();
    }

}
