package com.andrew749.flickrwallpaper;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URL;

/**
 * Created by andrewcodispoti on 2015-05-09.
 */
public class FlickrResult implements LinkFollowingCallback{
    private String imageName;
    private URL url=null;
    private long id;
    //simple model for a result containing the image and the url of the image.
    public FlickrResult(String name,long id){
        this.imageName=name;
        FollowUrl linkFollower=new FollowUrl(this);
        linkFollower.execute(id);
    }
    public long getId(){
        return id;
    }
    public void setId(long id){
        this.id=id;
    }

    public URL getUrl(){
        return  url;
    }

    public String getName(){
        return  imageName;
    }

    @Override
    public void doneFollowing(URL url) {
        this.url=url;
    }

    private class FollowUrl extends AsyncTask<Long, Void,URL>{
        LinkFollowingCallback callback;
        public FollowUrl(LinkFollowingCallback callback) {
            this.callback=callback;
        }

        @Override
        protected URL doInBackground(Long... longs) {
            Gson gson=new GsonBuilder().create();
            return null;
        }

        @Override
        protected void onPostExecute(URL url) {
            super.onPostExecute(url);
            callback.doneFollowing(url);
        }
    }

}
