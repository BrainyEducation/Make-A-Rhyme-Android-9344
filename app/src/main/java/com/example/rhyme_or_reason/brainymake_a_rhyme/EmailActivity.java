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
import com.example.rhyme_or_reason.brainymake_a_rhyme.emailSystem.ImageSaver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class EmailActivity extends AppCompatActivity {

    TextInputLayout textInputLayout;
    TableRow tableRowTemplate;
    ArrayList<String> emails;
    EmailSystem emailSystem;
    TableLayout emailTable;
    HashSet<String> emailHashSet;

    byte[] illustration;
    ArrayList<String> chosenWords;
    String storyText = "";
    String subjectText = "";
    String textForEmail = "";
    boolean paused = false;
    String uuid = "";

    Button submitButton;
    TextView enteredEmailAddresses;

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
        uuid = getIntent().getExtras().getString("uuid");
        emails = emailSystem.retrieveEmails(uuid, this);

        loadIntentsAndViews();

        stitchStoryText();
    }

    /**
     * Loads this activity with the necessary data to produce the email intent from whatever
     * activity preceded this one
     */
    public void loadIntentsAndViews()
    {
        chosenWords =  getIntent().getStringArrayListExtra("rhyme_words");
        illustration = getIntent().getByteArrayExtra("illustration");
        storyText = getIntent().getStringExtra("general_rhyme_text");
        subjectText = getIntent().getStringExtra("subject_line");
        submitButton = (Button) findViewById(R.id.SubmitButton);
        enteredEmailAddresses = findViewById(R.id.EmailAddressesLabel);
        submitButton.setAlpha(0.0f);
        enteredEmailAddresses.setAlpha(0.0f);

        for (String s : emails) {
            addEmailHelper(s);
            submitButton.setAlpha(1.0f); // Allow button to be clicked if emails are in this list
            enteredEmailAddresses.setAlpha(1.0f);
        }
    }

    /**
     * Takes the raw story text (from Rhyme.java) and  combines it with the words that the user has selected
     * and places it into the textForEmail instance variable
     */
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

    /**
     * Hitting the email button triggers this function that opens up the messaging client selection menu
     * supplied by the Android OS
     * @param v
     */
    public void sendEmail(View v) {
        if (emails.size() == 0) {
            displayError("No emails have been added");
            return;
        }
        String[] a = new String[1];
        a[0] = "jqdude@gmail.com";
        String subject = subjectText;
        String body = textForEmail;
        String path = (String) getIntent().getExtras().get("imageUri");

        emailSystem.setEmailVariables( emails.toArray(new String[0]), subject, body, path);

        Intent in = emailSystem.createEmailIntent(this);

        try {
            startActivity(Intent.createChooser(in, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
           Log.d("Email error", "No email clients installed");
        }
    }

    /**
     * Adds an email to the UI and updates emailSystem with a list of the added emails
     * @param v
     */
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

        submitButton.setAlpha(1.0f); // Allow button to be clicked after a button is added
        enteredEmailAddresses.setAlpha(1.0f);
        textInputLayout.getEditText().setText(""); // Reset the text field for another entry
        emails.add(email);
        emailHashSet.add(email);
        addEmailHelper(email);

        emailSystem.saveEmails(emails, uuid, this);
    }

    /**
     * Does the necessary work to add emails only to the UI
     * @param email
     */
    private void addEmailHelper(String email) {
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
        emailRemovalButton.setOnClickListener(generateRemoveEmailListener(email, this));

        newEmailRow.addView(emailTextView);
        newEmailRow.addView(emailRemovalButton);
        emailTable.addView(newEmailRow);
    }

    /**
     * Displays the error as a Snackbar activity
     * @param errorMessage
     */
    private void displayError(String errorMessage) {
        Snackbar.make(emailTable, errorMessage, Snackbar.LENGTH_LONG).show();
    }

    /**
     * When an email is added, this function provides the event listener that allows the user to remove
     * an added email
     * @param email the email address to remove
     * @return
     */
    private View.OnClickListener generateRemoveEmailListener(final String email, final Context parentContext) {
        View.OnClickListener  returnListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int rowNum = emails.indexOf(email);
                emails.remove(rowNum);
                emailTable.removeViewAt(rowNum+1);
                emailHashSet.remove(email);

                if (emails.size() == 0) {
                    submitButton.setAlpha(0.0f);
                    enteredEmailAddresses.setAlpha(0.0f);
                }
                emailSystem.saveEmails(emails, uuid, parentContext);
            }
        };
        return returnListener;
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
        ImageSaver.clearHashmap();

        onBackPressed();
    }

}
