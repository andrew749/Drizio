package com.andrew749.flickrwallpaper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * Created by andrewcodispoti on 2015-05-10.
 */
public class WallpaperService extends android.service.wallpaper.WallpaperService {
    int x,y;
    private Paint paint=new Paint();

    @Override
    public Engine onCreateEngine() {
        return new PhotoEngine();
    }
    class PhotoEngine extends Engine{
        private  final Handler handler=new Handler();
        private boolean visible=true;
        private final Runnable runnable=new Runnable() {
            @Override
            public void run() {
                draw();
            }
        };
        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            paint.setColor(Color.BLUE);
            x=100;y=100;
        }
        @Override
        public void onVisibilityChanged(boolean visible)
        {
            this.visible = visible;
            // if screen wallpaper is visible then draw the image otherwise do not draw
            if (visible)
            {
                handler.post(runnable);
            }
            else
            {
                handler.removeCallbacks(runnable);
            }
        }
        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder)
        {
            super.onSurfaceDestroyed(holder);
            this.visible = false;
            handler.removeCallbacks(runnable);
        }
        public void onOffsetsChanged(float xOffset, float yOffset, float xStep, float yStep, int xPixels, int yPixels)
        {
            draw();
        }

        void draw()
        {
            final SurfaceHolder holder = getSurfaceHolder();

            Canvas c = null;
            try
            {
                c = holder.lockCanvas();
                // clear the canvas
                c.drawColor(Color.BLACK);
                if (c != null)
                {
                    // draw the background image
//                    c.drawBitmap(backgroundImage, 0, 0, null);
                    // draw the fish
                    c.drawRoundRect(x,y,x+10,y+10,10,10,paint);
                    // get the width of canvas
                    int width=c.getWidth();

                    // if x crosses the width means  x has reached to right edge
                    if(x>width+100)
                    {
                        // assign initial value to start with
                        x=-130;
                    }
                    // change the x position/value by 1 pixel
                    x=x+1;
                }
            }
            finally
            {
                if (c != null)
                    holder.unlockCanvasAndPost(c);
            }

            handler.removeCallbacks(runnable);
            if (visible)
            {
                handler.postDelayed(runnable, 10); // delay 10 mileseconds
            }

        }
    }
}
