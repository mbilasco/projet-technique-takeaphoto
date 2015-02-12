package com.takeaphoto.flickr;

import javax.xml.parsers.ParserConfigurationException;

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.REST;
import com.googlecode.flickrjandroid.RequestContext;
import com.googlecode.flickrjandroid.interestingness.InterestingnessInterface;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.photos.PhotosInterface;

public final class FlickrHelper {
    private static FlickrHelper instance = null;
    public static final String API_KEY = "38b0e222d6bf3e5218ea2e0c3fbd16d3"; //$NON-NLS-1$
    public static final String API_SEC = "8bff90f6aaae0db8"; //$NON-NLS-1$

    public FlickrHelper() {  }

    public static FlickrHelper getInstance() {
        if (instance == null) {
                instance = new FlickrHelper();
        }

        return instance;
    }

    public Flickr getFlickr() {
        try {
            Flickr f = new Flickr(API_KEY, API_SEC, new REST());
            return f;
        } catch (ParserConfigurationException e) {
            return null;
        }
    }

    public Flickr getFlickrAuthed(String token, String secret) {
        Flickr f = getFlickr();
        RequestContext requestContext = RequestContext.getRequestContext();
        OAuth auth = new OAuth();
        auth.setToken(new OAuthToken(token, secret));
        requestContext.setOAuth(auth);
        return f;
    }

    public InterestingnessInterface getInterestingInterface() {
        Flickr f = getFlickr();
        if (f != null) {
                return f.getInterestingnessInterface();
        } else {
                return null;
        }
    }

    public PhotosInterface getPhotosInterface() {
        Flickr f = getFlickr();
        if (f != null) {
                return f.getPhotosInterface();
        } else {
                return null;
        }
    }
}