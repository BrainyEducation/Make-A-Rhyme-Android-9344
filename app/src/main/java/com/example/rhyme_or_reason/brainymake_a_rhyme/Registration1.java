package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Registration1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration1);
    }

    /**
     * Launches the student registration UI
     */
    public void ClickedRegisterStudent(View v)
    {
        Intent newIntent = new Intent(this, StudentRegistration.class);
        startActivityForResult(newIntent, 1);
    }

    /**
     * Launches the student registration UI
     */
    public void ClickedRegisterParentTeacher(View v)
    {
        Intent newIntent = new Intent(this, ParentTeacherRegistration.class);
        startActivityForResult(newIntent, 1);
    }

    public void ClickedBackButton(View v) {
        onBackPressed();
    }

    /**
     * When someone hits the back arrow from one of the following screens after making a new account,
     * shouldn't be taken back to this screen, so automatically pop backwards to the application welcome.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {

        finish();
        //System.out.println("Reg1: On Activity Result.");
        //onBackPressed();
    }
}
