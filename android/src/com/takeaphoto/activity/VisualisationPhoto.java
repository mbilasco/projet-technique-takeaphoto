package com.takeaphoto.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.takeaphoto.flickr.ImageDownloadTask;
 
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.widget.ImageView;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

 
public class VisualisationPhoto extends FragmentActivity {
 
    ImageView welcome;
    String urlWelcome = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        
        urlWelcome = this.getIntent().getStringExtra("URL_PHOTO");
        welcome = (ImageView) findViewById(R.id.imageView1);
        // Manifest.permission.INTERNET;
        //downloadImage();
        //welcome.setImageResource(R.drawable.ic_launcher); cette ligne me permet de voir que je ne peu changer l'image que depuis les ressources
        ImageDownloadTask img = new ImageDownloadTask(welcome);
        img.execute(urlWelcome);
    }
 
    private void downloadImage() {
        Bitmap bitmap = null;
/*        try {
            HttpURLConnection connection = (HttpURLConnection) urlWelcome.openConnection();
            InputStream inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            
            welcome.setImageBitmap(bitmap);
            
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/    }
}