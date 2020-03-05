package com.example.rhyme_or_reason.brainymake_a_rhyme.emailSystem;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EmailSystem
{
    private String[] emails;
    private String subjectLine;
    private String emailBody;
    public static int maxEmailDisplayLength = 28;
    //regex comes from https://howtodoinjava.com/regex/java-regex-validate-email-address/
    private String emailValidationRegex = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    private Pattern emailRegexPattern;
    private static int maxEmails = 20;
    private String emailShortener = "... ";

    public EmailSystem() {
        emailRegexPattern = Pattern.compile(emailValidationRegex);
    }

    public void setEmailVariables(String[] emails, String subjectLine, String emailBody) {
        this.emails = emails;
        this.subjectLine = subjectLine;
        this.emailBody = emailBody;
    }

    public Intent createEmailIntent() {
        Intent it = new Intent(Intent.ACTION_SEND);
        it.putExtra(Intent.EXTRA_EMAIL, emails);
        it.putExtra(Intent.EXTRA_SUBJECT,subjectLine);
        it.putExtra(Intent.EXTRA_TEXT,emailBody);
        it.setType("message/rfc822");

        //attach image
        String root = Environment.getExternalStorageDirectory().toString();
        String path = root + "/saved_images" + "Brainy-make-a-rhyme-temp.jpg";
        Uri thing = Uri.fromFile(new File(path));
        it.putExtra(Intent.EXTRA_STREAM, thing);

        return it;
    }


    public boolean isValidEmail(String email) {
        return emailRegexPattern.matcher(email).matches();
    }

    public boolean tooManyEmails(ArrayList<String> emails) {
        return emails.size() > maxEmails;
    }

    public String shortenedEmailText(String email) {
        if (email.length() >= maxEmailDisplayLength) {
            int atSymbolIndex = email.indexOf("@");
            String domainName = email.substring(atSymbolIndex);
            int usedUpCharacters = domainName.length();
            int remainingCharacters = maxEmailDisplayLength - usedUpCharacters;
            String emailPrefix = email.substring(0, remainingCharacters - emailShortener.length());
            StringBuilder sb = new StringBuilder();
            sb.append(emailPrefix);
            sb.append(emailShortener);
            sb.append(domainName);
            return sb.toString();
        }
        return email;
    }
}
