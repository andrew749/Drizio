package com.andrew749.flickrwallpaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

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
    private final String directory;
    public LocalStorage(Context context) {
        this.context = context;
        directory=Environment.getExternalStorageDirectory().toString()+"/flickrwallpaper";
        File mydir=new File(directory);
        mydir.mkdirs();
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

    public boolean writeToExternalStorage(Bitmap bm,Context context) {
        if (!isExternalStorageWritable()) return false;
        FileOutputStream os = null;
        //for pictures with spaces in the name.
        File f=new File(directory+"/"+System.currentTimeMillis());
        try {
            os = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, os);
        } catch (FileNotFoundException e) {
            Log.d(MainActivity.TAG,"Failed to write to file");
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
        File dir = new File(directory+"/");
        File file[]=dir.listFiles();
        if(file!=null)
        for(File x: file){
            images.add(BitmapFactory.decodeFile(x.getPath()));
        }
        return images;
    }

}
