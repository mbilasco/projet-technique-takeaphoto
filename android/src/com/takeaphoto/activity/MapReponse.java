package com.takeaphoto.activity;

import java.io.Serializable;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.googlecode.flickrjandroid.oauth.OAuth;

import com.takeaphoto.model.Demande;
import com.takeaphoto.model.User;

/**
 * Fragment permettant de visualiser les demandes des autres utilisateurs sur une map et d'y répondre en prenant des phtoso 
 * @author Maxime & Jules
 *
 */
public class MapReponse extends SupportMapFragment {
	private ArrayList<MarkerOptions> markers;
	private Activity mainActivity;
	private GoogleMap gMap;
	private OAuth oauth;
	private ArrayList<Demande> demandes;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		markers = new ArrayList<MarkerOptions>();
	}

	public void initialize(Activity main, User user) {
		this.setMainActivity(main);	
	}

	public void setMainActivity(Activity main) {
		mainActivity = main;
	}

	@Override
	public void onResume() {
		super.onResume();
		setUpMapIfNeeded();
		updateMap();
	}

	private void setUpMapIfNeeded() {
		if (gMap == null) {
			gMap = getMap();
			gMap.setMyLocationEnabled(true);
			
			LatLng position = new LatLng(42.00,24.00);
	        gMap.moveCamera(CameraUpdateFactory.newLatLng(position));
	        gMap.animateCamera(CameraUpdateFactory.zoomTo(2));
		}
		// Lors d'un click sur le marker, lancement activité du résumé de la demande pour ensuite prendre la photo
		gMap.setOnMarkerClickListener(new OnMarkerClickListener(){
			public boolean onMarkerClick(Marker marker) {				
				Intent intent = new Intent(mainActivity.getApplicationContext(), VisualisationDemande.class);
	            intent.putExtra("LAT_VALUE", marker.getPosition().latitude +"");
	            intent.putExtra("LNG_VALUE", marker.getPosition().longitude +"");
	            intent.putExtra("DESC_VALUE", marker.getSnippet());
	            intent.putExtra("OAUTH_VALUE", 	oauth);
				startActivity(intent);
				
				return true;
			}
		});
	}
	
	/**
	 * Ajout des demandes des autres utilisateur sur la map
	 */
	private void updateMap() {
		for (Demande d : demandes) {
			MarkerOptions m = new MarkerOptions();
			m.title("Demande de + TODO...");
			m.position(new LatLng(d.getLat(), d.getLng()));
			m.snippet(d.getDescription());
			markers.add(m);
		}

		for (MarkerOptions m : markers) {
			gMap.addMarker(m);
		}
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View myFragmentView = super.onCreateView(inflater, container, savedInstanceState);

		return myFragmentView;
	}

	public void setOauth(Serializable oauth) {
		this.oauth = (OAuth) oauth;
	}
	
	public ArrayList<Demande> getDemandes() {
		return demandes;
	}

	public void setDemandes(ArrayList<Demande> demandes) {
		this.demandes = demandes;
	}
}