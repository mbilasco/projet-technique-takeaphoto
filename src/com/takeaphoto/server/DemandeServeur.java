package com.takeaphoto.server;

import java.util.ArrayList;

import android.content.Context;

import com.takeaphoto.database.DemandeBDD;
import com.takeaphoto.model.Demande;
import com.takeaphoto.model.User;

public class DemandeServeur extends Serveur{
	static DemandeBDD demandeBdd = null ;
	
	private void demandeBddIsSet(Context context){
		if(demandeBdd == null)
			demandeBdd = new DemandeBDD(context) ;
	}
	
	public String addDemande(Context context, User currentUser, Demande demande) {
		demandeBddIsSet(context) ;
		String resultTmp = null ;
		
		ArrayList<String> args = new ArrayList<String>() ;
		args.add("ajout_demande.php") ;
		args.add("login="+currentUser.getLogin());
		args.add("pass="+currentUser.getPass()) ;
		args.add("latitude="+demande.getLat()) ;
		args.add("longitude="+demande.getLng()) ;
		args.add("description="+demande.getDescription()) ;
		
		sendJson( args);
		while(isRunning()){}
		
		if(getResultArray() != null){
			if(getResultArray().containsKey("id")){
				int id = Integer.parseInt((String)getResultArray().get("id")) ;
				
				setResultToFalse() ;
				setRunning(true);
				if(updateDemandeLocal(context, currentUser, id))
					resultTmp = "ok" ;
				else
					resultTmp = "Erreur en local" ;
			}else
				resultTmp = (String)getResultArray().get("message") ;
		}else
			resultTmp = "Erreur" ;
		
		setResultToFalse() ;
		
		return resultTmp ;
		
	}
	
	public Demande getLocalDemandeWithId(Context context, int id_demande){
		Demande demande = null ;
		demandeBddIsSet(context) ;
		
		demandeBdd.open();
		demande = demandeBdd.getDemandeWithId(id_demande) ;
		demandeBdd.close() ;
		
		return demande ;
	}
	
	public ArrayList<Demande> getMyDemandesLocal(Context context, User currentUser){
		demandeBddIsSet(context) ;
		ArrayList<Demande> resultTmp = null ;
		
		demandeBdd.open() ;
        resultTmp = demandeBdd.getDemandeWithIdUser(currentUser.getId()) ;
        demandeBdd.close() ;
		
		return resultTmp ;
	}

	public ArrayList<Demande> getDemandesOthers(Context context, User currentUser) {
		demandeBddIsSet(context) ;
		ArrayList<Demande> resultTmp = new ArrayList<Demande>() ;
		
		demandeBdd.open() ;
        demandeBdd.removeDemandeWithoutIdUser(currentUser.getId()) ; 
        demandeBdd.close() ;
        ArrayList<String> args = new ArrayList<String>() ;
        args.add("get_demandes_except_user.php") ;
		args.add("login="+currentUser.getLogin());
		args.add("pass="+currentUser.getPass()) ;
		
		sendJson(args);
		while(isRunning()){}
		
    	if(getResultArray() != null){
	    	for (String mapKey : getResultArray().keySet()) {
	    		if(!mapKey.contains("result") && !mapKey.contains("id"))
	    			resultTmp.add((Demande) getResultArray().get(mapKey)) ;
	    	}
    	}else{
    		demandeBdd.open() ;
    		resultTmp = demandeBdd.getDemandeWithoutId(currentUser.getId()) ; 
    		demandeBdd.close() ;
    	}
	    	
	    setResultToFalse() ;
    	
		return (resultTmp.size() == 0 ) ? null : resultTmp  ;
	}
	
	public String updateDemande(Context context, User currentUser, int id_demande, String name, String value ){
		demandeBddIsSet(context) ;
		String resultTmp = null ;
		
		ArrayList<String> args = new ArrayList<String>() ;
		args.add("update_demande.php") ;
		args.add("login="+currentUser.getLogin());
		args.add("pass="+currentUser.getPass()) ;
		args.add("id_demande=" + id_demande) ;
		args.add(name+"="+value) ;
		
		sendJson(args);
		while(isRunning()){}
		
    	if(getResultArray() != null){
			if(getResultArray().containsKey("result") && getResultArray().get("result").toString().contains("TRUE")){
				setResultToFalse() ;
				setRunning(true) ;
					if(updateDemandeLocal(context, currentUser, id_demande))
						resultTmp = "Votre demande a été mise à jour" ;
					else
						resultTmp = "Erreur en local" ;
			}else
				resultTmp = "Erreur distante" ;
		}
		
    	setResultToFalse() ;
    	
		return resultTmp ;
	}
	
	public Boolean updateDemandeLocal(Context context, User currentUser, int id_demande){
		demandeBddIsSet(context) ;
		Boolean resultTmp = false ;
		
		 ArrayList<String> args = new ArrayList<String>() ;
		 args.add("get_demande.php") ;
		 args.add("login="+currentUser.getLogin());
		 args.add("pass="+currentUser.getPass()) ;
		 args.add("id_demande=" + id_demande) ;
		 
		 sendJson(args);
		 while(isRunning()){}
	     if(getResultArray() != null){
			 Demande demande = (Demande)getResultArray().get("0") ;
			 if(demande != null){
				 demandeBdd.open() ;
				 if(demandeBdd.getDemandeWithId(demande.getId()) != null )
					 demandeBdd.removeDemandeWithID(id_demande) ;
				
				 demandeBdd.insertDemande(demande) ;
				 demandeBdd.close() ;
				 resultTmp = true;
			 }
		 }
		 
		 setResultToFalse() ;
		 return resultTmp ;
		 
	}
	
	public void updateMyDemandesLocal(Context context, User currentUser){
		demandeBddIsSet(context) ;
		
		demandeBdd.open() ;
		demandeBdd.clear() ;
		demandeBdd.close() ;
		
		ArrayList<String> args = new ArrayList<String>() ;
		args.add("my_demandes.php") ;
		args.add("login="+currentUser.getLogin());
		args.add("pass="+currentUser.getPass()) ;
		
		sendJson(args);
		while(isRunning()){}
			
	    if(getResultArray() != null){
			demandeBdd.open() ;
			for (String mapKey : getResultArray().keySet()) {
				if(!mapKey.contains("result") && !mapKey.contains("id"))
					if(demandeBdd.getDemandeWithId(((Demande)getResultArray().get(mapKey)).getId()) == null)
						demandeBdd.insertDemande((Demande)getResultArray().get(mapKey)) ;
	    	}
			demandeBdd.close() ;
		}
		
		setResultToFalse() ;
	}

	public String removeDemande(Context context, User currentUser, int id_demande){
		demandeBddIsSet(context) ;
		String resultTmp = null ;
		
    	ArrayList<String> args = new ArrayList<String>() ;
    	args.add("del_demande.php") ;
		args.add("login="+currentUser.getLogin());
		args.add("pass="+currentUser.getPass()) ;
		args.add("id_demande=" + id_demande) ;
		
		sendJson(args);
		while(isRunning()){}
	    if(getResultArray() != null){
	    	if(getResultArray().containsKey("result") && getResultArray().get("result").toString().contains("TRUE")){
					demandeBdd.open() ;
		    		demandeBdd.removeDemandeWithID(id_demande) ;
		    		demandeBdd.close() ;
						resultTmp = "Votre demande a été supprimée" ;
			}else
				resultTmp = (String)getResultArray().get("message") ;
		}else
			resultTmp = "Erreur" ;
		
		setResultToFalse() ;
		
		return resultTmp ;
	}
}
