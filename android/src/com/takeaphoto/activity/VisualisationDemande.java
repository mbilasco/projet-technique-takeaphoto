package com.takeaphoto.activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.Marker;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.takeaphoto.flickr.GetPhotoInfoTask;
import com.takeaphoto.flickr.OAuthTask;
import com.takeaphoto.flickr.UploadPhotoFlickr;
import com.takeaphoto.model.Demande;
import com.takeaphoto.model.Reponse;
import com.takeaphoto.model.User;
import com.takeaphoto.server.DemandeServeur;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class VisualisationDemande extends Activity {
	static final int REQUEST_TAKE_PHOTO = 1;
	
	private String mCurrentPhotoPath;
	
	TextView latValue;
	TextView lngValue;
	TextView descValue;
	Button buttonTakePhoto;
	
	private String lat;
	private String lng;
	private OAuth oauth;
	
	private File photoFile;
    
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
        oauth = (OAuth) this.getIntent().getSerializableExtra("OAUTH_VALUE");
     
        buttonTakePhoto.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				/*
				Intent intent = new Intent(getApplicationContext(), VisualisationDemande.class);
	            intent.putExtra("LAT_VALUE", lat);
	            intent.putExtra("LNG_VALUE", lng);
	            intent.putExtra("OAUTH_VALUE", 	oauth);
				startActivity(intent);
				*/
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
        	UploadPhotoFlickr task = new UploadPhotoFlickr(this,oauth,photoFile);
			
			DemandeServeur demandeServeur = new DemandeServeur();
			List <Demande> demandes = demandeServeur.getDemandesByLatLng(this, lat, lng);
			
            Demande demande = demandes.get(0);
            String photoId = new String("");
            String url = new String("");
            try {
            	photoId = task.execute().get();
            	
				Log.i("je suis de retouuuuuuuuuur", photoId);
				GetPhotoInfoTask getInfoTask = new GetPhotoInfoTask(this, oauth, photoId);
				url = getInfoTask.execute().get();
				Log.i("URL MA PHOTO", url);
				
			} catch (Exception e) {
				e.printStackTrace();
			} 
            Reponse reponse = new Reponse(url, demande.getId());

			demandeServeur.addReponse(this.getApplicationContext(),	demande, reponse);
			demandeServeur.updateEtatDemande(demande.getId(), 1);
		}
	}
	

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
	    mCurrentPhotoPath = "file:" + image.getAbsolutePath();
	    return image;
	}
}