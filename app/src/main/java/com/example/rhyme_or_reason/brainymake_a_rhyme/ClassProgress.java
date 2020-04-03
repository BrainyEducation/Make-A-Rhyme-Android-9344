package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

public class ClassProgress extends AppCompatActivity {

    String ptName;
    ParentTeacher currPT;
    BarGraphSeries<DataPoint> series = new BarGraphSeries<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_progress);
        ptName = getIntent().getExtras().get("name").toString();

        ArrayList<ParentTeacher> listOfPTs = ParentTeacher.retrieveParentTeachers(this.getApplicationContext());

        for (int index = 0; index < listOfPTs.size(); ++index) {
            if (listOfPTs.get(index).getName().equals(ptName)) {
                currPT = listOfPTs.get(index);
            }
        }

        ArrayList<Student> students = currPT.getStudents();

        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.setBackgroundColor(getResources().getColor(android.R.color.white));
        String[] studentNames = new String[students.size()];
        for (int i = 0; i < students.size(); i++) {
            series.appendData(new DataPoint(i + 1, students.get(i).getAttemptsMap().size()), true, 50);
            studentNames[i] = students.get(i).getName();
        }
        graph.addSeries(series);
        if (studentNames.length > 1) {
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(studentNames);
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        }

        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series.setSpacing(50);

        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);
        //series.setValuesOnTopSize(50);
    }

    public void ClickedBackButton(View view) {
        onBackPressed();
    }
}
