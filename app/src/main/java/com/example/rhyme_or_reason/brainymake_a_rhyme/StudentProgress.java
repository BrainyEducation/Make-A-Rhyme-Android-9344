package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class StudentProgress extends AppCompatActivity{

    GraphView graph;
    LineGraphSeries<DataPoint> series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        graph = (GraphView) findViewById(R.id.graph);
        series.appendData(new DataPoint(0, 1), true, 100);
        series.appendData(new DataPoint(1, 5), true, 100);
        series.appendData(new DataPoint(2, 3), true, 100);
        series.appendData(new DataPoint(3, 2), true, 100);
        series.appendData(new DataPoint(4, 6), true, 100);
        graph.addSeries(series);
    }
}
