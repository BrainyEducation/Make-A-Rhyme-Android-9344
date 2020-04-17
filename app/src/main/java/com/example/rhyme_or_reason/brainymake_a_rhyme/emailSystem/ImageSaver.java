package com.example.rhyme_or_reason.brainymake_a_rhyme.emailSystem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class ImageSaver {

    private static HashMap<String, String> filenameToPathMap = new HashMap<>();

    /**
     * This function saves an image to the Android device. The filename and the path to that file is saved
     * in an internal Hashmap
     *
     * @param parentView an Android view to be rasterized into a png image
     * @param context the Android Context object that the view belongs to
     * @param fileName name of the file, legal characters are lowercase letters and underscores. Cannot have file extension as the name
     * @return
     */
    public static String saveImageAndReturnPath(View parentView, Context context, String fileName) {
        Bitmap bm = Bitmap.createBitmap(parentView.getWidth(), parentView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        parentView.draw(canvas);

        String path = "";
        try {
            File imagePath = new File(context.getFilesDir(),"images");
            imagePath.mkdir();
            File imageFile = new File(imagePath.getPath(), fileName);
            path = imageFile.getPath();
            FileOutputStream out = new FileOutputStream(imageFile);
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            Log.d("FileNotFoundException", e.toString());
        } catch (IOException e) {
            Log.d("IOException",e.toString());
        }
        filenameToPathMap.put(fileName, path);
        return path;
    }

    /**
     * @return a HashMap that maps a filename to the file path to that resource
     */
    public static HashMap<String, String> getFilenameToPathMap() {
        return filenameToPathMap;
    }

    /**
     * This function clears out the internal filename to file path HashMap
     */
    public static void clearHashmap() {
        getFilenameToPathMap().clear();
    }
}
