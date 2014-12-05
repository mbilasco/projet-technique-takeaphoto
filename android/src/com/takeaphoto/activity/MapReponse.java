package com.takeaphoto.activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.takeaphoto.database.DemandesBDD;
import com.takeaphoto.model.Demande;
import com.takeaphoto.model.User;
import com.takeaphoto.server.DemandeServeur;

public class MapReponse extends SupportMapFragment {
	static final int REQUEST_TAKE_PHOTO = 1;
	private ArrayList<MarkerOptions> markers;
	private String mCurrentPhotoPath;
	private DemandesBDD demandesBDD;
	private Activity mainActivity;
	private GoogleMap gMap;
	private User user;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		markers = new ArrayList<MarkerOptions>();
	}

	public void initialize(Activity main, DemandesBDD demandesBDD, User user) {
		this.setMainActivity(main);
		this.setDemandeBDD(demandesBDD);
		this.setUser(user);
	}

	public void setMainActivity(Activity main) {
		mainActivity = main;
	}

	public void setDemandeBDD(DemandesBDD demandesBDD) {
		this.demandesBDD = demandesBDD;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public void onResume() {
		super.onResume();
		setUpMapIfNeeded();
		updateDemandes();
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (gMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			gMap = getMap();
			gMap.setMyLocationEnabled(true);
		}
		gMap.setOnMarkerClickListener(new OnMarkerClickListener(){

			@Override
			public boolean onMarkerClick(Marker arg0) {
				dispatchTakePictureIntent();
			 
				return true;
			}
		});
	}
	
	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    // Ensure that there's a camera activity to handle the intent
	    if (takePictureIntent.resolveActivity(this.getActivity().getPackageManager()) != null) {
	        // Create the File where the photo should go
	        File photoFile = null;
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
	    File storageDir = Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_PICTURES);
	    File image = File.createTempFile(
	        imageFileName,  /* prefix */
	        ".jpg",         /* suffix */
	        storageDir      /* directory */
	    );

	    // Save a file: path for use with ACTION_VIEW intents
	    mCurrentPhotoPath = "file:" + image.getAbsolutePath();
	    return image;
	}
	
	private void demandeAddPic(int id_demande) {
	    File f = new File(mCurrentPhotoPath);
	    Uri contentUri = Uri.fromFile(f);
	    File tmp = new File(contentUri.toString());
	    DemandeServeur demandeServeur = new DemandeServeur();
	    demandeServeur.uploadMedia(mainActivity.getApplicationContext(), this.user, id_demande, tmp);
	}

	private void updateDemandes() {
		DemandeServeur demandeServeur = new DemandeServeur();
		Log.i("LOL",mainActivity.getApplicationContext().getPackageName());
		Log.i("LOL",this.user.getLogin());
		ArrayList<Demande> demandes = demandeServeur.getDemandesOthers(mainActivity.getApplicationContext(), this.user);

		if (demandes != null) {
			for (Demande d : demandes) {
				MarkerOptions m = new MarkerOptions();
				m.title("Demande de + TODO...");
				m.position(new LatLng(d.getLat(), d.getLng()));
				m.snippet(d.getDescription());
				markers.add(m);
			}
		}

		for (MarkerOptions m : markers) {
			gMap.addMarker(m);
		}
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View myFragmentView = super.onCreateView(inflater, container, savedInstanceState);

		return myFragmentView;
	}
}