package com.andrew749.drizio.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.andrew749.drizio.FlickrHelper.FlickrResult;
import com.andrew749.drizio.FullSizeImage;
import com.andrew749.drizio.R;

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
        final int j=i;
        View listRow = view;
        if (listRow == null)
            listRow = inflater.inflate(R.layout.listelement, viewGroup, false);
        TextView titleText = (TextView) listRow.findViewById(R.id.imageTitle);
        ImageView imageView=(ImageView)listRow.findViewById(R.id.sideImage);
        results.get(i).getAndSetImage(imageView);
        titleText.setText(results.get(i).getName());
        listRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,FullSizeImage.class);
                intent.putExtra("image",results.get(j).getImage());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        return listRow;
    }
}
