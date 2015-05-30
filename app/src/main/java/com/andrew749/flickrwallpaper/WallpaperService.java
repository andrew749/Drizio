package com.andrew749.flickrwallpaper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.ArrayList;

/**
 * Created by andrewcodispoti on 2015-05-10.
 */
public class WallpaperService extends android.service.wallpaper.WallpaperService {
    private Paint paint = new Paint();
    @Override
    public Engine onCreateEngine() {
        return new PhotoEngine();
    }

    /**
     * Subclass that handles downloading of images.
     */
    class PhotoEngine extends Engine implements ImageDownloadingInterface,ListDownloadingInterface {
        private final Handler handler = new Handler();
        ArrayList<Bitmap> images = new ArrayList<Bitmap>();
        Bitmap currentImage;
        LocalStorage storage;
        int index = 0;
        private boolean visible = true;


        private final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (index > images.size()) index = 0;
                //gets the next image
                if(images.size()>2)
                    currentImage = images.get(index++);
                draw(currentImage);
                if (images.size() - index < 2) {
                    //get more images
                    int tempsize=images.size();
                    images.addAll(storage.getImages());
                    if(images.size()-tempsize<5)index=0;
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

            Canvas c = null;
            try {
                c = holder.lockCanvas();
                // clear the canvas
                c.drawColor(Color.BLACK);
                int canvasWidth=c.getWidth(),canvasHeight=c.getHeight();
                float scaleFactor=canvasHeight/bm.getHeight();
                Bitmap fill=Bitmap.createScaledBitmap(bm,(int)(bm.getWidth()*scaleFactor),canvasHeight,false);
                c.drawBitmap(fill, 0, 0, paint);
            } finally {
                if (c != null)
                    holder.unlockCanvasAndPost(c);
            }

            handler.removeCallbacks(runnable);
            if (visible) {
                handler.postDelayed(runnable, 10000); // delay 10 seconds
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
            Log.d("FlickrWallpaper", "added photo");
            storage.writeToExternalStorage(bm,getApplicationContext());
            Log.d(MainActivity.TAG,"wrote photo");
        }
    }
}
