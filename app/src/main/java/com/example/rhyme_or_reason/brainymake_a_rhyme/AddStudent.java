package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AddStudent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
    }

    public void ClickedBackButton(View v) {
        onBackPressed();
    }

    public void ClickedAddByUUID(View v)
    {

        onBackPressed();
    }

}
