package com.takeaphoto.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

//import com.takeaphoto.database.DemandesBDD;
import com.takeaphoto.model.Demande;
import com.takeaphoto.model.Reponse;
import com.takeaphoto.model.User;

public class DemandeServeur extends Serveur{
	//static DemandesBDD demandeBdd = null ;
	/*
	private void demandeBddIsSet(Context context){
		if(demandeBdd == null)
			demandeBdd = new DemandesBDD(context);
	}
	*/
	public String addDemande(User currentUser, Demande demande) {
		//demandeBddIsSet(context);
		String resultTmp = null;
		
		ArrayList<String> args = new ArrayList<String>();
		args.add("ajout_demande.php");
		args.add("idUser="+currentUser.getUserId());
		args.add("latitude="+demande.getLat());
		args.add("longitude="+demande.getLng());
		args.add("description="+demande.getDescription());
		Log.i("Ajout demande", "1");
		sendJson(args);
		Log.i("Ajout demande", "2");
		while(isRunning()){}
		Log.i("Ajout demande", "3");
		if(getResultArray() != null){
			if(getResultArray().containsKey("id")){
				int id = Integer.parseInt((String)getResultArray().get("id"));
				
				setResultToFalse() ;
				setRunning(true);
				if(updateDemandeLocal(currentUser, id)){
					resultTmp = "ok";
				}
				else{
					resultTmp = "Erreur en local";
				}	
			} else {
				resultTmp = (String)getResultArray().get("message");
			}
		} else{
			resultTmp = "Erreur" ;
		}
		Log.i("Ajout demande", "4");
		setResultToFalse() ;
		
		return resultTmp ;
		
	}
	
	public ArrayList<Demande> getDemandes(){
		 ArrayList<Demande> demandes = new ArrayList<Demande>();
		
		 ArrayList<String> args = new ArrayList<String>() ;
		 args.add("get_demandes.php") ;
		 
		 sendJson(args);
		 while(isRunning()){}
		 
		 if(getResultArray() != null){
			 for (String mapKey : getResultArray().keySet()) {
				if(!mapKey.contains("result")){
					demandes.add((Demande) getResultArray().get(mapKey));
				}
			 }
		 }
		 
		 //setResultToFalse() ;
		 return demandes;
	}
	
	/*
	public Demande getLocalDemandeWithId(Context context, int id_demande){
		Demande demande = null ;
		demandeBddIsSet(context) ;
		
		demandeBdd.open();
		demande = demandeBdd.getDemandeWithId(id_demande).get(0);
		demandeBdd.close() ;
		
		return demande ;
	}
	*/
	/*
	public ArrayList<Demande> getMyDemandesLocal(Context context, User currentUser){
		demandeBddIsSet(context);
		ArrayList<Demande> resultTmp = null;
		
		demandeBdd.open();
        resultTmp = demandeBdd.getDemandeWithIdUser(currentUser.getUserId());
        demandeBdd.close();
		
		return resultTmp;
	}
	*/
	
	public ArrayList<Demande> getDemandesOthers(Context context, User currentUser) {
		//demandeBddIsSet(context) ;
		ArrayList<Demande> resultTmp = new ArrayList<Demande>() ;
		/*
		demandeBdd.open() ;
        demandeBdd.removeDemandeWithIdUser(currentUser.getUserId()) ; 
        demandeBdd.close() ;
        */
        ArrayList<String> args = new ArrayList<String>() ;
        args.add("get_demandes_except_user.php") ;
		args.add("idUser="+currentUser.getUserId());
		
		sendJson(args);
		while(isRunning()){}
		
    	if(getResultArray() != null){
	    	for (String mapKey : getResultArray().keySet()) {
	    		if(!mapKey.contains("result") && !mapKey.contains("id"))
	    			resultTmp.add((Demande) getResultArray().get(mapKey)) ;
	    	}
    	}else{
    		/*
    		demandeBdd.open() ;
    		resultTmp = demandeBdd.getDemandeWithIdUser(currentUser.getUserId()) ; 
    		demandeBdd.close() ;
    		*/
    	}
	    	
	    setResultToFalse() ;
    	
		return (resultTmp.size() == 0 ) ? null : resultTmp;
	}
	
	public String updateDemande(Context context, User currentUser, int id_demande, String name, String value ){
		//demandeBddIsSet(context) ;
		String resultTmp = null ;
		
		ArrayList<String> args = new ArrayList<String>() ;
		args.add("update_demande.php") ;
		args.add("idUser="+currentUser.getUserId());
		args.add("id_demande=" + id_demande) ;
		args.add(name+"="+value) ;
		
		sendJson(args);
		while(isRunning()){}
		
    	if(getResultArray() != null){
			if(getResultArray().containsKey("result") && getResultArray().get("result").toString().contains("TRUE")){
				setResultToFalse() ;
				setRunning(true) ;
					if(updateDemandeLocal(currentUser, id_demande))
						resultTmp = "Votre demande a ete mise a jour" ;
					else
						resultTmp = "Erreur en local" ;
			}else
				resultTmp = "Erreur distante" ;
		}
		
    	setResultToFalse() ;
    	
		return resultTmp ;
	}
	
	public Boolean updateDemandeLocal(User currentUser, int id_demande){
		//demandeBddIsSet(context) ;
		Boolean resultTmp = false ;
		
		 ArrayList<String> args = new ArrayList<String>() ;
		 args.add("get_demande.php") ;
		 args.add("idUser="+currentUser.getUserId());
		 args.add("id_demande=" + id_demande) ;
		 
		 sendJson(args);
		 while(isRunning()){}
	     if(getResultArray() != null){
			 Demande demande = (Demande)getResultArray().get("0") ;
			 if(demande != null){
			/*	 demandeBdd.open() ;
				 if(demandeBdd.getDemandeWithId(demande.getId()) != null )
					 demandeBdd.removeDemandeWithID(id_demande) ;
				
				 demandeBdd.insertDemande(demande) ;
				 demandeBdd.close() ;
			*/	 
				 resultTmp = true;
			 }
		 }
		 
		 setResultToFalse() ;
		 return resultTmp ;	 
	}
	
	public void updateMyDemandesLocal(Context context, User currentUser){
		//demandeBddIsSet(context) ;
				
		ArrayList<String> args = new ArrayList<String>() ;
		args.add("my_demandes.php") ;
		args.add("idUser="+currentUser.getUserId());
		
		sendJson(args);
		while(isRunning()){}
			
	    if(getResultArray() != null){
			//demandeBdd.open() ;
			for (String mapKey : getResultArray().keySet()) {
				if(!mapKey.contains("result") && !mapKey.contains("id")){
					/*
					if(demandeBdd.getDemandeWithId(((Demande)getResultArray().get(mapKey)).getId()) == null){
						demandeBdd.insertDemande((Demande)getResultArray().get(mapKey));
					}
					*/
				}
	    	}
			//demandeBdd.close() ;
		}
		
		setResultToFalse() ;
	}

	public String removeDemande(Context context, User currentUser, int id_demande){
		//demandeBddIsSet(context) ;
		String resultTmp = null ;
		
    	ArrayList<String> args = new ArrayList<String>() ;
    	args.add("del_demande.php") ;
		args.add("idUser="+currentUser.getUserId());
		args.add("id_demande=" + id_demande) ;
		
		sendJson(args);
		while(isRunning()){}
	    if(getResultArray() != null){
	    	if(getResultArray().containsKey("result") && getResultArray().get("result").toString().contains("TRUE")){
				/*	demandeBdd.open() ;
		    		demandeBdd.removeDemandeWithID(id_demande) ;
		    		demandeBdd.close() ;
				*/		resultTmp = "Votre demande a ete supprimee" ;
			}else
				resultTmp = (String)getResultArray().get("message") ;
		}else
			resultTmp = "Erreur" ;
		
		setResultToFalse() ;
		
		return resultTmp ;
	}
	
	public void uploadMedia(Context context, User currentUser, int id_demande, File file){
		//demandeBddIsSet(context) ;
		/*
		demandeBdd.open() ;
		demandeBdd.close() ;
		*/
		ArrayList<String> args = new ArrayList<String>() ;
		args.add("upload_media.php") ;
		args.add("idUser="+currentUser.getUserId());
		args.add("id_demande="+id_demande) ;
		args.add("uploaded_file="+file);
		
		sendJson(args);
		while(isRunning()){}
			
	    if(getResultArray() != null){
			//demandeBdd.open() ;
			for (String mapKey : getResultArray().keySet()) {
				if(!mapKey.contains("result") && !mapKey.contains("id")){
				/*	if(demandeBdd.getDemandeWithId(((Demande)getResultArray().get(mapKey)).getId()) == null){
						demandeBdd.insertDemande((Demande)getResultArray().get(mapKey));
					}
				*/
				}
	    	}
//			demandeBdd.close() ;
		}
		
		setResultToFalse() ;
	}
	
	public String addReponse(Context context, Demande demande, Reponse reponse) {
		//demandeBddIsSet(context);
		String resultTmp = null;
		
		ArrayList<String> args = new ArrayList<String>();
		args.add("ajout_reponse.php");
		args.add("url="+reponse.getUrl());
		args.add("idDemande="+demande.getId());
		
		sendJson(args);
		
		while(isRunning()){}

		if(getResultArray() != null){
			if(getResultArray().containsKey("id")){
				int id = Integer.parseInt((String)getResultArray().get("id"));
				
				setResultToFalse() ;
				setRunning(true);
				/* Ne pas oublier de faire la MAJ des reponses 
				if(updateDemandeLocal(context, currentUser, id)){
					resultTmp = "ok";
				}
				else{
					resultTmp = "Erreur en local";
				}
				*/	
			} else {
				resultTmp = (String)getResultArray().get("message");
			}
		} else{
			resultTmp = "Erreur" ;
		}
		
		setResultToFalse() ;
		
		return resultTmp ;
	}
	
	public ArrayList<Demande> getDemandesByLatLng(Context context, String lat, String lng) {
		//demandeBddIsSet(context) ;
		ArrayList<Demande> resultTmp = new ArrayList<Demande>() ;

        ArrayList<String> args = new ArrayList<String>() ;
        args.add("get_demandes_with_latlng.php") ;
		args.add("lat=" + lat);
		args.add("lng=" + lng);
		
		sendJson(args);
		
		while(isRunning()){}
		
    	if(getResultArray() != null){
    		
	    	for (String mapKey : getResultArray().keySet()) {
	    		if(!mapKey.contains("result") && !mapKey.contains("demande")){
	    			resultTmp.add((Demande) getResultArray().get(mapKey));
	    		}
	    	}
    	}
    	
	    setResultToFalse() ;
    	
		return (resultTmp.size() == 0 ) ? null : resultTmp;
	}
	
	public String updateEtatDemande(int id_demande, int etat){
		String resultTmp = null ;
		
		ArrayList<String> args = new ArrayList<String>() ;
		args.add("update_etat_demande.php") ;
		args.add("id_demande=" + id_demande);
		args.add("etat=" + etat);
		
		sendJson(args);
		while(isRunning()){}
		
    	if(getResultArray() != null){
			if(getResultArray().containsKey("result") && getResultArray().get("result").toString().contains("TRUE")){
				setResultToFalse() ;
				setRunning(true) ;
				
			}else
			resultTmp = "Erreur distante" ;
		}
		
    	setResultToFalse() ;
    	
		return resultTmp ;
	}
	
	public String updatePositionDemande(int id_demande, Double latitude, Double longitude){
		String resultTmp = null ;
		String lat = latitude+"";
		String lng = longitude+"";
		ArrayList<String> args = new ArrayList<String>() ;
		args.add("update_position_demande.php") ;
		args.add("id_demande=" + id_demande);
		Log.i("update positiiiiiiion",lat + " " + lng +"");
		args.add("latitude=" + lat);
		args.add("longitude=" + lng);
		
		sendJson(args);
		while(isRunning()){}
		
    	if(getResultArray() != null){
			if(getResultArray().containsKey("result") && getResultArray().get("result").toString().contains("TRUE")){
				setResultToFalse() ;
				setRunning(true) ;
				
			}else
			resultTmp = "Erreur distante" ;
		}
		
    	setResultToFalse() ;
    	
		return resultTmp ;
	}
	
	public ArrayList <Reponse> getURLPhotoReponse(int id_demande){
		ArrayList <Reponse> resultTmp = new ArrayList<Reponse>() ;
		
		ArrayList<String> args = new ArrayList<String>() ;
		args.add("get_url_photo_demande.php") ;
		args.add("id_demande=" + id_demande);
		Log.i("yaya", "coucou");
		sendJson(args);
		while(isRunning()){}
		Log.i("yaya", "beuuuuh");
		
    	if(getResultArray() != null){
			Log.i("yaya", "beuuuuh X2");
			Log.i("yaya", getResultArray().toString());
			for (String mapKey : getResultArray().keySet()) {
				Log.i("yaya", "beuuuuh X3");
				if(!mapKey.contains("result") && !mapKey.contains("reponses")){
					resultTmp.add((Reponse) getResultArray().get(mapKey));	
					Log.i("yaya", ((Reponse) getResultArray().get(mapKey)).toString());
				}
			}
		}
		
    	//setResultToFalse() ;
    	
    	return (resultTmp.size() == 0 ) ? null : resultTmp;
	}
}
