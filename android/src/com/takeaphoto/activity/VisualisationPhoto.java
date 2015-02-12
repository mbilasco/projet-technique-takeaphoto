package com.takeaphoto.activity;

import com.takeaphoto.flickr.ImageDownloadTask;
 
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
 
public class VisualisationPhoto extends FragmentActivity {
    ImageView welcome;
    String url = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        
        url = this.getIntent().getStringExtra("URL_PHOTO");
        welcome = (ImageView) findViewById(R.id.imageView1);
       
        ImageDownloadTask img = new ImageDownloadTask(welcome);
        img.execute(url);
    }
}