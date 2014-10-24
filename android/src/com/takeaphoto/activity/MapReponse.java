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
import com.takeaphoto.database.Demande;
import com.takeaphoto.database.DemandesBDD;

public class MapReponse extends SupportMapFragment  {

	 final String EXTRA_LOGIN = "user_login";
	
	 private GoogleMap gMap;
	 private Activity mainActivity ;
	 private DemandesBDD demandeBdd ;
	 private ArrayList<MarkerOptions> markers ;
	 
	 @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        markers = new ArrayList<MarkerOptions>() ;
    }

	public void setMainActivity(Activity main) {
		mainActivity = main ;
	}
	
	public void setDemandeBdd(DemandesBDD demandeBdd){
		this.demandeBdd = demandeBdd ;
	}
	
	@Override
	public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        updateDemandes() ;
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (gMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            gMap = getMap();
            gMap.setMyLocationEnabled(true);
        }
    }
    
    private void updateDemandes(){
    	demandeBdd.open() ;
        ArrayList<Demande> demandes = demandeBdd.getDemandeWithoutLogin(mainActivity.getIntent().getStringExtra(EXTRA_LOGIN)) ;
        demandeBdd.close() ;
        
        if(demandes != null){
        	for(Demande d : demandes){
        		MarkerOptions m = new MarkerOptions() ;
        		m.title(d.getLogin()) ;
        		m.position(new LatLng(d.getLat(), d.getLng())) ;
        		m.snippet(d.getDescription()) ;
        		markers.add(m) ;
        	}
        }
        
        for(MarkerOptions m : markers){
        	gMap.addMarker(m);
        }
    }
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
	
	View myFragmentView = super.onCreateView(inflater, container, savedInstanceState) ;
	
	return myFragmentView;
}
}
