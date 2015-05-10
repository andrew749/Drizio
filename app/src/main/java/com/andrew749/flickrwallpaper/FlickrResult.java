package com.andrew749.flickrwallpaper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by andrewcodispoti on 2015-05-09.
 */
public class FlickrResult implements LinkFollowingCallback, ImageDownloadingInterface {
    private String imageName;
    private URL url = null;
    private long id;
    private static String REST_ENDPOINT = "https://api.flickr.com/services/rest/";
    private Bitmap image;

    //simple model for a result containing the image and the url of the image.
    public FlickrResult(String name, long id) {
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

    public String getName() {
        return imageName;
    }

    @Override
    public void doneFollowing(URL url) {
        this.url = url;
        ImageDownloader imageDownloader = new ImageDownloader(this);
        imageDownloader.execute();
    }

    @Override
    public void downloadedImage(Bitmap bm) {
        this.image = bm;
    }

    private class FollowUrl extends AsyncTask<Long, Void, URL> {
        LinkFollowingCallback callback;

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
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
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
        }
    }

}
