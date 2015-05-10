package com.andrew749.flickrwallpaper;

import android.graphics.Bitmap;

/**
 * Created by andrewcodispoti on 2015-05-09.
 */
public interface ImageDownloadingInterface {
    //a callback method to indicate if done downloading
    //returns null if error and true if image is downloaded
    boolean downloadedImage(Bitmap bm);
}
