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
import com.takeaphoto.utils.MCrypt;

class ServeurAsync extends AsyncTask<ArrayList<String>, Void, String> {

    //public ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
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
		
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 1000);
		HttpResponse response;
		
		// Cr�ation du tableau de donn�e a envoyer en POST
		List<NameValuePair> postdata = new ArrayList<NameValuePair>();

		try {
			// On traite les arguments
			args = params[0] ;
			nomFichier = args.get(0) ;
			String URL = "http://jules-vanneste.fr/takeaphotoforme/"
					+ nomFichier;
			
			for (int i = 1; i < args.size(); i++) {
				String key = args.get(i).substring(0,
						args.get(i).indexOf("="));
				String value = args.get(i).substring(
						args.get(i).indexOf("=") + 1, args.get(i).length());
				
				/* On encrypte le mot de passe */
				if (key.equalsIgnoreCase("pass")) value = MCrypt.bytesToHex( SecuritySingleton.encrypt(value));
				
				postdata.add(new BasicNameValuePair(key, value));
			}

			// On envoit les donn�es en POST au serveur
			HttpPost post = new HttpPost(URL);
			post.setEntity(new UrlEncodedFormEntity(postdata));
			System.out.println(response = client.execute(post));

			// On pr�pare la r�ception de la r�ponse du serveur
			InputStream inputStream = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(inputStream, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null)
				sb.append(line + "\n");

			// On r�cup�re la r�ponse
			response.getEntity().consumeContent();
			String json = sb.toString();

			JSONObject jsonObj = new JSONObject(json.substring(
					json.indexOf("{"), json.lastIndexOf("}") + 1));

			// On verifie si il n'y a pas d'erreur
			String result = jsonObj.getString("result");

			if (result.contains("TRUE")) {
				// Pour les demandes ont va remplir le tableau resultArray
				if (nomFichier.equals("my_demandes.php") || nomFichier.equals("get_demandes_except_user.php") || nomFichier.equals("get_demande.php")) {
					remplirResultArray("demande", jsonObj);
				} else if (nomFichier.equals("get_photos.php")) {
					remplirResultArray("url", jsonObj);
				} else if (nomFichier.equals("del_demande.php") || nomFichier.equals("update_demande.php") ){
					addInResultArray("result", "TRUE") ;
				}
				else
					addInResultArray("id", jsonObj.getString("id"));
			} else {
				addInResultArray("message", jsonObj.getString("message"));
			}
			
			serveur.setResult(resultArray) ;
			serveur.setRunning(false) ;
			
		} catch (Exception e) {
			e.printStackTrace();
			// showDialog("Error", "Cannot Estabilish Connection");
		}
		
		return "";
    }
    
    protected void onPostExecute(String s) {
       // this.progressDialog.dismiss();
        Log.i("onPostExecute", s);
    }
    
	private void addInResultArray(String key, Object value) {
		resultArray = new HashMap<String, Object>();
		resultArray.put(key, value);
	}
    
    private void remplirResultArray(String key, JSONObject jsonObj)
			throws JSONException {
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
				
					Demande demande = new Demande(Integer.parseInt(id_user),
							Double.parseDouble(latitude),
							Double.parseDouble(longitude), description);
					demande.setId(Integer.parseInt(id_demande));
					demande.setEtat(Integer.parseInt(etat));
					resultArray.put(String.valueOf(i), demande);
				}
			}
		}
	}
}