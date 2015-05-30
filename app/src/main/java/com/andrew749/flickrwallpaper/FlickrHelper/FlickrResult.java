package com.andrew749.flickrwallpaper.FlickrHelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.andrew749.flickrwallpaper.Interfaces.ImageDownloadingInterface;
import com.andrew749.flickrwallpaper.Interfaces.LinkFollowingCallback;
import com.andrew749.flickrwallpaper.DataHelper.LocalStorage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by andrewcodispoti on 2015-05-09.
 */
public class FlickrResult implements LinkFollowingCallback, ImageDownloadingInterface {
    private static Bitmap fallbackBitmap;
    private static String REST_ENDPOINT = "https://api.flickr.com/services/rest/";
    ImageView iv;
    ImageDownloadingInterface imageDownloadingInterface;
    private String imageName;
    private URL url = null;
    private long id;
    private Bitmap image=null;
    private ImageDownloader imageDownloader=null;
    private Context context;
    //simple model for a result containing the image and the url of the image.
    public FlickrResult(String name, long id, Context context) {
        this.context=context;
        this.imageName = name;
        this.id = id;
        FollowUrl linkFollower = new FollowUrl(this);
        linkFollower.execute(id);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public URL getUrl() {
        return url;
    }
    public Bitmap getImage(){
        return this.image;
    }

    public void getAndSetImage(ImageView iv){
        this.iv=iv;
        if(this.image!=null){
            iv.setImageBitmap(this.image);
        }
    }
    public void getImageRequestandWait(ImageDownloadingInterface imageDownloadingInterface){
        if(this.image==null){
            try {
                this.image=this.imageDownloader.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        imageDownloadingInterface.downloadedImage(this.image);

    }
    public String getName() {
        return imageName;
    }

    @Override
    public void doneFollowing(URL url) {
        this.url = url;
        imageDownloader=new ImageDownloader(this);
        imageDownloader.execute(url);
    }

    @Override
    public void downloadedImage(Bitmap bm) {
        this.image = bm;
        LocalStorage storage = new LocalStorage(context);
        storage.writeToExternalStorage(bm,context);

    }

    private class FollowUrl extends AsyncTask<Long, Void, URL> {
        LinkFollowingCallback callback;

        public FollowUrl(LinkFollowingCallback callback) {
            this.callback = callback;
        }

        @Override
        protected URL doInBackground(Long... longs) {

            String queryParameter = "?method=flickr.photos.getSizes&api_key=6c30fdb8388402770932f08d6e367939&format=json&nojsoncallback=1&photo_id=" + longs[0];
            Gson gson = new GsonBuilder().create();
            URL largestImage = null;
            try {
                String json = FlickrSearcher.readUrl(new URL(REST_ENDPOINT + queryParameter));
                SizesParent sizesParent = gson.fromJson(json, SizesParent.class);
                SizesResult sizesResult = sizesParent.sizes;
                List<Size> sizes = sizesResult.size;
                for (Size size : sizes) {
                    if(size.label.equals("Large"))
                        largestImage = new URL(size.source);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return largestImage;
        }

        @Override
        protected void onPostExecute(URL url) {
            super.onPostExecute(url);
            callback.doneFollowing(url);

        }

        private class SizesParent {
            SizesResult sizes;
        }

        private class SizesResult {
            List<Size> size;
        }

        public class Size {
            String label;
            int height, width;
            String source;
        }
    }

    private class ImageDownloader extends AsyncTask<URL, Integer, Bitmap> {
        ImageDownloadingInterface callback;
        public ImageDownloader(ImageDownloadingInterface callback) {
            this.callback = callback;
        }
        @Override
        protected Bitmap doInBackground(URL... urls) {
            Bitmap result = null;
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) urls[0].openConnection();
                InputStream is = httpURLConnection.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                result = BitmapFactory.decodeStream(bis);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            callback.downloadedImage(bitmap);
            if(iv!=null){
                iv.setImageBitmap(bitmap);
            }
        }
    }

}
