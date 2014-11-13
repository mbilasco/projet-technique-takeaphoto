package com.takeaphoto.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.takeaphoto.model.Demande;
import com.takeaphoto.model.User;
import com.takeaphoto.server.DemandeServeur;
import com.takeaphoto.database.DemandesBDD;
import com.takeaphoto.database.UserBDD;

public class MapReponse extends SupportMapFragment {
	private GoogleMap gMap;
	private Activity mainActivity;
	private DemandesBDD demandesBDD;
	private User user;
	private ArrayList<MarkerOptions> markers;

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
	}

	private void updateDemandes() {
		//demandesBDD.open();
		DemandeServeur demandeServeur = new DemandeServeur();
		//demandeServeur.addDemande(mainActivity.getApplicationContext(), user, demande);
		//ArrayList<Demande> demandes = demandesBDD.getDemandeWithoutId(this.user.getId());
		ArrayList<Demande> demandes = demandeServeur.getDemandesOthers(mainActivity.getApplicationContext(), this.user);
		//demandesBDD.close();

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

		View myFragmentView = super.onCreateView(inflater, container,
				savedInstanceState);

		return myFragmentView;
	}
}
