package com.takeaphoto.activity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.takeaphoto.flickr.ImageDownloadTask;
import com.takeaphoto.server.DemandeServeur;
 
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Fragment permettant à l'utilisateur de visualiser une photo
 * @author Maxime & Jules
 *
 */
public class VisualisationPhoto extends FragmentActivity {
    private ImageView photo;
    
    private String url = null;
    private String id_reponse = null;
    
    private Button enregistrer;
    private Button rejetter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        
        url = this.getIntent().getStringExtra("URL_PHOTO");
        id_reponse = this.getIntent().getStringExtra("ID_REPONSE");
        photo = (ImageView) findViewById(R.id.imageView1);
        
        ImageDownloadTask img = new ImageDownloadTask(photo);
        img.execute(url);
        
        enregistrer = (Button) findViewById(R.id.button_accepter);
        rejetter = (Button) findViewById(R.id.button_refuser);
        
        // Action pour enregistrer une photo sur son téléphone
        enregistrer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {            	
            	AlertDialog.Builder boite = new AlertDialog.Builder(VisualisationPhoto.this);
            	boite.setMessage("Voulez-vous enregistrer cette photo ?");
            	boite.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Date now = new Date();
                        SimpleDateFormat dateFormat = null;
                		dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
                        String format = "IMG_" + dateFormat.format(now);
                      
                    	Bitmap bitmap =((BitmapDrawable)photo.getDrawable()).getBitmap();
                    	
                        insertImage(getContentResolver(), bitmap, format , "Image from Take a Photo");
                    	
                        Toast.makeText(getApplicationContext(), "Enregistrement de la photo", Toast.LENGTH_SHORT).show();
                    }
                });
            	boite.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    	 
                    }
             	});
            	boite.show();
            }
        });   
        
        // Action pour rejetter une photo prise par un autre utilisateur
        rejetter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	AlertDialog.Builder boite = new AlertDialog.Builder(VisualisationPhoto.this);
            	boite.setMessage("Voulez-vous supprimer cette photo ?");
            	boite.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    	DemandeServeur demande = new DemandeServeur();
                        demande.removeReponse(getApplicationContext(), Integer.parseInt(id_reponse));
                        finish();
                    }
                });
            	boite.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    	 
                    }
             	});
            	boite.show();               
            }
        });
    }
    
	public static final String insertImage(ContentResolver cr, Bitmap source,String title, String description) {
		ContentValues values = new ContentValues();
		values.put(Images.Media.TITLE, title);
		values.put(Images.Media.DISPLAY_NAME, title);
		values.put(Images.Media.DESCRIPTION, description);
		values.put(Images.Media.MIME_TYPE, "image/jpeg");
		// Add the date meta data to ensure the image is added at the front of the gallery
		values.put(Images.Media.DATE_ADDED, System.currentTimeMillis());
		values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis());

		Uri url = null;
		String stringUrl = null; /* value to be returned */

		try {
			url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

			if (source != null) {
				OutputStream imageOut = cr.openOutputStream(url);
				try {
					source.compress(Bitmap.CompressFormat.JPEG, 50, imageOut);
				} finally {
					imageOut.close();
				}

				long id = ContentUris.parseId(url);
				// Wait until MINI_KIND thumbnail is generated.
				Bitmap miniThumb = Images.Thumbnails.getThumbnail(cr, id, Images.Thumbnails.MINI_KIND, null);
				// This is for backward compatibility.
				storeThumbnail(cr, miniThumb, id, 50F, 50F,Images.Thumbnails.MICRO_KIND);
			} else {
				cr.delete(url, null, null);
				url = null;
			}
		} catch (Exception e) {
			if (url != null) {
				cr.delete(url, null, null);
				url = null;
			}
		}

		if (url != null) {
			stringUrl = url.toString();
		}

		return stringUrl;
	} 
	
	private static final Bitmap storeThumbnail(ContentResolver cr,Bitmap source,long id,float width,float height,int kind) {
		// create the matrix to scale it
		Matrix matrix = new Matrix();

		float scaleX = width / source.getWidth();
		float scaleY = height / source.getHeight();

		matrix.setScale(scaleX, scaleY);

		Bitmap thumb = Bitmap.createBitmap(source, 0, 0,source.getWidth(),source.getHeight(),matrix,true);

		ContentValues values = new ContentValues(4);
		values.put(Images.Thumbnails.KIND,kind);
		values.put(Images.Thumbnails.IMAGE_ID,(int)id);
		values.put(Images.Thumbnails.HEIGHT,thumb.getHeight());
		values.put(Images.Thumbnails.WIDTH,thumb.getWidth());

		Uri url = cr.insert(Images.Thumbnails.EXTERNAL_CONTENT_URI, values);

		try {
			OutputStream thumbOut = cr.openOutputStream(url);
			thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut);
			thumbOut.close();
			return thumb;
		} catch (FileNotFoundException ex) {
			return null;
		} catch (IOException ex) {
			return null;
		}
	}
}