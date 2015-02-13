package com.takeaphoto.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.takeaphoto.model.Demande;
import com.takeaphoto.model.User;
import com.takeaphoto.server.DemandeServeur;

/**
 * Fragment permettant d'ajouter et visualiser ses demandes par des markers sur une map 
 * @author Maxime & Jules
 *
 */
public class MapAdd extends SupportMapFragment implements OnMarkerDragListener {
	private GoogleMap gMap;
	private MarkerOptions ajoutMarker;
	private Activity mainActivity;
	private User user;
	private ArrayList<Demande> demandes;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ajoutMarker = new MarkerOptions();
		setHasOptionsMenu(true);
	}

	public void initialize(Activity main, User user) {
		this.setMainActivity(main);
		this.setUser(user);
	}

	public void setMainActivity(Activity main) {
		mainActivity = main;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Ajout des demandes représentés par des markers sur la map
	 */
	private void setMarkerDemandes() {
		MarkerOptions m = new MarkerOptions();
		
		for(Demande d : demandes){
			m.title(d.getDescription());
			
			switch(d.getEtat()){
				case 0 :
					m.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
					break;
				case 1 :
					m.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
					break;
				case 2 :
					m.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
					break;
			}
			m.position(new LatLng(d.getLat(), d.getLng()));
			m.snippet(d.getId()+"");
			m.draggable(true);
	
			gMap.addMarker(m);
		}
	}
	
	/**
	 * Ajout d'un marker lors de l'ajout d'une demande
	 * @param result
	 */
	private void setMarker(String result) {
		ajoutMarker.title("Demande non validée");
		ajoutMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
		ajoutMarker.draggable(true);
		ajoutMarker.snippet(result);
		gMap.addMarker(ajoutMarker);
	}

	@Override
	public void onResume() {
		super.onResume();
		setUpMapIfNeeded();
		gMap.clear();
		setMarkerDemandes();
	}

	private void setUpMapIfNeeded() {
		if (gMap == null) {
			gMap = getMap();
			gMap.setMyLocationEnabled(true);
			gMap.setOnMarkerDragListener(this);
			LatLng position = new LatLng(42.00,24.00);
	        gMap.moveCamera(CameraUpdateFactory.newLatLng(position));
	        gMap.animateCamera(CameraUpdateFactory.zoomTo(2));
			
			gMap.setOnMapLongClickListener(new OnMapLongClickListener() {
				public void onMapLongClick(LatLng point) {
					ajoutMarker.position(point);

					AlertDialog.Builder alert = new AlertDialog.Builder(mainActivity);

					alert.setTitle("Description de la photo voulue :");

					final EditText input = new EditText(mainActivity);
					alert.setView(input);

					alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,	int whichButton) {
							setMarker(input.getText().toString());
						}
					});

					alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,	int whichButton) {	}
					});

					alert.show();

					gMap.animateCamera(CameraUpdateFactory.newLatLng(point));

				}
			});
		}
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.map_add, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_save:
				String desc = ajoutMarker.getSnippet();
				AlertDialog.Builder alert = new AlertDialog.Builder(mainActivity);
	
				if (desc != null) {
					alert.setTitle("Voulez-vous vraiment valider cette demande ?");
					alert.setMessage("Description : \n"	+ ajoutMarker.getSnippet());
					
					alert.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,	int whichButton) {
								Demande demande = new Demande(user.getUserId(),	ajoutMarker.getPosition().latitude, ajoutMarker.getPosition().longitude, ajoutMarker.getSnippet());
								
								DemandeServeur demandeServeur = new DemandeServeur();
	
								demandeServeur.addDemande(user, demande);
								demandes.add(demande);
								
								Toast.makeText(mainActivity, "Votre demande a ete ajoutee",	Toast.LENGTH_SHORT).show();
								
								ajoutMarker = new MarkerOptions();
								gMap.clear();
								setMarkerDemandes();
							}
						});
	
					alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {	}
					});
				} else {
					alert.setTitle("Erreur");
					alert.setMessage("Vous devez d'abord poser un marqueur et lui ajouter une description");
					alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,	int whichButton) {	}
					});
				}
	
				alert.show();
				return true;
			default:
				return super.onOptionsItemSelected(item);
			}
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View myFragmentView = super.onCreateView(inflater, container, savedInstanceState);
		return myFragmentView;
	}

	@Override
	public void onMarkerDrag(Marker marker) {	}

	@Override
	public void onMarkerDragEnd(Marker marker) {
		if(marker.getTitle().compareTo("Demande non validée")!=0){
			for(Demande d : demandes){
				if(d.getId() == Integer.parseInt(marker.getSnippet())){
					d.setLat(marker.getPosition().latitude);
					d.setLng(marker.getPosition().longitude);
					DemandeServeur demandeServeur = new DemandeServeur();
					demandeServeur.updatePositionDemande(d.getId(), d.getLat(), d.getLng());
				}
			}
		}
		else{
			ajoutMarker.position(marker.getPosition());
		}
	}

	@Override
	public void onMarkerDragStart(Marker marker) {	  }

	public ArrayList<Demande> getDemandes() {
		return demandes;
	}

	public void setDemandes(ArrayList<Demande> demandes) {
		this.demandes = demandes;
	}
}
