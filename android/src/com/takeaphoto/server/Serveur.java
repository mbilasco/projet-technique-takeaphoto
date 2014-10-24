package com.takeaphoto.server ;

import java.util.ArrayList;
import java.util.HashMap;


public abstract class Serveur {
	private boolean running = true ;
	private HashMap<String, Object> resultArray  ;
	
	@SuppressWarnings("unchecked")
	public void sendJson(ArrayList<String> args){
		setResultToFalse() ;
		 new ServeurAsync(this).execute(args);	
	}
	
	public  HashMap<String, Object> getResultArray() {
		return resultArray;
	}

	public boolean isRunning() {
		return running;
	}
	
	public void setResult(HashMap<String, Object> resultArray ){
		this.resultArray = resultArray ;
	}
		
	public void setRunning(boolean b){
		running = b ;
	}
	
	public void setResultToFalse(){
		resultArray = new HashMap<String, Object>() ;
		resultArray.put("result", "FALSE") ;
	}
}
