package com.example.getpost;
 
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
 
public class GetAndPost extends Activity {
 
    TextView tv;
    String text;
    private Context context;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this.getLayoutInflater().getContext();
        tv  = (TextView)findViewById(R.id.textview);
        text    = "";
        

        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://jeremiesamson-portfolio.com/wp-content/uploads/takeaphotoforme/post.php");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("id", "9999"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            
            int responseCode = response.getStatusLine().getStatusCode();
            switch(responseCode) {
            case 200:
            HttpEntity entity = response.getEntity();
                if(entity != null) {
                    String responseBody = EntityUtils.toString(entity);
                    message(responseBody);
                }
                break;
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }

    }
    
    public void message(String message) {
		Toast toast = Toast.makeText(context, message, 4);
		toast.show();
	}
}
