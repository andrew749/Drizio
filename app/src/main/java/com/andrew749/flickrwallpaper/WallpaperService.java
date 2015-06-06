package com.andrew749.flickrwallpaper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;

import com.andrew749.flickrwallpaper.DataHelper.LocalStorage;
import com.andrew749.flickrwallpaper.FlickrHelper.FlickrSearcher;
import com.andrew749.flickrwallpaper.Fragments.SettingsFragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by andrewcodispoti on 2015-05-10.
 */
public class WallpaperService extends android.service.wallpaper.WallpaperService implements SharedPreferences.OnSharedPreferenceChangeListener {
    private Paint paint = new Paint();
    private float interval = (float) .1;
    private int cacheSize = 100;
    private String imageSize = "Large";
    Timer timer=new Timer();
    @Override
    public Engine onCreateEngine() {
        updateProperties();
        getSharedPreferences(SettingsFragment.prefsName, Context.MODE_PRIVATE).registerOnSharedPreferenceChangeListener(this);
        return new PhotoEngine();
    }

    private void updateProperties() {
        //get preferences
        cacheSize = getSharedPreferences(SettingsFragment.prefsName, Context.MODE_PRIVATE).getInt(SettingsFragment.cacheName, 5);
        interval = getSharedPreferences(SettingsFragment.prefsName, Context.MODE_PRIVATE).getFloat(SettingsFragment.refreshName, (float) .1);
        imageSize = getSharedPreferences(SettingsFragment.prefsName, Context.MODE_PRIVATE).getString(SettingsFragment.imageName, "Large");
    }

    //easier just to update all rather than having to do string comparison
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updateProperties();
    }

    /**
     * Subclass that handles downloading of images.
     */
    class PhotoEngine extends Engine {
        private final Handler handler = new Handler();
        ArrayList<String> imageNames = new ArrayList<>();
        int index = 0;
        LocalStorage storage;
        FlickrSearcher searcher;
        Bitmap previous, current;
        private boolean visible = true;

        private final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //do a query to get more images if anything changes or if its the first run
                if (imageNames.size() == 0) {
                    Log.d("flickr","no image name");
                    searcher = new FlickrSearcher(getApplicationContext());
                    searcher.getImages(cacheSize);
                    checkForNewImage();
                    handler.postDelayed(this, 1000);
                    return;
                }

                //go back in bounds
                if (index > imageNames.size() - 1) index = 0;

                draw();
                if (imageNames.size() != cacheSize) {
                    checkForNewImage();
                }
            }
        };

        private void checkForNewImage() {
            //when getting close to end
            ArrayList<String> updatedNames = storage.getImageNames();

            //check to see if the image already exists in the array.
            for (String imageName : updatedNames) {
                //if flag =0 then don't add the image
                int flag = 1;
                for (String imageName2 : imageNames) {
                    if (imageName.equals(imageName2)) {
                        flag = 0;
                        break;
                    }
                }
                if (flag == 1) {
                    imageNames.add(imageName);
                    Log.d("Flickr", "Adding new image");
                }

            }

        }
        private TimerTask task=new TimerTask() {
            @Override
            public void run() {
                long lastUpdate=getSharedPreferences(SettingsFragment.prefsName,Context.MODE_PRIVATE).getLong("lastUpdate",0);
                long diff= System.currentTimeMillis()-lastUpdate;
                if(storage!=null&&diff!=0&&diff>1000){
                    Log.d("flickr", "do update stuff here");
                    storage.deleteImages();
                    if(searcher==null)
                        searcher=new FlickrSearcher(getApplicationContext());
                    searcher.getImages(cacheSize);

                    imageNames.clear();
                }
            }
        };
        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            paint.setColor(Color.WHITE);
            storage = LocalStorage.getInstance(getApplicationContext());
            imageNames.addAll(storage.getImageNames());
            timer.schedule(task,1000,86400000);
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
        void draw() {
            final SurfaceHolder holder = getSurfaceHolder();
            Canvas c = holder.lockCanvas();
            previous = current;
            current = storage.getImage(imageNames.get(index++));
            if (current != null) {
                try {
                    // clear the canvas
                    int canvasWidth = c.getWidth(), canvasHeight = c.getHeight();
                    float scaleFactor = canvasHeight / current.getHeight();
                    int scaledWidth = (int) (current.getWidth() * scaleFactor);
                    Bitmap fill;
                    if (scaledWidth > c.getWidth())
                        fill = Bitmap.createScaledBitmap(current, scaledWidth, canvasHeight, false);
                    else {
                        fill = Bitmap.createScaledBitmap(current, canvasWidth, canvasHeight, false);
                    }
                    current = fill;
                } finally {
                    holder.unlockCanvasAndPost(c);
                    animatePicture(holder);
                }
                handler.removeCallbacks(runnable);
                if (visible) {
                    handler.postDelayed(runnable, (long) (interval * 1000 * 60));
                }
            } else {
                //most likely a deletion so recheck the array for null entries
                Iterator<String> iter = imageNames.iterator();

                while (iter.hasNext()) {
                    String temp=iter.next();
                    if (!storage.imageExists(temp)) {
                        iter.remove();
                    }
                }
                holder.unlockCanvasAndPost(c);
                handler.postDelayed(runnable,1000);

            }
        }

        private void animatePicture(SurfaceHolder holder) {
            int width = 1;
            for (int x = 0; x < width; x += (int) width / 60) {
                Canvas c = holder.lockCanvas();
                width = c.getWidth();
                c.drawColor(Color.BLACK);
                if (c != null) {
                    synchronized (holder) {
                        if (previous != null) {
                            c.drawBitmap(previous, -x, 0, paint);
                        }
                        if (index >= 0 && current != null) {
                            if (c.getWidth() - x > (width / 60))
                                c.drawBitmap(current, c.getWidth() - x, 0, paint);
                            else
                                c.drawBitmap(current, 0, 0, paint);
                        }
                    }

                    holder.unlockCanvasAndPost(c);
                }
            }

        }
    }
}
