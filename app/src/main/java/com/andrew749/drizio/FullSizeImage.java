package com.andrew749.drizio;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by andrew on 5/14/15.
 */
public class FullSizeImage extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout;
        ActionBar.LayoutParams params=new ActionBar.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

        layout= new LinearLayout(getApplicationContext());
        ImageView iv=new ImageView(getApplicationContext());
        iv.setLayoutParams(params);
        Bitmap image=(Bitmap)getIntent().getParcelableExtra("image");
        iv.setImageBitmap(image);
        layout.setLayoutParams(params);
        setContentView(layout);
    }
}
