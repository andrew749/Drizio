package com.andrew749.flickrwallpaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by andrewcodispoti on 2015-05-10.
 */
public class ImageAdapter extends BaseAdapter {
    @Override
    public int getCount() {
        return results.size();
    }
    private  static Bitmap fallbackBitmap;
    ArrayList<FlickrResult> results=new ArrayList<FlickrResult>();
    Context context;

    public ImageAdapter(ArrayList<FlickrResult> results, Context context) {
        this.results = results;
        this.context = context;
    }

    @Override
    public Object getItem(int i) {
        return results.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View listRow = view;
        if (listRow == null)
            listRow = inflater.inflate(R.layout.listelement, viewGroup, false);
        TextView titleText = (TextView) listRow.findViewById(R.id.imageTitle);
        ImageView imageView=(ImageView)listRow.findViewById(R.id.image);
        results.get(i).getAndSetImage(imageView);
//        else{
//            if(fallbackBitmap==null) {
//                fallbackBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.defaultplaceholder);
//            }
//
//            imageView.setImageBitmap(fallbackBitmap);
//        }

        titleText.setText(results.get(i).getName());

        return listRow;
    }
}
