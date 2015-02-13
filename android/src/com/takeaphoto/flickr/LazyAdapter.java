package com.takeaphoto.flickr;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.googlecode.flickrjandroid.photos.PhotoList;

public class LazyAdapter extends BaseAdapter {
   
    private Activity activity;
    private PhotoList photos;
    private static LayoutInflater inflater=null;
   
    public LazyAdapter(Activity a, PhotoList d) {
        activity = a;
        photos = d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return photos.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
   
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;

        return vi;
    }
}