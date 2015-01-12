package com.takeaphoto.flickr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.people.User;
import com.takeaphoto.activity.FlickrActivity;

public class LoadUserTask extends AsyncTask<OAuth, Void, User> {
        /**
         *
         */
        private final FlickrActivity flickrjAndroidSampleActivity;
        private ImageView userIconImage;
        private final Logger logger = LoggerFactory.getLogger(LoadUserTask.class);
        
        /**
         * The progress dialog before going to the browser.
         */
        private ProgressDialog mProgressDialog;
       
        public LoadUserTask(FlickrActivity flickrjAndroidSampleActivity, ImageView userIconImage) {
                this.flickrjAndroidSampleActivity = flickrjAndroidSampleActivity;
                this.userIconImage = userIconImage;
        }
              
        @Override
        protected void onPreExecute() {
                super.onPreExecute();
                Log.i("LoadUserTask onPreExecute", "debut");
           /*     mProgressDialog = ProgressDialog.show(flickrjAndroidSampleActivity,"", "Loading user information..."); //$NON-NLS-1$ //$NON-NLS-2$
                mProgressDialog.setCanceledOnTouchOutside(true);
                mProgressDialog.setCancelable(true);
                mProgressDialog.setOnCancelListener(new OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dlg) {
                                LoadUserTask.this.cancel(true);
                        }
                });
            */    Log.i("LoadUserTask onPreExecute", "fin");
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected User doInBackground(OAuth... params) {
        		Log.i("LoadUserTask doInBackground", "debut");
                OAuth oauth = params[0];
                User user = oauth.getUser();
                OAuthToken token = oauth.getToken();
                try {
                		Log.i("LoadUserTask doInBackground", "try");
                        Flickr f = FlickrHelper.getInstance().getFlickrAuthed(token.getOauthToken(), token.getOauthTokenSecret());
                        Log.i("LoadUserTask doInBackground", "try before return");
                        return f.getPeopleInterface().getInfo(user.getId());
                } catch (Exception e) {
                //        Toast.makeText(flickrjAndroidSampleActivity, e.toString(), Toast.LENGTH_LONG).show();
                        logger.error(e.getLocalizedMessage(), e);
                }
                Log.i("LoadUserTask doInBackground", "fin");
                
                return null;
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(User user) {
        		Log.i("LoadUserTask onPostExecute", "debut");
           /*     if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                }
             */   if (user == null) {
                        return;
                }
           /*     if (user.getBuddyIconUrl() != null) {
                    String buddyIconUrl = user.getBuddyIconUrl();
	                if (userIconImage != null) {
	                    ImageDownloadTask task = new ImageDownloadTask(userIconImage);
	                    Drawable drawable = new DownloadedDrawable(task);
	                    userIconImage.setImageDrawable(drawable);
	                    task.execute(buddyIconUrl);
	                }
                }
          */      Log.i("LoadUserTask onPostExecute", "fin");
        }
       
       
}