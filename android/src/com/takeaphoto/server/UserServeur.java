package com.takeaphoto.server;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;

import com.takeaphoto.database.UserBDD;
import com.takeaphoto.model.User;

public class UserServeur extends Serveur {

	static UserBDD userbdd = null;

	private void userBddIsSet(Context context) {
		if (userbdd == null)
			userbdd = new UserBDD(context);
	}

	public User getUser(Context context, int idUser) {
		userBddIsSet(context);

		User user = null;

		userbdd.open();
		user = userbdd.getUserWithId(idUser).get(0);
		userbdd.close();

		return user;
	}

	public HashMap<String, Object> authentification(Context context,
			String login, String pass) {
		userBddIsSet(context);
		// HashMap<String, Object> resultTmp = null ;

		ArrayList<String> args = new ArrayList<String>();
		args.add("authentification.php");
		args.add("login=" + login);
		args.add("pass=" + pass);

		/*
		 * JSONObject joMap = new JSONObject(); JSONArray jArray = new
		 * JSONArray(); JSONObject jo = new JSONObject(); try { jo.put("url",
		 * "authentification.php"); jo.put("login", login); jo.put("pass",
		 * pass); jArray.put(jo); joMap.put("POST", jArray);
		 * Log.i("JSONObject ", joMap.toString()); } catch (JSONException e) {
		 * // TODO Auto-generated catch block e.printStackTrace(); }
		 */

		// sendJson(joMap) ;

		// resultTmp = Serveur.sendJson("authentification.php", args);

		sendJson(args);
		while (isRunning()) {
		}

		if (getResultArray() != null) {
			if (getResultArray().containsKey("id")) {
				userbdd.open();
				userbdd.close();

				int id = Integer.parseInt((String) getResultArray().get("id"));
				User u = new User(id, login, pass);

				
				userbdd.open();
				if(userbdd.getAllUser() == null){
					long res = userbdd.insertUser(u);
				}
				else if(userbdd.getUserWithId(id) == null){
					long res = userbdd.insertUser(u);
				}
				Log.d(String.valueOf(Log.DEBUG), "DEBUT");
				for(User u1 : userbdd.getAllUser()){
					Log.d(String.valueOf(Log.DEBUG), u1.toString());
				}
				Log.d(String.valueOf(Log.DEBUG), "FIN");
				userbdd.close();
			}
		}

		HashMap<String, Object> tmpMap = getResultArray();
		setResultToFalse();
		return tmpMap;
	}

	public HashMap<String, Object> createUser(Context context, String login,
			String pass) {
		userBddIsSet(context);

		ArrayList<String> args = new ArrayList<String>();
		args.add("create_user.php");
		args.add("login=" + login);
		args.add("pass=" + pass);
		sendJson(args);
		while (isRunning()) {
		}

		HashMap<String, Object> tmpMap = getResultArray();
		setResultToFalse();
		return tmpMap;
	}

	public User getOnlyUser(Context context) {
		userBddIsSet(context);

		User user = null;

		userbdd.open();
		ArrayList<User> users = userbdd.getAllUser();
		userbdd.close();

		if (users != null && users.size() == 1)
			user = users.get(0);

		return user;
	}
}
