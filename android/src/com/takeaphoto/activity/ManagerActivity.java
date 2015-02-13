package com.takeaphoto.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.takeaphoto.model.Demande;
import com.takeaphoto.model.Reponse;
import com.takeaphoto.model.User;
import com.takeaphoto.server.DemandeServeur;

/**
 * Fragment permettant la gestion des demandes 
 * @author Maxime & Jules
 *
 */
public class ManagerActivity extends ListFragment {
	private ArrayList<Demande> demandes;
	private User user;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		actualiserListeDemande();
	}

	public void setUser(User user) {
		this.user = user;
	}

	private void updateDemandes() {
		if (this.user != null) {
			new DemandeServeur().getMyDemandes(getActivity(), this.user);
			actualiserListeDemande();
		} else
			setListAdapter(null);
	}

	/**
	 * Mise à jour des demandes & construction de l'adapter pour la vue
	 */
	public void actualiserListeDemande() {
		if (this.user != null) {
			if (demandes != null) {				
				List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

				String[] Descriptions = new String[demandes.size()];
				int[] images = new int[demandes.size()];

				for (int i = 0; i < demandes.size(); i++) {
					Descriptions[i] = demandes.get(i).getDescription();

					switch (demandes.get(i).getEtat()) {
						case 0:
							images[i] = R.drawable.rouge;
							break;
	
						case 1:
							images[i] = R.drawable.jaune;
							break;
	
						case 2:
							images[i] = R.drawable.vert;
							break;
						default:
							break;
					}
				}

				for (int i = 0; i < demandes.size(); i++) {
					HashMap<String, String> hm = new HashMap<String, String>();
					hm.put("txt", Descriptions[i]);
					hm.put("image", Integer.toString(images[i]));
					aList.add(hm);
				}

				// Keys used in Hashmap
				String[] from = { "image", "txt" };

				// Ids of views in listview_layout
				int[] to = { R.id.image, R.id.txt };

				// Instantiating an adapter to store each items R.layout.listview_layout defines the layout of each item
				SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.listview, from, to);

				setListAdapter(adapter);
			} else
				setListAdapter(null);
		} else
			setListAdapter(null);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_save:
				updateDemandes();
				break;
		}
		return true;
	}

	/**
	 * Action lors d'un click sur l'item de la list de l'adapter
	 */
	public void onListItemClick(ListView l, View v, final int position, long id) {
		final int id_demande = demandes.get(position).getId();
		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		int etat = demandes.get(position).getEtat();
		
		if (etat == 0) {
			alert.setTitle("Demande : " + demandes.get(position).getDescription());
			alert.setPositiveButton("Modifier Description", new OnClickListener() {
				public void onClick(DialogInterface dialog,	int whichButton) {
					renomerDemande(position);
				}
			});

			alert.setNeutralButton("Supprimer Demande", new OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					demandes.remove(position);
					supprimerDemande(id_demande);
				}
			});

			alert.setNegativeButton("Annuler", new OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {	}
			});

			alert.show();
		} else {
			DemandeServeur demandeServeur = new DemandeServeur();
			ArrayList<Reponse> result = demandeServeur.getURLPhotoReponse(id_demande);
						
			if (result != null) {
				Intent intent = new Intent(getActivity(), VisualisationReponses.class);
				intent.putExtra("REPONSES", result);
				intent.putExtra("ID_DEMANDE", result.get(0).getId_demande());
		
				startActivity(intent);
			}
		}
	}
	
	/**
	 * Methode pour modifier une demande
	 * @param position
	 */
	private void renomerDemande(final int position) {
		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

		alert.setTitle("Description de la photo voulue :");

		final EditText input = new EditText(getActivity());
		input.setText(demandes.get(position).getDescription());
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String desc = input.getText().toString();
				demandes.get(position).setDescription(desc);
				String result = new DemandeServeur().updateDemande(getActivity(), user, demandes.get(position).getId(),	"description", desc);
				if (result != null)
					Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(getActivity(), R.string.erreur_connextion,Toast.LENGTH_SHORT).show();

				actualiserListeDemande();
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {	}
		});
		alert.show();
	}

	/**
	 * Methode pour supprimer une demande
	 * @param id_demande
	 */
	private void supprimerDemande(final int id_demande) {
		String result = new DemandeServeur().removeDemande(getActivity(), this.user, id_demande);

		if (result != null){
			Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
		}
		else{
			Toast.makeText(getActivity(), R.string.erreur_connextion, Toast.LENGTH_SHORT).show();
		}
		actualiserListeDemande();
	}

	public ArrayList<Demande> getDemandes() {
		return demandes;
	}

	public void setDemandes(ArrayList<Demande> demandes) {
		this.demandes = demandes;
	}
}
