package com.takeaphoto.server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.takeaphoto.model.Demande;
import com.takeaphoto.model.Reponse;
import com.takeaphoto.utils.MCrypt;

/**
 * AsyncTask pour effectuer les demandes au serveur
 * @author Maxime
 *
 */
class ServeurAsync extends AsyncTask<ArrayList<String>, Void, String> {
	private static HashMap<String, Object> resultArray = null;
	private String nomFichier;
	private ArrayList<String> args;
	private static final MCrypt SecuritySingleton = new MCrypt(); 
	private Serveur serveur ;
	
	public ServeurAsync(Serveur serveur){
		this.serveur= serveur ;
	}

    protected String doInBackground(ArrayList<String>...params){
    	HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 20000);
		HttpResponse response;
		
		// Création du tableau de données à envoyer en POST
		List<NameValuePair> postdata = new ArrayList<NameValuePair>();
		try {
			// On traite les arguments
			args = params[0] ;
			nomFichier = args.get(0) ;
			String URL = "http://jules-vanneste.fr/takeaphotoforme/" + nomFichier;
			for (int i = 1; i < args.size(); i++) {
				String key = args.get(i).substring(0, args.get(i).indexOf("="));
				String value = args.get(i).substring(args.get(i).indexOf("=") + 1, args.get(i).length());
				System.out.println("key = " + key + " value = " + value);
				postdata.add(new BasicNameValuePair(key, value));
			}

			// On envoit les données en POST au serveur
			HttpPost post = new HttpPost(URL);
			post.setEntity(new UrlEncodedFormEntity(postdata));
			response = client.execute(post);
			System.out.println("client excute poste => " + response);
			
			// On prépare la réponse du serveur
			InputStream inputStream = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null){
				System.out.println("line = " + line);
				sb.append(line + "\n");
			}
			
			// On récupère la réponse
			response.getEntity().consumeContent();
			String json = sb.toString();
			System.out.println("json = " + json);
			JSONObject jsonObj = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1));
			// On verifie si il n'y a pas d'erreur
			String result = jsonObj.getString("result");
			
			if (result.contains("TRUE")) {
				// Pour les demandes on va remplir le tableau resultArray
				if (nomFichier.equals("my_demandes.php") || nomFichier.equals("get_demandes_with_latlng.php") || nomFichier.equals("get_demandes_except_user.php") || nomFichier.equals("get_demande.php")) {
					remplirResultArray("demande", jsonObj);
				} else if (nomFichier.equals("get_photos.php")) {
					remplirResultArray("url", jsonObj);
				} else if (nomFichier.equals("del_demande.php") || nomFichier.equals("del_reponse.php")  || nomFichier.equals("update_demande.php") || nomFichier.equals("update_etat_demande.php") || nomFichier.equals("update_position_demande.php")){
					addInResultArray("result", "TRUE") ;
				} else if(nomFichier.equals("get_demandes.php")) {
					remplirResultArray("demandes", jsonObj);
				} else if(nomFichier.equals("get_url_photo_demande.php")) {
					Log.i("Ajout demande", "k");
					Log.i("Ajout demande", jsonObj.toString());
					remplirResultArray("reponses", jsonObj);
				}
				else{
					addInResultArray("id", jsonObj.getString("id"));
				}
			} else {
				addInResultArray("message", jsonObj.getString("message"));
			}
			serveur.setResult(resultArray);
			serveur.setRunning(false);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
    }
    
    protected void onPostExecute(String s) {
        Log.i("onPostExecute", s);
    }
    
	private void addInResultArray(String key, Object value) {
		resultArray = new HashMap<String, Object>();
		resultArray.put(key, value);
	}
    
    private void remplirResultArray(String key, JSONObject jsonObj)	throws JSONException {
    	JSONArray jArray =null ;
    	
    	if(jsonObj.getJSONArray(key) != null)
    		 jArray = jsonObj.getJSONArray(key);
    	
		resultArray = new HashMap<String, Object>();
		
		if (jArray != null) {
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject oneObject = jArray.getJSONObject(i);

				if (key.contains("url")) {
					String url = oneObject.getString("url");
					resultArray.put(String.valueOf(i), url);
				} else if (key.contains("demande")) {
					String id_demande = oneObject.getString("id_demande");
					String id_user = oneObject.getString("id_user");
					String latitude = oneObject.getString("latitude");
					String longitude = oneObject.getString("longitude");
					String description = oneObject.getString("description");
					String etat = oneObject.getString("etat");
				
					Demande demande = new Demande(id_user, Double.parseDouble(latitude), Double.parseDouble(longitude), description);
					demande.setId(Integer.parseInt(id_demande));
					demande.setEtat(Integer.parseInt(etat));
					resultArray.put(String.valueOf(i), demande);
				}
				else if (key.contains("reponses")) {
					int id_reponse = oneObject.getInt("id_reponse");
					String url = oneObject.getString("url");
					int id_demande = oneObject.getInt("id_demande");
				
					Reponse reponse = new Reponse(id_reponse, url, id_demande);
					resultArray.put(String.valueOf(i), reponse);
				}
			}
		}
	}
}