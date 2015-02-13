package com.takeaphoto.server;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.takeaphoto.model.Demande;
import com.takeaphoto.model.Reponse;
import com.takeaphoto.model.User;

/**
 * Classe demandes envoyées au serveur
 * @author Maxime & Jules
 *
 */
public class DemandeServeur extends Serveur{
	
	/**
	 * Ajout d'une demande sur le serveur
	 * @param currentUser
	 * @param demande
	 * @return
	 */
	public String addDemande(User currentUser, Demande demande) {
		String resultTmp = null;
		
		ArrayList<String> args = new ArrayList<String>();
		args.add("ajout_demande.php");
		args.add("idUser="+currentUser.getUserId());
		args.add("latitude="+demande.getLat());
		args.add("longitude="+demande.getLng());
		args.add("description="+demande.getDescription());
		
		sendJson(args);
		while(isRunning()){}
		
		if(getResultArray() != null){
			if(getResultArray().containsKey("id")){
				int id = Integer.parseInt((String)getResultArray().get("id"));
				
				setResultToFalse() ;
				setRunning(true);
				if(getDemande(currentUser, id)){
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
		setResultToFalse() ;
		
		return resultTmp ;
	}
	
	/**
	 * Récupération des demandes du serveur
	 * @return
	 */
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
		 
		 return demandes;
	}
	
	/**
	 * Récupération des demandes des autres utilisateurs du serveur
	 * @param context
	 * @param currentUser
	 * @return
	 */
	public ArrayList<Demande> getDemandesOthers(Context context, User currentUser) {
		ArrayList<Demande> resultTmp = new ArrayList<Demande>() ;
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
    	}
	    	
	    setResultToFalse() ;
    	
		return (resultTmp.size() == 0 ) ? null : resultTmp;
	}
	
	/**
	 * Mise à jour d'une demande
	 * @param context
	 * @param currentUser
	 * @param id_demande
	 * @param name
	 * @param value
	 * @return
	 */
	public String updateDemande(Context context, User currentUser, int id_demande, String name, String value ){
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
					if(getDemande(currentUser, id_demande))
						resultTmp = "Votre demande a ete mise a jour" ;
					else
						resultTmp = "Erreur en local" ;
			}else
				resultTmp = "Erreur distante" ;
		}
		
    	setResultToFalse() ;
    	
		return resultTmp ;
	}
	
	/**
	 * Récupération d'une demande de l'utilisateur
	 * @param currentUser
	 * @param id_demande
	 * @return
	 */
	public Boolean getDemande(User currentUser, int id_demande){
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
				 resultTmp = true;
			 }
		 }
		 
		 setResultToFalse() ;
		 return resultTmp ;	 
	}
	
	/**
	 * Récupération des demandes de l'utilisateur
	 * @param context
	 * @param currentUser
	 */
	public void getMyDemandes(Context context, User currentUser){
		ArrayList<String> args = new ArrayList<String>() ;
		args.add("my_demandes.php") ;
		args.add("idUser="+currentUser.getUserId());
		
		sendJson(args);
		while(isRunning()){}
			
	    if(getResultArray() != null){
			for (String mapKey : getResultArray().keySet()) {
				if(!mapKey.contains("result") && !mapKey.contains("id")){	}
	    	}
		}
		
		setResultToFalse() ;
	}
	
	/**
	 * Suppression d'une demande
	 * @param context
	 * @param currentUser
	 * @param id_demande
	 * @return
	 */
	public String removeDemande(Context context, User currentUser, int id_demande){
		String resultTmp = null ;
		
    	ArrayList<String> args = new ArrayList<String>() ;
    	args.add("del_demande.php") ;
		args.add("idUser="+currentUser.getUserId());
		args.add("id_demande=" + id_demande) ;
		
		sendJson(args);
		while(isRunning()){}
		
	    if(getResultArray() != null){
	    	if(getResultArray().containsKey("result") && getResultArray().get("result").toString().contains("TRUE")){
	    		resultTmp = "Votre demande a ete supprimee" ;
			} else
				resultTmp = (String)getResultArray().get("message") ;
		}else
			resultTmp = "Erreur" ;
		
		setResultToFalse() ;
		
		return resultTmp ;
	}
	
	/**
	 * Suppression d'une réponse
	 * @param context
	 * @param id_reponse
	 * @return
	 */
	public String removeReponse(Context context, int id_reponse){
		String resultTmp = null ;
		
    	ArrayList<String> args = new ArrayList<String>() ;
    	args.add("del_reponse.php") ;
		args.add("id_reponse=" + id_reponse) ;
		
		sendJson(args);
		while(isRunning()){}
		
	    if(getResultArray() != null){
	    	if(getResultArray().containsKey("result") && getResultArray().get("result").toString().contains("TRUE")){
	    		setResultToFalse() ;
				setRunning(true);
			} else
				resultTmp = (String)getResultArray().get("message") ;
		}else
			resultTmp = "Erreur" ;
		
		setResultToFalse() ;
		
		return resultTmp ;
	}
	
	/**
	 * Ajout d'une réponse
	 * @param demande
	 * @param reponse
	 * @return
	 */
	public String addReponse(Demande demande, Reponse reponse) {
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
			} else {
				resultTmp = (String)getResultArray().get("message");
			}
		} else{
			resultTmp = "Erreur" ;
		}
		
		setResultToFalse() ;
		
		return resultTmp ;
	}
	
	/**
	 * Récupération d'une demande selon des coordonnées GPS
	 * @param lat
	 * @param lng
	 * @return
	 */
	public ArrayList<Demande> getDemandesByLatLng(String lat, String lng) {
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
    	
	    setResultToFalse();
	    
		return (resultTmp.size() == 0 ) ? null : resultTmp;
	}
	
	/**
	 * Mise à jour de l'état d'une demande
	 * @param id_demande
	 * @param etat
	 * @return
	 */
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
	
	/**
	 * Mise à jour de la position d'une demande
	 * @param id_demande
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public String updatePositionDemande(int id_demande, Double latitude, Double longitude){
		String resultTmp = null ;
		String lat = latitude+"";
		String lng = longitude+"";
		ArrayList<String> args = new ArrayList<String>() ;
		
		args.add("update_position_demande.php") ;
		args.add("id_demande=" + id_demande);
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
	
	/**
	 * Récupération de l'url de la photo d'une réponse
	 * @param id_demande
	 * @return
	 */
	public ArrayList <Reponse> getURLPhotoReponse(int id_demande){
		ArrayList <Reponse> resultTmp = new ArrayList<Reponse>() ;
		
		ArrayList<String> args = new ArrayList<String>() ;
		args.add("get_url_photo_demande.php") ;
		args.add("id_demande=" + id_demande);
		
		sendJson(args);
		while(isRunning()){}
		
    	if(getResultArray() != null){
			for (String mapKey : getResultArray().keySet()) {
				if(!mapKey.contains("result") && !mapKey.contains("reponses")){
					resultTmp.add((Reponse) getResultArray().get(mapKey));
				}
			}
		}
    	
    	return (resultTmp.size() == 0 ) ? null : resultTmp;
	}
}
