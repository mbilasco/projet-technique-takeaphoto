package com.takeaphoto.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DemandesBDD {
	private static final int VERSION_BDD = 1;
	private static final String NOM_BDD = "takeaphoto.db";
 
	private static final String TABLE_DEMANDE = "table_demande";
	private static final String COL_ID = "ID";
	private static final int NUM_COL_ID = 0;
	private static final String COL_LOGIN = "Login";
	private static final int NUM_COL_LOGIN = 1;
	private static final String COL_LAT = "Lat" ;
	private static final int NUM_COL_LAT= 2;
	private static final String COL_LNG = "Lng" ;
	private static final int NUM_COL_LNG= 3;
	private static final String COL_DESCRIPTION = "Description";
	private static final int NUM_COL_DESCRIPTION = 4;
	private static final String COL_ETAT = "Etat";
	private static final int NUM_COL_ETAT = 5;

	private SQLiteDatabase bdd;
	private Base maBase;
 
	public DemandesBDD(Context context){
		//On créer la BDD et sa table
		maBase = new Base(context, NOM_BDD, null, VERSION_BDD);
	}
 
	public void open(){
		//on ouvre la BDD en écriture
		bdd = maBase.getWritableDatabase();
	}
 
	public void close(){
		//on ferme l'accès à la BDD
		bdd.close();
	}
 
	public SQLiteDatabase getBDD(){
		return bdd;
	}
 
	public long insertDemande(Demande demande){
		//Création d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		//on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
		values.put(COL_LOGIN, demande.getLogin()) ;
		values.put(COL_LAT, demande.getLat()) ;
		values.put(COL_LNG, demande.getLng()) ;
		values.put(COL_DESCRIPTION, demande.getDescription());
		values.put(COL_ETAT, demande.getEtat()) ;
		//on insère l'objet dans la BDD via le ContentValues
		return bdd.insert(TABLE_DEMANDE, null, values);
	}
 
	public int updateDemande(int id, Demande demande){
		//La mise à jour d'une demande dans la BDD fonctionne plus ou moins comme une insertion
		//il faut simple préciser quelle livre on doit mettre à jour grâce à l'ID
		ContentValues values = new ContentValues();
		values.put(COL_LOGIN, demande.getLogin()) ;
		values.put(COL_LAT, demande.getLat()) ;
		values.put(COL_LNG, demande.getLng()) ;
		values.put(COL_DESCRIPTION, demande.getDescription());
		values.put(COL_ETAT, demande.getEtat()) ;
		return bdd.update(TABLE_DEMANDE, values, COL_ID + " = " +id, null);
	}
 
	public int removeDemandeWithID(int id){
		//Suppression d'une demande de la BDD grâce à l'ID
		return bdd.delete(TABLE_DEMANDE, COL_ID + " = " +id, null);
	}
 
	public ArrayList<Demande> getDemandeWithLogin(String login){
		//Récupère dans un Cursor les valeur correspondant à une demande contenu dans la BDD (ici on sélectionne les demandes grace a son login)
		Cursor c = bdd.query(TABLE_DEMANDE, new String[] {COL_ID, COL_LOGIN, COL_LAT, COL_LNG, COL_DESCRIPTION, COL_ETAT}, COL_LOGIN + " LIKE \"" + login +"\"", null, null, null, null);
		return cursorToDemande(c);
	}
 
	public ArrayList<Demande> getDemandeWithoutLogin(String login){
		//Récupère dans un Cursor les valeur correspondant à une demande contenu dans la BDD (ici on sélectionne les demandes grace a son login)
		Cursor c = bdd.query(TABLE_DEMANDE, new String[] {COL_ID, COL_LOGIN, COL_LAT, COL_LNG, COL_DESCRIPTION, COL_ETAT}, COL_LOGIN + " NOT LIKE \"" + login +"\"", null, null, null, null);
		return cursorToDemande(c);
	}
	
	//Cette méthode permet de convertir un cursor en une demande
	private ArrayList<Demande> cursorToDemande(Cursor c){
		//si aucun élément n'a été retourné dans la requête, on renvoie null
		if (c.getCount() == 0)
			return null;
	
		ArrayList<Demande> demandes = new ArrayList<Demande>() ;
		
		c.moveToFirst();
		
		do{
			//On créé une demande
			Demande demande = new Demande() ;
			
			//on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
			demande.setId(c.getInt(NUM_COL_ID));
			demande.setLogin(c.getString(NUM_COL_LOGIN)) ;
			demande.setLat(c.getDouble(NUM_COL_LAT)) ;
			demande.setLng(c.getDouble(NUM_COL_LNG)) ;
			demande.setDescription(c.getString(NUM_COL_DESCRIPTION)) ;
			demande.setEtat(c.getInt(NUM_COL_ETAT)) ;
			demandes.add(demande) ;
		}while(c.moveToNext()) ;
		
		
		//On ferme le cursor
		c.close();
 
		//On retourne le livre
		return demandes;
	}
}