package com.takeaphoto.flickr;

import java.util.List;

import com.googlecode.flickrjandroid.REST;
import com.googlecode.flickrjandroid.RequestContext;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.PhotosInterface;
import com.takeaphoto.model.Demande;
import com.takeaphoto.model.Reponse;
import com.takeaphoto.server.DemandeServeur;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class GetPhotoInfoTask extends AsyncTask<String,Integer, String> {
	private OAuth oauth;
	private String photoId;
	private String lat;
	private String lng;
	private Photo photo;
	
	public GetPhotoInfoTask(OAuth oauth, String photoId, String lat, String lng) {
        super();
        this.oauth = oauth;
        this.photoId = photoId;
        this.lat = lat;
        this.lng = lng;
	}
	
	@Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

	@Override
	protected String doInBackground(String... params) {
		String url = new String("");
		RequestContext.getRequestContext().setOAuth(oauth);
		try {
			REST rest = new REST();
			
			PhotosInterface photoInt = new PhotosInterface(FlickrHelper.API_KEY, FlickrHelper.API_SEC, rest);
			photo = photoInt.getPhoto(photoId);
        	
    		Log.i("Get Photo", photo.toString());
    		Log.i("Get Photo", "GET PHOTO EFFECTUE");
			
			Log.i("Get Photo Info Task", "Get Photo Info Task finish");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return url;
	}	
	
	protected void onPostExecute(String url){
		super.onPostExecute(url);
		url = new String("https://farm"+photo.getFarm()+".staticflickr.com/"+photo.getServer()+"/"+photo.getId()+"_"+photo.getSecret()+"."+photo.getOriginalFormat());			
		DemandeServeur demandeServeur = new DemandeServeur();
		List<Demande> demandes = demandeServeur.getDemandesByLatLng(lat, lng);
        Demande demande = demandes.get(0);
		Reponse reponse = new Reponse(url, demande.getId());
		demandeServeur.addReponse(demande, reponse);
		demandeServeur.updateEtatDemande(demande.getId(), 2);
	}
}
