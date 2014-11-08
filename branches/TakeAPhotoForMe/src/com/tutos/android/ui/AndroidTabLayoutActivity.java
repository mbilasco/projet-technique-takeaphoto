package com.tutos.android.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class AndroidTabLayoutActivity extends TabActivity {
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.androidtablayout);
        TabHost tabHost = getTabHost();
        
        
        // setting Title and Icon for the Tab
        TabSpec photospec = tabHost.newTabSpec("TaheAPhotoForMe");
        photospec.setIndicator("TaheAPhotoForMe", getResources().getDrawable(R.drawable.ic_action_search));
        Intent photosIntent = new Intent(this, MapActivity.class);
        photospec.setContent(photosIntent);
        
        // Tab for Videos
        TabSpec videospec = tabHost.newTabSpec("ITakeForYou");
        videospec.setIndicator("ITakeForYou", getResources().getDrawable(R.drawable.ic_launcher));
        Intent videosIntent = new Intent(this, MapActivity.class);
        videospec.setContent(videosIntent);
 
        // Adding all TabSpec to TabHost
        tabHost.addTab(photospec); // Adding photos tab
        tabHost.addTab(videospec); // Adding photos tab
    }
}
