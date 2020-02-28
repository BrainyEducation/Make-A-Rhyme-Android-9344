package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.rhyme_or_reason.brainymake_a_rhyme.emailSystem.EmailSystem;

import java.util.ArrayList;
import java.util.HashSet;

public class EmailActivity extends AppCompatActivity implements View.OnClickListener{

    TextInputLayout textInputLayout;
    TableRow tableRowTemplate;
    ArrayList<String> emails;
    EmailSystem emailSystem;
    TableLayout emailTable;
    HashSet<String> emailHashSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        textInputLayout = (TextInputLayout) findViewById(R.id.EmailInput);
        tableRowTemplate  = (TableRow) findViewById(R.id.EmailTableRowTemplate);
        emailTable = (TableLayout) findViewById(R.id.EmailTable);
        emails = new ArrayList<String>();
        emailHashSet = new HashSet<>();
        emailSystem = new EmailSystem();
        tableRowTemplate.setVisibility(View.GONE);
    }


    public void sendEmail(View v) {
        if (emails.size() == 0) {
            displayError("No emails have been added");
            return;
        }
        String[] a = new String[1];
        a[0] = "jqdude@gmail.com";
        String subject = "hi";
        String body = "this is the body text";
        emailSystem.setEmailVariables( emails.toArray(new String[0]), subject, body);
        Intent in = emailSystem.createEmailIntent();
        try {
            startActivity(Intent.createChooser(in, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
           Log.d("Email error", "No email clients installed");
        }
    }

    public void addEmail(View v) {
        final String email = textInputLayout.getEditText().getText().toString();
        if (!emailSystem.isValidEmail(email)) {
            displayError("Invalid email");
            return;
        }
        if (emailSystem.tooManyEmails(emails)) {
            displayError("Max emails exceeded");
            return;
        }
        if (emailHashSet.contains(email)) {
            displayError("Email already exists");
            return;
        }


        emails.add(email);
        emailHashSet.add(email);
        String displayText = emailSystem.shortenedEmailText(email);

        TextView exampleEmailTextView = (TextView) tableRowTemplate.getChildAt(0);
        TextView exampleEmailRemovalButton = (TextView) tableRowTemplate.getChildAt(1);

        TableRow newEmailRow = new TableRow(this);
        TextView emailTextView = new TextView(this);
        Button emailRemovalButton = new Button(this);
        emailTextView.setLayoutParams(exampleEmailTextView.getLayoutParams());
        emailRemovalButton.setLayoutParams(exampleEmailRemovalButton.getLayoutParams());

        emailTextView.setText(displayText);
        emailRemovalButton.setText(exampleEmailRemovalButton.getText());
        emailRemovalButton.setOnClickListener(generateClickListener(email));

        newEmailRow.addView(emailTextView);
        newEmailRow.addView(emailRemovalButton);
        emailTable.addView(newEmailRow);

    }

    private void displayError(String s) {
        Snackbar.make(emailTable, s, Snackbar.LENGTH_LONG).show();
    }

    private View.OnClickListener generateClickListener(final String email) {
        View.OnClickListener  returnListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int rowNum = emails.indexOf(email);
                emails.remove(rowNum);
                emailTable.removeViewAt(rowNum+1);
                emailHashSet.remove(email);
            }
        };
        return returnListener;
    }
    
    @Override
    public void onClick(View v) {

    }
}
