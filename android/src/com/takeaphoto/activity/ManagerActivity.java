package com.takeaphoto.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.takeaphoto.model.Demande;
import com.takeaphoto.model.User;
import com.takeaphoto.server.DemandeServeur;
import com.takeaphoto.server.PhotoServeur;

public class ManagerActivity extends ListFragment {
	private ArrayList<Demande> demandes;
	private User user;
	private ArrayList<Bitmap> photos = null;
	private int nbPhotosCurrent, nbPhotos;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
/*
		if (demandes == null) {
			updateDemandes();
		}
*/	}
	
	@Override
	public void onResume() {
		super.onResume();
		actualiserListeDemande();
	}

	public void setUser(User user) {
		this.user = user;
	}

	private void updateDemandes() {
		Log.i("manager", user.getUserId());
		if (this.user != null) {
			new DemandeServeur().updateMyDemandesLocal(getActivity(), this.user);
			actualiserListeDemande();
		} else
			setListAdapter(null);
	}

	public void actualiserListeDemande() {
		if (this.user != null) {
			//demandes = new DemandeServeur().getMyDemandesLocal(getActivity(), this.user);
			
			if (demandes != null) {
				// Each row in the list stores country name, currency and flag
				Log.i("actu", demandes.size()+"");
				
				List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

				String[] Descriptions = new String[demandes.size()];
				int[] images = new int[demandes.size()];

				for (int i = 0; i < demandes.size(); i++) {
					Descriptions[i] = demandes.get(i).getDescription();

					switch (demandes.get(i).getEtat()) {
						case 0:
							images[i] = R.drawable.vert;
							break;
	
						case 1:
							images[i] = R.drawable.jaune;
							break;
	
						case 2:
							images[i] = R.drawable.rouge;
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

				// Instantiating an adapter to store each items
				// R.layout.listview_layout defines the layout of each item
				SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.listview, from, to);

				setListAdapter(adapter);
			} else
				setListAdapter(null);
		} else
			setListAdapter(null);
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Add your menu entries here
		super.onCreateOptionsMenu(menu, inflater);
		// inflater.inflate(R.menu.refresh, menu) ;
		inflater.inflate(R.menu.main, menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// case R.id.menu_refresh :
		case R.id.menu_save:
			updateDemandes();
			break;
		}
		return true;
	}

	@Override
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
					supprimerDemande(id_demande);
				}
			});

			alert.setNegativeButton("Annuler", new OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				}
			});

			alert.show();
		} else if (etat == 1 || etat == 2) {
			ArrayList<Object> result = new PhotoServeur().getUrls(this.user,id_demande);
			if (result != null) {
				photos = new ArrayList<Bitmap>();
				nbPhotos = result.size();
				nbPhotosCurrent = 0;
				for (Object url : result) {
					new getPhoto().execute((String) url, id_demande + "");
				}
			}
		}
	}

	public class getPhoto extends AsyncTask<String, Integer, Bitmap> {
		volatile int id_demande;

		@Override
		protected Bitmap doInBackground(String... args) {
			id_demande = Integer.parseInt(args[1]);
			String url = "http://jules-vanneste.fr/takeaphotoforme/" + args[0];
			Bitmap bmp = null;

			try {
				URLConnection conn = null;
				URL u = new URL(url);
				conn = u.openConnection();
				HttpURLConnection httpConn = (HttpURLConnection) conn;
				httpConn.setRequestMethod("GET");
				httpConn.connect();
				InputStream is = null;

				if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					is = httpConn.getInputStream();
					int thisLine;
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					while ((thisLine = is.read()) != -1) {
						bos.write(thisLine);
					}
					bos.flush();
					byte[] data = bos.toByteArray();

					if (bos != null) {
						bos.close();
					}

					bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
					return bmp;
				}

				httpConn.disconnect();

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return bmp;
		}

		protected void onPostExecute(Bitmap bmp) {
			photos.add(bmp);
			nbPhotosCurrent++;
			if (nbPhotos == nbPhotosCurrent) {
				nbPhotosCurrent = 0;
				afficherPhotos(id_demande);
			}

		}
	}

	private void afficherPhotos(final int id_demande) {
		if (photos != null && nbPhotosCurrent < nbPhotos) {
			AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
			ImageView iv = new ImageView(getActivity());
			iv.setImageBitmap(photos.get(nbPhotosCurrent));
			alert.setView(iv);
			alert.setTitle("photo " + (nbPhotosCurrent + 1) + "/" + nbPhotos);

			if (nbPhotosCurrent < nbPhotos - 1) {
				alert.setPositiveButton("Suivant", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						nbPhotosCurrent++;
						afficherPhotos(id_demande);
					}
				});
			}

			alert.setNegativeButton("Ok", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					photos = null;
					nbPhotos = 0;
					nbPhotosCurrent = 0;
					afficherPhotos(id_demande);
				}
			});

			alert.show();
		} else
			choixEtat(id_demande);
	}

	private void choixEtat(final int id_demande) {
		Demande demande = null;
		
		for(int i=0; i<demandes.size(); i++){
			if(demandes.get(i).getId()==id_demande){
				demande=demandes.get(i);
			}
		}
		if(demande!=null){
			if (demande.getEtat() == 1) {
				AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
				ImageView iv = new ImageView(getActivity());
				alert.setView(iv);
				alert.setTitle("Voulez-vous d'autres photos pour cette demande ?");
	
				alert.setPositiveButton("Oui", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
	
					}
				});
	
				alert.setNegativeButton("Non", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						new DemandeServeur().updateDemande(getActivity(), user,	id_demande, "etat", "2");
						actualiserListeDemande();
					}
				});
	
				alert.show();
			}
		}
	}

	private void renomerDemande(final int position) {
		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

		alert.setTitle("Description de la photo voulue :");

		// Set an EditText view to get user input
		final EditText input = new EditText(getActivity());
		input.setText(demandes.get(position).getDescription());
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				String desc = input.getText().toString();
				String result = new DemandeServeur().updateDemande(
						getActivity(), user, demandes.get(position).getId(),
						"description", desc);
				if (result != null)
					Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT)
							.show();
				else
					Toast.makeText(getActivity(), R.string.erreur_connextion,
							Toast.LENGTH_SHORT).show();

				actualiserListeDemande();
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}
				});
		alert.show();
	}

	private void supprimerDemande(final int id_demande) {

		String result = new DemandeServeur().removeDemande(getActivity(),
				this.user, id_demande);

		if (result != null){
			Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
		}
		else{
			Toast.makeText(getActivity(), R.string.erreur_connextion,
					Toast.LENGTH_SHORT).show();
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
