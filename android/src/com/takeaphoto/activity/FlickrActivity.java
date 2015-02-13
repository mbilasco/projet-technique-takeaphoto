package com.takeaphoto.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.people.User;
import com.takeaphoto.flickr.GetOAuthTokenTask;
import com.takeaphoto.flickr.LoadUserTask;
import com.takeaphoto.flickr.OAuthTask;

/**
 * Activité permettant l'identification des utilisateurs
 * @author Maxime & Jules
 *
 */
public class FlickrActivity extends Activity {
        public static final String CALLBACK_SCHEME = "flickrj-android-sample-oauth";
        public static final String PREFS_NAME = "flickrj-android-sample-pref";
        public static final String KEY_OAUTH_TOKEN = "flickrj-android-oauthToken";
        public static final String KEY_TOKEN_SECRET = "flickrj-android-tokenSecret";
        public static final String KEY_USER_NAME = "flickrj-android-userName";
        public static final String KEY_USER_ID = "flickrj-android-userId";
        public static final String KEY_TOKEN_VERIF = "flickrj-android-tokenVerif";
       
        private static final Logger logger = LoggerFactory.getLogger(FlickrActivity.class);
       
        private ImageView oauthYahoo;
        
        private User user;
        
        private Context mContext;
        public OAuth oauth = null;
                        
        @Override
        /**
         * Connexion à l'application via OAuth Yahoo
         */
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login_flickr);
            this.mContext = this;
            
            oauthYahoo = (ImageView) this.findViewById(R.id.imageView1);
           	oauthYahoo.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {            		
                	oauth = getOAuthToken();
                    if (oauth == null || oauth.getUser() == null) {
                    	OAuthTask task = new OAuthTask(mContext);
                    	task.execute();
                    } else {
                    	load(oauth);
                    	launchActivity();
                    }                        
                }
            });
           	
            oauth = getOAuthToken();
    	}
        
        /**
         * Redirige utilisateur vers espace de connexion Flickr Yahoo
         * @param oauth
         */
        private void load(OAuth oauth) {
            if (oauth != null) {
                new LoadUserTask(this, oauthYahoo).execute(oauth);
            }
        }
        
        /**
         * Ouverture de l'application suite à la connexion
         */
        public void launchActivity(){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            Log.i("launch oauth", oauth + "");
            Log.i("launch user", user +"");
            intent.putExtra("USER_ID", user.getId());
            intent.putExtra("USER_NAME", user.getUsername());
            intent.putExtra("OAUTH", oauth);
    		
			startActivity(intent); 
        }
   
	    @Override
	    public void onDestroy() {
	        super.onDestroy();
	    }
	    
        @Override
        protected void onNewIntent(Intent intent) {
        	setIntent(intent);
        }
        
        public ImageView getUserIconImageView() {
        	return oauthYahoo;
        }

        @Override
        public void onResume() {
            super.onResume();
            Intent intent = getIntent();
            String scheme = intent.getScheme();
            OAuth savedToken = getOAuthToken();
            
            if (CALLBACK_SCHEME.equals(scheme) && (savedToken == null || savedToken.getUser() == null)) {
                Uri uri = intent.getData();
                String query = uri.getQuery();
                String[] data = query.split("&");
                if (data != null && data.length == 2) {
                    String oauthToken = data[0].substring(data[0].indexOf("=") + 1);
                    String oauthVerifier = data[1].substring(data[1].indexOf("=") + 1);
                    oauth = getOAuthToken();
                    if (oauth != null && oauth.getToken() != null && oauth.getToken().getOauthTokenSecret() != null) {
                        GetOAuthTokenTask task = new GetOAuthTokenTask(this);
                        task.execute(oauthToken, oauth.getToken().getOauthTokenSecret(), oauthVerifier);
                    }
                }
            }
        }
           
	    public void onOAuthDone(OAuth result) {
	        if (result == null) {
	        	Toast.makeText(this,"Authorization failed", Toast.LENGTH_LONG).show();
	        } else {
	            user = result.getUser();
	            OAuthToken token = result.getToken();
	            if (user == null || user.getId() == null || token == null || token.getOauthToken() == null || token.getOauthTokenSecret() == null) {
                    Toast.makeText(this,"Authorization failed", Toast.LENGTH_LONG).show();
                    return;
	            }
	            saveOAuthToken(user.getUsername(), user.getId(), token.getOauthToken(), token.getOauthTokenSecret());
	            load(result);
	            launchActivity();
	        }
	    }
	   
	    public OAuth getOAuthToken() {
	        SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
	        String oauthTokenString = settings.getString(KEY_OAUTH_TOKEN, null);
	        String tokenSecret = settings.getString(KEY_TOKEN_SECRET, null);
	        if (oauthTokenString == null && tokenSecret == null) {
                logger.warn("No oauth token retrieved");
                return null;
	        }
	        oauth = new OAuth();
	        String userName = settings.getString(KEY_USER_NAME, null);
	        String userId = settings.getString(KEY_USER_ID, null);
	        if (userId != null) {
                user = new User();
                user.setUsername(userName);
                user.setId(userId);
                oauth.setUser(user);
	        }
	        OAuthToken oauthToken = new OAuthToken();
	        oauth.setToken(oauthToken);
	        oauthToken.setOauthToken(oauthTokenString);
	        oauthToken.setOauthTokenSecret(tokenSecret);
	        logger.debug("Retrieved token from preference store: oauth token={}, and token secret={}", oauthTokenString, tokenSecret);
	        return oauth;
	    }
	   
	    public void saveOAuthToken(String userName, String userId, String token, String tokenSecret) {
	    	Log.i("getOAuthToken", "getOAuthToken");
	        logger.debug("Saving userName=" + userName +", userId= " + userId + ", oauth token= " + token + " and token secret= " + tokenSecret);
	        SharedPreferences sp = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
	        Editor editor = sp.edit();
	        editor.putString(KEY_OAUTH_TOKEN, token);
	        editor.putString(KEY_TOKEN_SECRET, tokenSecret);
	        editor.putString(KEY_USER_NAME, userName);
	        editor.putString(KEY_USER_ID, userId);
	        editor.commit();
	    }
}