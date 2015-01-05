package com.takeaphoto.server;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.util.Log;

import com.takeaphoto.model.User;
import com.takeaphoto.utils.MCrypt;

public class PhotoServeur extends Serveur {

	public int uploadFile(String sourceFilePath, User currentUser, int id_demande) throws Exception {
		int serverResponseCode = 0;
        String upLoadServerUri = "http://jules-vanneste.fr/takeaphotoforme/upload_media.php";

        String params = "?login="+ currentUser.getLogin() + "&pass=" +  MCrypt.bytesToHex( new MCrypt().encrypt(currentUser.getPass())) + "&id_demande=" + id_demande;
		upLoadServerUri += params ;
		System.out.println(upLoadServerUri);
        
        HttpURLConnection conn = null;
        DataOutputStream dos = null;  
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024; 
        File sourceFile = new File(sourceFilePath); 
        File f = new File("");
        if (!sourceFile.isFile()) {
         Log.e("uploadFile", "Source File Does not exist : " + sourceFilePath + ", path :" + f.getAbsolutePath());
         return 0;
        }
            try { // open a URL connection to the Servlet
             FileInputStream fileInputStream = new FileInputStream(sourceFile);
             URL url = new URL(upLoadServerUri);
             conn = (HttpURLConnection) url.openConnection(); // Open a HTTP  connection to  the URL
             conn.setDoInput(true); // Allow Inputs
             conn.setDoOutput(true); // Allow Outputs
             conn.setUseCaches(false); // Don't use a Cached Copy
             conn.setRequestMethod("POST");
             conn.setRequestProperty("Connection", "Keep-Alive");
             conn.setRequestProperty("ENCTYPE", "multipart/form-data");
             conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
             conn.setRequestProperty("uploaded_file", sourceFilePath); 
             dos = new DataOutputStream(conn.getOutputStream());
   
             dos.writeBytes(twoHyphens + boundary + lineEnd); 
             dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""+ sourceFilePath + "\"" + lineEnd);
             dos.writeBytes(lineEnd);
   
             bytesAvailable = fileInputStream.available(); // create a buffer of  maximum size
   
             bufferSize = Math.min(bytesAvailable, maxBufferSize);
             buffer = new byte[bufferSize];
   
             // read file and write it into form...
             bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
               
             while (bytesRead > 0) {
               dos.write(buffer, 0, bufferSize);
               bytesAvailable = fileInputStream.available();
               bufferSize = Math.min(bytesAvailable, maxBufferSize);
               bytesRead = fileInputStream.read(buffer, 0, bufferSize);               
              }
   
             // send multipart form data necesssary after file data...
             dos.writeBytes(lineEnd);
             dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
   
             // Responses from the server (code and message)
             serverResponseCode = conn.getResponseCode();
             String serverResponseMessage = conn.getResponseMessage();
              
             Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
             //close the streams //
             fileInputStream.close();
             dos.flush();
             dos.close();
              
        } catch (MalformedURLException ex) {    
            ex.printStackTrace();
            //Toast.makeText(PhotoActivity.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
            Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(PhotoActivity.this, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Upload file to server Exception", "Exception : " + e.getMessage(), e);  
        }      
        return serverResponseCode;  
       }
	
	public  ArrayList<Object> getUrls(User currentUser, int id_demande){

		ArrayList<Object> resultTmp = null ;
		
        ArrayList<String> args = new ArrayList<String>() ;
        args.add("get_photos.php") ;
		args.add("login="+currentUser.getLogin());
		args.add("pass="+currentUser.getPass()) ;
		args.add("id_demande="+id_demande);
		
		sendJson(args);
		while(isRunning()){}
		
    	if(getResultArray() != null){
    		if(getResultArray() != null){
    			resultTmp = new ArrayList<Object>() ;
    	    	for (String mapKey : getResultArray().keySet()) {
    	    		resultTmp.add(getResultArray().get(mapKey)) ;
    	    	}
    		}
    	}

		return resultTmp ;
	}
}
