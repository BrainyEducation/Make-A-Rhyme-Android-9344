package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

    byte[] illustration;
    ArrayList<String> chosenWords;
    String storyText = "";
    String textForEmail = "";
    boolean paused = false;

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

        loadIntentsAndViews();

        stitchStoryText();

    }

    public void loadIntentsAndViews()
    {
        chosenWords =  getIntent().getStringArrayListExtra("rhyme_words");
        illustration = getIntent().getByteArrayExtra("illustration");
        storyText = getIntent().getStringExtra("general_rhyme_text");
    }

    public void stitchStoryText()
    {
        boolean inSquareBrackets = false;

        int blankIndex = 0;

        for (int index = 0; index < storyText.length(); ++index) {
            if (!inSquareBrackets && storyText.charAt(index) != '[') {
                textForEmail += storyText.charAt(index);
            } else if (storyText.charAt(index) == '[') {
                inSquareBrackets = true;
                String chosenWord = chosenWords.get(blankIndex);
                textForEmail += (chosenWord.length() != 0) ? chosenWord : "___";
                ++blankIndex;
            } else if (storyText.charAt(index) != ']' && inSquareBrackets) {
                // Do nothing
            } else if (storyText.charAt(index) == ']') {
                inSquareBrackets = false;
            }
        }

        // Get rid of potential double spaces.
        textForEmail = textForEmail.replace("  ", " ");
    }

    public void sendEmail(View v) {
        if (emails.size() == 0) {
            displayError("No emails have been added");
            return;
        }
        String[] a = new String[1];
        a[0] = "jqdude@gmail.com";
        String subject = "This is your child's recent progress in this story!";
        ArrayList<String> workingBodyText = (ArrayList<String>) getIntent().getExtras().get("completedWords");
        StringBuilder sb = new StringBuilder();
        sb.append("You child has learned the following words:\n");
        for (String s : workingBodyText) {
            sb.append(s);
            if (s.length() == 0) {
                continue;
            }
            sb.append("\n");
        }
        sb.append("\nAttached is an image of your child's work for you to print out.");
        String body = sb.toString();

        String path = (String) getIntent().getExtras().get("imageURI");

        emailSystem.setEmailVariables(emails.toArray(new String[0]), subject, body, path);

        Intent in = emailSystem.createEmailIntent(this);
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
        emailRemovalButton.setOnClickListener(generateRemoveEmailListener(email));

        newEmailRow.addView(emailTextView);
        newEmailRow.addView(emailRemovalButton);
        emailTable.addView(newEmailRow);
    }

    private void displayError(String s) {
        Snackbar.make(emailTable, s, Snackbar.LENGTH_LONG).show();
    }

    private View.OnClickListener generateRemoveEmailListener(final String email) {
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

    /**
     * When paused to switch to email, keep track of this so that will return to rhyme screen when unpaused
     */
    @Override
    public void onPause()
    {
        super.onPause();

        paused = true;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (paused) {
            ClickedBackButton(null);
        }
    }

    /**
     * Returns to the rhyme screen
     */
    public void ClickedBackButton(View view) {

        onBackPressed();
    }
}
