package com.andrew749.flickrwallpaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by andrewcodispoti on 2015-05-10.
 */
public class LocalStorage {
    Context context;

    public LocalStorage(Context context) {
        this.context = context;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
    
    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public boolean writeToExternalStorage(Bitmap bm) {
        if (!isExternalStorageWritable()) return false;
        File directory = new File(String.valueOf(context.getExternalFilesDir("DIRECTORY_PICTURES")));
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(directory);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, os);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (os != null)
                    os.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
    //read image files from Pictures Directory.
    public ArrayList<Bitmap> getImages() {
        if (!isExternalStorageReadable()) return null;
        ArrayList<Bitmap> images = new ArrayList<Bitmap>();
        File dir = context.getDir("Pictures", Context.MODE_PRIVATE);
        File file[]=dir.listFiles();
        for(File x: file){
            images.add(BitmapFactory.decodeFile(x.getPath()));
        }
        return images;
    }

}
