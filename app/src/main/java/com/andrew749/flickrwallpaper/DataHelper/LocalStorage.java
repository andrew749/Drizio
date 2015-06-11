package com.andrew749.flickrwallpaper.DataHelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.andrew749.flickrwallpaper.MainActivity;

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
    private static LocalStorage storage;
    public LocalStorage(Context context) {
        this.context = context;
        directory=Environment.getExternalStorageDirectory().toString()+"/flickrwallpaper";
        File mydir=new File(directory);
        mydir.mkdirs();
    }
    public static LocalStorage getInstance(Context context){
        if(storage==null){
            storage=new LocalStorage(context);
        }
        return storage;
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

    public boolean writeToExternalStorage(String imageName,Bitmap bm,Context context) {
        if (!isExternalStorageWritable()) return false;
        FileOutputStream os = null;
        File f=new File(directory+"/"+imageName);
        // dont make multiple copies
        if(f.exists())return true;
        //write to an output stream.
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
    public ArrayList<LocalImage> getImages() {
        if (!isExternalStorageReadable()) return null;
        ArrayList<LocalImage> images=new ArrayList<>();
        File dir = new File(directory+"/");
        File file[]=dir.listFiles();
        if(file!=null)
        for(File x: file){
            images.add(new LocalImage(BitmapFactory.decodeFile(x.getPath()),x.getName()));
        }
        return images;
    }
    public ArrayList<String> getImageNames(){
        if (!isExternalStorageReadable()) return null;
        ArrayList<String> images=new ArrayList<>();
        File dir = new File(directory+"/");
        File file[]=dir.listFiles();
        if(file!=null)
            for(File x: file){
                images.add(x.getName());
                if(images.size()==30) break;
            }
        return images;
    }
    public Bitmap getImage(String name){
        File dir = new File(directory+"/"+name);
        if(dir!=null)
            return BitmapFactory.decodeFile(dir.getPath());
        return null;
    }

    public void deleteImages(){
        if(!isExternalStorageReadable()||!isExternalStorageWritable())return;
        File dir = new File(directory+"/");
        File file[]=dir.listFiles();
        if(file!=null){
            for (File f:file){
                f.delete();
            }
        }

    }
    public void deleteImage(String name){
        if(!isExternalStorageReadable()||!isExternalStorageWritable())return;
        File file=new File(directory+"/"+name);
        if(file!=null){
            file.delete();
        }
    }
    public boolean imageExists(String name){
        File file=new File(directory+"/"+name);
        return file.exists();
    }


}
