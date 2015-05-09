package com.andrew749.flickrwallpaper;

import java.net.URL;

/**
 * Created by andrewcodispoti on 2015-05-09.
 */
public class FlickrResult {
    public String imageName;
    public URL url;

    //simple model for a result containing the image and the url of the image.
    public FlickrResult(String name,URL url){
        this.imageName=name;
        this.url=url;
    }

}
