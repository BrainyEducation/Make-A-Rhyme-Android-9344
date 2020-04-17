package com.example.rhyme_or_reason.brainymake_a_rhyme.emailSystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.content.FileProvider;
import android.view.View;

import com.example.rhyme_or_reason.brainymake_a_rhyme.Student;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;


public class EmailSystem
{
    private String[] emails;
    private String subjectLine;
    private String emailBody;

    //This variable determines how long an email can be before being truncated
    public static int maxEmailDisplayLength = 28;

    //When an email is more than maxEmailDisplayLength characters long, the middle of the email is replaced with this string
    private String emailShortener = "... ";

    //The max number of email addresses that can receive the email produced from this class
    //This is to prevent email services from flagging the message as spam
    private static int maxEmails = 20;

    //regex comes from https://howtodoinjava.com/regex/java-regex-validate-email-address/
    private String emailValidationRegex = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    private Pattern emailRegexPattern;
    private Uri attachmentURI = null;


    public EmailSystem() {
        emailRegexPattern = Pattern.compile(emailValidationRegex);
    }

    /**
     * Call this function to register the necessary data for this object to produce an email in the future
     *
     * @param emails an array of email addresses
     * @param subjectLine the subject line of the email
     * @param emailBody the text that goes inside the email
     * @param path pass in data from the saveViewAsPngAndReturnPath method, if this string is an empty string, no pictures will be attached.
     */
    public void setEmailVariables(String[] emails, String subjectLine, String emailBody, String path) {
        this.emails = emails;
        this.subjectLine = subjectLine;
        this.emailBody = emailBody;
        if (!path.equals("")) {
            this.attachmentURI = reformatPathStringToURI(path);
        }
    }

    /**
     * @param path path to an image resource that needs to be turned into its Uri path form
     * @return a URI that contains the path passed in
     */
    private Uri reformatPathStringToURI(String path) {
        String returnString = path.substring(1);
        Uri imageUri = new Uri.Builder().appendPath(returnString).build();
        return imageUri;
    }

    /**
     * This creates an Android Intent that will bring up a menu with whatever email clients
     * the user has on their phone.
     *
     * @param context the Context object that will be launching the email Intent
     * @return an Intent with Email data
     */
    public Intent createEmailIntent(Context context) {
        Intent it = new Intent(Intent.ACTION_SEND_MULTIPLE);
        it.putExtra(Intent.EXTRA_EMAIL, emails);
        it.putExtra(Intent.EXTRA_SUBJECT,subjectLine);
        it.putExtra(Intent.EXTRA_TEXT,emailBody);
        it.setType("image/png");

        it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (attachmentURI == null) {
            return it;
        }


        HashMap<String, String> filenameToPathMap = ImageSaver.getFilenameToPathMap();
        ArrayList<Uri> uris = new ArrayList<>();
        for (String s : filenameToPathMap.keySet()) {
            File f = new File(filenameToPathMap.get(s));
            Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName()+".fileprovider",f);
            uris.add(contentUri);
        }

        it.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        return it;
    }


    /**
     * Checks if the provided email address is a valid email
     * @param email the email address
     * @return whether the email address is valid
     */
    public boolean isValidEmail(String email) {
        return emailRegexPattern.matcher(email).matches();
    }

    /**
     * @param emails an ArrayList of email addresses
     * @return whether the ArrayList has more than the maximum number of emails
     */
    public boolean tooManyEmails(ArrayList<String> emails) {
        return emails.size() > maxEmails;
    }

    /**
     * @param email the email address
     * @return the email address that has been shortened if it exceeds the max email length
     */
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

    /**
     * Takes a view, saves it as a png file, and returns a string representing the path to
     * the file
     * @param parentView the view that will be converted into a PNG
     * @param context the context that the parentView belongs to
     */
    public static String saveViewAsPngAndReturnPath(View parentView, Context context) {
        return ImageSaver.saveImageAndReturnPath(parentView, context, "brainy_image");
    }

    /**
     * Saves an ArrayList of emails into SharedPreferences for persistence into uuid + "storedEmailLists"
     * The list is converted into a set
     * @param emailList
     * @param uuid
     * @param context
     */
    public void saveEmails(ArrayList<String> emailList, String uuid, Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        HashSet<String> emailSet = new HashSet<>();
        for (String s : emailList) {
            emailSet.add(s);
        }

        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();

        prefsEditor.putStringSet(uuid+"storedEmailLists", emailSet);
        prefsEditor.commit();

    }

    /**
     * Retrieves an ArrayList of emails from SharedPreferences for persistence into uuid + "storedEmailLists"
     * @param uuid
     * @param context
     * @return
     */
    public ArrayList<String> retrieveEmails(String uuid, Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        Gson gson = new Gson();


        Set<String> emailSet = appSharedPrefs.getStringSet(uuid+"storedEmailLists", new HashSet<String>());
        ArrayList<String> emailList = new ArrayList<>();

        for (String s : emailSet) {
            emailList.add(s);
        }

        return emailList;
    }

}
