package com.takeaphoto.flickr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.people.User;
import com.takeaphoto.activity.FlickrActivity;

public class LoadUserTask extends AsyncTask<OAuth, Void, User> {
    private final FlickrActivity flickrjAndroidSampleActivity;
    private ImageView userIconImage;
    private final Logger logger = LoggerFactory.getLogger(LoadUserTask.class);
   
    private ProgressDialog mProgressDialog;
    
    public LoadUserTask(FlickrActivity flickrjAndroidSampleActivity, ImageView userIconImage) {
        this.flickrjAndroidSampleActivity = flickrjAndroidSampleActivity;
        this.userIconImage = userIconImage;
    }
          
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = ProgressDialog.show(flickrjAndroidSampleActivity,"", "Connexion en cours...", true);
    }

    /* (non-Javadoc)
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    @Override
    protected User doInBackground(OAuth... params) {
        OAuth oauth = params[0];
        User user = oauth.getUser();
        OAuthToken token = oauth.getToken();
        try {
            Flickr f = FlickrHelper.getInstance().getFlickrAuthed(token.getOauthToken(), token.getOauthTokenSecret());
            return f.getPeopleInterface().getInfo(user.getId());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        
        return null;
    }

    /* (non-Javadoc)
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute(User user) {
		mProgressDialog.dismiss();
        if (user == null) {
                return;
        }
    }
}