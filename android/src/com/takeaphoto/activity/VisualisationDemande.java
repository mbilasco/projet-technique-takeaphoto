package com.takeaphoto.activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.googlecode.flickrjandroid.oauth.OAuth;
import com.takeaphoto.flickr.OAuthTask;
import com.takeaphoto.flickr.UploadPhotoFlickr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Fragment permettant de visualiser les d�tails d'une demande
 * @author Maxime & Jules
 *
 */
public class VisualisationDemande extends FragmentActivity {
	static final int REQUEST_TAKE_PHOTO = 1;
	
	TextView latValue;
	TextView lngValue;
	TextView descValue;
	Button buttonTakePhoto;
	
	private String lat;
	private String lng;
	private String desc;
	private OAuth oauth;
	
	private File photoFile;
	private ProgressDialog mProgressDialog;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_demande);
        
        latValue = (TextView) findViewById(R.id.latitude_value);
        lngValue = (TextView) findViewById(R.id.longitude_value);
        descValue = (TextView) findViewById(R.id.desc_value);
        buttonTakePhoto = (Button) findViewById(R.id.button_take_photo);
        
        lat = this.getIntent().getStringExtra("LAT_VALUE");
        lng = this.getIntent().getStringExtra("LNG_VALUE");
        desc = this.getIntent().getStringExtra("DESC_VALUE");
        oauth = (OAuth) this.getIntent().getSerializableExtra("OAUTH_VALUE");
        
        latValue.setText(lat);
        lngValue.setText(lng);
        descValue.setText(desc);
     
        // Action permettant � l'utilisateur de prendre la photo
        buttonTakePhoto.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dispatchTakePictureIntent();
			}
        });
	}
    
	@Override
	public void onResume() {
		super.onResume();
		
	  	if (oauth == null) {
            OAuthTask task = new OAuthTask(this.getApplicationContext());
            task.execute();
    	} else	if(photoFile!=null){
        	UploadPhotoFlickr task = new UploadPhotoFlickr(this,oauth,photoFile,mProgressDialog,lat,lng);
        	
            try {
            	task.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
            
		}
	}
	
	/**
	 * Lancement activit� pour prendre une photo
	 */
	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    // Ensure that there's a camera activity to handle the intent
	    if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
	        // Create the File where the photo should go
	        photoFile = null;
	        try {
	            photoFile = createImageFile();
	        } catch (IOException ex) {
	            // Error occurred while creating the File
	            System.err.print("IOException");
	        }
	        // Continue only if the File was successfully created
	        if (photoFile != null) {
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
	            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
	        }
	    }
	}
	
	private File createImageFile() throws IOException {
	    // Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "JPEG_" + timeStamp + "_";
	    File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
	    File image = File.createTempFile(imageFileName, ".jpg", storageDir);
	    // Save a file: path for use with ACTION_VIEW intents
	    String mCurrentPhotoPath = "file:" + image.getAbsolutePath();
	    return image;
	}
}