package com.andrew749.flickrwallpaper.Interfaces;

import com.andrew749.flickrwallpaper.FlickrHelper.FlickrResult;

import java.util.ArrayList;

/**
 * Created by andrewcodispoti on 2015-05-10.
 */
public interface ListDownloadingInterface {
     boolean imageListIsDoneLoading(ArrayList<FlickrResult> result);
}
