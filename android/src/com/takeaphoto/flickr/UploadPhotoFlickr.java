package com.takeaphoto.flickr;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import com.googlecode.flickrjandroid.RequestContext;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.uploader.UploadMetaData;
import com.googlecode.flickrjandroid.uploader.Uploader;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class UploadPhotoFlickr extends AsyncTask<String,Integer, String> {
	private Context mContext;
	private OAuth oauth;
	private File photoFile;
	
	public UploadPhotoFlickr(Context mContext, OAuth oauth, File photoFile) {
        super();
        this.mContext = mContext;
        this.oauth = oauth;
        this.photoFile = photoFile;
	}
	
	@Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

	@Override
	protected String doInBackground(String... params) {
		String result = new String("debut upload");
		RequestContext.getRequestContext().setOAuth(oauth);
		try {
        	Uploader uploader = new Uploader(FlickrHelper.API_KEY, FlickrHelper.API_SEC);

        	FileInputStream f = new FileInputStream(photoFile);
        	UploadMetaData metaData = new UploadMetaData();
        	
        	metaData.setTitle("test");
        	metaData.setDescription("description");
        	metaData.setTags(new ArrayList<String>());
        	metaData.setPublicFlag(true);
        	metaData.setFriendFlag(true);
        	metaData.setFamilyFlag(true);
        	metaData.setHidden(false);
        	metaData.setSafetyLevel("normal");
        	metaData.setAsync(false); //true pour async
        	metaData.setContentType("ma photo");
        	
    		result = uploader.upload(photoFile.getName(), f, metaData);
   
    		Log.i("upload", result);
    		Log.i("upload", "UPLOAD EFFECTUE");
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		return result;
	}	
}
