package com.takeaphoto.flickr;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import com.googlecode.flickrjandroid.REST;
import com.googlecode.flickrjandroid.RequestContext;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.PhotosInterface;
import com.googlecode.flickrjandroid.uploader.UploadMetaData;
import com.googlecode.flickrjandroid.uploader.Uploader;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class GetPhotoInfoTask extends AsyncTask<String,Integer, String> {
	private Context mContext;
	private OAuth oauth;
	private String photoId;
	
	public GetPhotoInfoTask(Context mContext, OAuth oauth, String photoId) {
        super();
        this.mContext = mContext;
        this.oauth = oauth;
        this.photoId = photoId;
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
			Photo photo = photoInt.getPhoto(photoId);
        	
    		Log.i("Get Photo", photo.toString());
    		Log.i("Get Photo", "GET PHOTO EFFECTUE");
			
			url = new String("https://farm"+photo.getFarm()+".staticflickr.com/"+photo.getServer()+"/"+photo.getId()+"_"+photo.getSecret()+"."+photo.getOriginalFormat());			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return url;
	}	
}
