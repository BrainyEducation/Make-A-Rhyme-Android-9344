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
import java.util.HashSet;

public class ImageSaver {

    private static HashMap<String, String> filenameToPathMap = new HashMap<>();

    /**
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
            Log.d("notfound", e.toString());
        } catch (IOException e) {
            Log.d("ioFileOutput",e.toString());
        }
        filenameToPathMap.put(fileName, path);
        Log.d("ATTACHMULTIPLEPUT", path);
        return path;
    }

    public static HashMap<String, String> getFilenameToPathMap() {
        return filenameToPathMap;
    }
}
