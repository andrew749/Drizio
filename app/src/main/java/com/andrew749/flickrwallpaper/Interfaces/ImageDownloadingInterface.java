package com.andrew749.flickrwallpaper.Interfaces;

import android.graphics.Bitmap;

/**
 * Created by andrewcodispoti on 2015-05-09.
 */
public interface ImageDownloadingInterface {
    //a callback method to indicate if done downloading
    //returns null if error and true if image is downloaded
    void downloadedImage(Bitmap bm,String name);
}
