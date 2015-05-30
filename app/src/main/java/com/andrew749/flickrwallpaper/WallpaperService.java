package com.andrew749.flickrwallpaper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.preference.Preference;
import android.util.Log;
import android.view.SurfaceHolder;

import com.andrew749.flickrwallpaper.DataHelper.LocalStorage;
import com.andrew749.flickrwallpaper.FlickrHelper.FlickrResult;
import com.andrew749.flickrwallpaper.Fragments.SettingsFragment;
import com.andrew749.flickrwallpaper.Interfaces.ImageDownloadingInterface;
import com.andrew749.flickrwallpaper.Interfaces.ListDownloadingInterface;

import java.util.ArrayList;

/**
 * Created by andrewcodispoti on 2015-05-10.
 */
public class WallpaperService extends android.service.wallpaper.WallpaperService implements SharedPreferences.OnSharedPreferenceChangeListener {
    private Paint paint = new Paint();
    private int interval=1;
    private int cacheSize=100;
    private String imageSize="Large";
    @Override
    public Engine onCreateEngine() {
        updateProperties();
        getSharedPreferences(SettingsFragment.prefsName,Context.MODE_PRIVATE).registerOnSharedPreferenceChangeListener(this);
        return new PhotoEngine();
    }
    private void updateProperties(){
        //get preferences
        cacheSize=getSharedPreferences(SettingsFragment.prefsName, Context.MODE_PRIVATE).getInt(SettingsFragment.cacheName,100);
        interval=getSharedPreferences(SettingsFragment.prefsName, Context.MODE_PRIVATE).getInt(SettingsFragment.refreshName, 100);
        imageSize=getSharedPreferences(SettingsFragment.prefsName, Context.MODE_PRIVATE).getString(SettingsFragment.imageName, "Large");
    }
    //easier just to update all rather than having to do string comparison
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updateProperties();
    }

    /**
     * Subclass that handles downloading of images.
     */
    class PhotoEngine extends Engine implements ImageDownloadingInterface, ListDownloadingInterface {
        private final Handler handler = new Handler();
        ArrayList<Bitmap> images = new ArrayList<Bitmap>();
        Bitmap currentImage;
        Bitmap oldImage;
        LocalStorage storage;
        int index = 0;
        private boolean visible = true;


        private final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (index > images.size()) index = 0;
                //gets the next image
                oldImage = currentImage;
                if (images.size() > 2)
                    currentImage = images.get(index++);
                draw(currentImage);
                if (images.size() - index < 2) {
                    //get more images
                    int tempsize = images.size();
                    images.addAll(storage.getImages());
                    if (images.size() - tempsize < 5) index = 0;
                }
            }
        };

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            paint.setColor(Color.BLUE);
            LocalStorage storage = new LocalStorage(getApplicationContext());
            images = storage.getImages();
//            FlickrSearcher searcher=new FlickrSearcher(this,getApplicationContext());
//            searcher.getImagesSync();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;
            // if screen wallpaper is visible then draw the image otherwise do not draw
            if (visible) {
                handler.postDelayed(runnable, 1000);
            } else {
                handler.removeCallbacks(runnable);
            }
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            this.visible = false;
            handler.removeCallbacks(runnable);
        }

        //draws the supplied image to the canvas
        void draw(Bitmap bm) {
            final SurfaceHolder holder = getSurfaceHolder();
            Canvas c = holder.lockCanvas();
            try {
                // clear the canvas
                int canvasWidth = c.getWidth(), canvasHeight = c.getHeight();
                float scaleFactor = canvasHeight / bm.getHeight();
                int scaledWidth = (int) (bm.getWidth() * scaleFactor);
                int scaledHeight = (int) (bm.getHeight() * scaleFactor);
                Bitmap fill;
                if (scaledWidth > c.getWidth())
                    fill = Bitmap.createScaledBitmap(bm, scaledWidth, canvasHeight, false);
                else {
                    fill = Bitmap.createScaledBitmap(bm, canvasWidth, canvasHeight, false);
                }
                currentImage = fill;
            } finally {
                holder.unlockCanvasAndPost(c);
                animatePicture(holder);
            }
            handler.removeCallbacks(runnable);
            if (visible) {
                handler.postDelayed(runnable, interval*1000*60);
            }

        }

        private void animatePicture(SurfaceHolder holder) {
            int width = 1;
            for (int x = 0; x < width; x += (int) width / 60) {
                Canvas c = holder.lockCanvas();
                width = c.getWidth();
                if (c != null) {
                    synchronized (holder) {
                        if (oldImage != null) {
                            c.drawBitmap(oldImage, -x, 0, paint);
                        }
                        if (c.getWidth() - x > (width/60))
                            c.drawBitmap(currentImage, c.getWidth() - x, 0, paint);
                        else
                            c.drawBitmap(currentImage, 0, 0, paint);
                    }

                    holder.unlockCanvasAndPost(c);
                }
            }

        }

        @Override
        public boolean imageListIsDoneLoading(ArrayList<FlickrResult> result) {
            for (FlickrResult x : result) {
                x.getImageRequestandWait(this);
            }
            return true;
        }

        @Override
        public void downloadedImage(Bitmap bm) {
            images.add(bm);
            storage.writeToExternalStorage(bm, getApplicationContext());
        }
    }
}
