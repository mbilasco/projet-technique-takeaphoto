/*
package com.takeaphoto.database;

import java.util.ArrayList;

import com.takeaphoto.model.Demande;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DemandesBDD {
	private static final int VERSION_BDD = 7;
	private static final String NOM_BDD = "takeaphoto.db";
 
	private static final String TABLE_DEMANDE = "table_demande";
	private static final String COL_ID = "ID";
	private static final int NUM_COL_ID = 0;
	private static final String COL_USER_ID = "userId";
	private static final int NUM_COL_USER_ID = 1;
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
		//On cr�er la BDD et sa table
		maBase = new Base(context, NOM_BDD, null, VERSION_BDD);
	}
 
	public void open(){
		//on ouvre la BDD en �criture
		bdd = maBase.getWritableDatabase();
	}
 
	public void close(){
		//on ferme l'acc�s � la BDD
		bdd.close();
	}
 
	public SQLiteDatabase getBDD(){
		return bdd;
	}
 
	public long insertDemande(Demande demande){
		//Cr�ation d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		//on lui ajoute une valeur associ� � une cl� (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
		values.put(COL_USER_ID, demande.getUserId()) ;
		values.put(COL_LAT, demande.getLat()) ;
		values.put(COL_LNG, demande.getLng()) ;
		values.put(COL_DESCRIPTION, demande.getDescription());
		values.put(COL_ETAT, demande.getEtat()) ;
		//on ins�re l'objet dans la BDD via le ContentValues
		return bdd.insert(TABLE_DEMANDE, null, values);
	}
 
	public int updateDemande(String id, Demande demande){
		//La mise � jour d'une demande dans la BDD fonctionne plus ou moins comme une insertion
		//il faut simple pr�ciser quelle livre on doit mettre � jour gr�ce � l'ID
		ContentValues values = new ContentValues();
		values.put(COL_USER_ID, demande.getUserId()) ;
		values.put(COL_LAT, demande.getLat()) ;
		values.put(COL_LNG, demande.getLng()) ;
		values.put(COL_DESCRIPTION, demande.getDescription());
		values.put(COL_ETAT, demande.getEtat()) ;
		return bdd.update(TABLE_DEMANDE, values, COL_ID + " = " +id, null);
	}
 
	public int removeDemandeWithID(int id){
		//Suppression d'une demande de la BDD gr�ce � l'ID
		return bdd.delete(TABLE_DEMANDE, COL_ID + " = " +id, null);
	}
	
	public int removeDemandeWithIdUser(String id){
		//Suppression d'une demande de la BDD gr�ce � l'ID
		return bdd.delete(TABLE_DEMANDE, COL_USER_ID + " = '" +id +"'", null);
	}
	
	public ArrayList<Demande> getDemandeWithId(int id_demande){
		//R�cup�re dans un Cursor les valeur correspondant � une demande contenu dans la BDD (ici on s�lectionne les demandes grace a son login)
		Cursor c = bdd.query(TABLE_DEMANDE, new String[] {COL_ID, COL_USER_ID, COL_LAT, COL_LNG, COL_DESCRIPTION, COL_ETAT}, COL_ID + " = " + id_demande + "", null, null, null, null);
		return cursorToDemande(c);
	}
 
	public ArrayList<Demande> getDemandeWithLoginUser(String login){
		//R�cup�re dans un Cursor les valeur correspondant � une demande contenu dans la BDD (ici on s�lectionne les demandes grace a son login)
		Cursor c = bdd.query(TABLE_DEMANDE, new String[] {COL_ID, COL_USER_ID, COL_LAT, COL_LNG, COL_DESCRIPTION, COL_ETAT}, COL_USER_ID + " LIKE \"" + login +"\"", null, null, null, null);
		return cursorToDemande(c);
	}
	
	public ArrayList<Demande> getDemandeWithIdUser(String userId){
		//R�cup�re dans un Cursor les valeur correspondant � une demande contenu dans la BDD (ici on s�lectionne les demandes grace a son login)
		Cursor c = bdd.query(TABLE_DEMANDE, new String[] {COL_ID, COL_USER_ID, COL_LAT, COL_LNG, COL_DESCRIPTION, COL_ETAT}, COL_USER_ID + " = '" + userId + "'", null, null, null, null);
		return cursorToDemande(c);
	}
 
	public ArrayList<Demande> getDemandeWithoutIdUser(String userId){
		//R�cup�re dans un Cursor les valeur correspondant � une demande contenu dans la BDD (ici on s�lectionne les demandes grace a son login)
		Cursor c = bdd.query(TABLE_DEMANDE, new String[] {COL_ID, COL_USER_ID, COL_LAT, COL_LNG, COL_DESCRIPTION, COL_ETAT}, COL_USER_ID + " != '" + userId + "'", null, null, null, null);
		return cursorToDemande(c);
	}
	
	public ArrayList<Demande> getDemandeWithLatLng(String lat, String lng){
		Cursor c = bdd.query(TABLE_DEMANDE, new String[] {COL_ID, COL_USER_ID, COL_LAT, COL_LNG, COL_DESCRIPTION, COL_ETAT}, COL_LAT + " = '" + lat + "' AND " + COL_LNG + " = '" + lng + "'", null, null, null, null);
		return cursorToDemande(c);
	}
	
	public ArrayList<Demande> getAllDemandes(){
		Cursor c = bdd.query(TABLE_DEMANDE, new String[] {COL_ID, COL_USER_ID, COL_LAT, COL_LNG, COL_DESCRIPTION, COL_ETAT}, null, null, null, null, null);
		return cursorToDemande(c);
	}
	
	//Cette m�thode permet de convertir un cursor en une demande
	private ArrayList<Demande> cursorToDemande(Cursor c){
		//si aucun �l�ment n'a �t� retourn� dans la requ�te, on renvoie null
		if (c.getCount() == 0)
			return null;
	
		ArrayList<Demande> demandes = new ArrayList<Demande>() ;
		
		c.moveToFirst();
		
		do{
			//On cr�� une demande
			Demande demande = new Demande() ;
			
			//on lui affecte toutes les infos gr�ce aux infos contenues dans le Cursor
			demande.setId(c.getInt(NUM_COL_ID));
			demande.setUserId(c.getString(NUM_COL_USER_ID)) ;
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
*/