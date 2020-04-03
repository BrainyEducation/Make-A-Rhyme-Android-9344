package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.rhyme_or_reason.brainymake_a_rhyme.emailSystem.EmailSystem;
import com.example.rhyme_or_reason.brainymake_a_rhyme.emailSystem.ImageSaver;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentProgress extends AppCompatActivity {

    GraphView graph;
    LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
    Button emailButton;
    String uuid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        graph = (GraphView) findViewById(R.id.graph);
        String w = getIntent().getExtras().get("Word").toString();
        ArrayList<int[]> innerMap = MainActivity.attemptsMap.get(w);
        for (int[] eachAttempt : innerMap) {
            series.appendData(new DataPoint(eachAttempt[0], eachAttempt[1]), true, 50);
        }
        series.setDrawDataPoints(true);
        graph.addSeries(series);

        graph.getViewport().setMinX(1);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxX(5);
        graph.getViewport().setMaxY(10);
        graph.getGridLabelRenderer().setVerticalAxisTitle("Incorrect Selections");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Attempts");
//            graph.getGridLabelRenderer().setNumHorizontalLabels(5);
//            graph.getGridLabelRenderer().setNumVerticalLabels(10);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);

        emailButton = (Button) findViewById(R.id.emailButtonAnalytics);
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEmailClick(v);
            }
        });
    }

    public void ClickedBackButton(View view) {
        ImageSaver.saveImageAndReturnPath(graph, this, getIntent().getExtras().get("Word").toString());
        onBackPressed();
    }

    public void onEmailClick(View v) {

        uuid = getIntent().getExtras().get("uuid").toString();
        //forceStopAudio();

        Intent newIntent = new Intent(StudentProgress.this,EmailActivity.class);
        View parentView = findViewById(R.id.graph);
        String path = EmailSystem.saveViewAsPngAndReturnPath(parentView, this);


        //newIntent.putExtra("current_rhyme", currRhyme);
        //newIntent.putStringArrayListExtra("rhyme_words", wordList);
        //newIntent.putExtra("illustration", byteArray);
        newIntent.putExtra("imageUri", path);
        newIntent.putExtra("general_rhyme_text", "Here is the student's progress with this word");
        newIntent.putExtra("uuid", uuid);

        startActivity(newIntent);
    }
}
