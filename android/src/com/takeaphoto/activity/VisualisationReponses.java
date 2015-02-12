package com.takeaphoto.activity;

import java.util.ArrayList;

import com.takeaphoto.flickr.ImageCache;
import com.takeaphoto.flickr.ImageDownloadTask;
import com.takeaphoto.model.Reponse;
 
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
 
public class VisualisationReponses extends FragmentActivity {
    ArrayList <Reponse> reponses;
    
    @SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reponses);
        
        reponses = (ArrayList<Reponse>) this.getIntent().getSerializableExtra("REPONSES");

        TableLayout tableLayout = (TableLayout) findViewById(R.id.table_reponses);
        Log.i("reponses size",reponses.size()+"");
        TableRow tableRow = null;
        for(int i=0; i<reponses.size(); i++){
        	if(i%2==0){
	            tableRow = new TableRow(this);
	            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
	            tableRow.setGravity(Gravity.CENTER);
        	}
            ImageView imgView = new ImageView(this);
            ImageDownloadTask img = new ImageDownloadTask(imgView);
            
            final String url = reponses.get(i).getUrl();
            String [] urlSplit = url.split("\\.");
            String urlMini = new String("");
            for(int j=0; j<urlSplit.length-2; j++){
            	urlMini += urlSplit[j] + ".";
            } 
            
            urlMini += urlSplit[urlSplit.length-2] + "_n." + urlSplit[urlSplit.length-1];
            img.execute(urlMini);
            
            android.widget.TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            lp.setMargins(10,0,10,10);
            imgView.setLayoutParams(lp);
            
            imgView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getApplicationContext(), VisualisationPhoto.class);
					intent.putExtra("URL_PHOTO", url);			
					startActivity(intent);
				}
			});
            

            tableRow.addView(imgView);
            if(i%2==0){
            	tableLayout.addView(tableRow);
            }
        }
    }
}