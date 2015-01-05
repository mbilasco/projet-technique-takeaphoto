package com.takeaphoto.database;

import java.util.ArrayList;

import com.takeaphoto.model.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserBDD {
	private static final int VERSION_BDD = 3;
	private static final String NOM_BDD = "takeaphoto.db";

	private static final String TABLE_USER = "table_user";
	private static final String COL_ID = "ID";
	private static final int NUM_COL_ID = 0;
	private static final String COL_ID_USER = "IdUser";
	private static final int NUM_COL_ID_USER = 1;
	private static final String COL_LOGIN = "Login";
	private static final int NUM_COL_LOGIN = 2;
	private static final String COL_PASS = "Pass";
	private static final int NUM_COL_PASS = 3;

	private SQLiteDatabase bdd;
	private Base maBase;

	public UserBDD(Context context) {
		// On cr�er la BDD et sa table
		maBase = new Base(context, NOM_BDD, null, VERSION_BDD);
	}

	public void open() {
		// on ouvre la BDD en �criture
		bdd = maBase.getWritableDatabase();
	}

	public void close() {
		// on ferme l'acc�s � la BDD
		bdd.close();
	}

	public SQLiteDatabase getBDD() {
		return bdd;
	}

	public long insertUser(User user) {
		// Cr�ation d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		// on lui ajoute une valeur associ� � une cl� (qui est le nom de la
		// colonne dans laquelle on veut mettre la valeur)
		values.put(COL_ID_USER, user.getId());
		values.put(COL_LOGIN, user.getLogin());
		values.put(COL_PASS, user.getPass());
		// on ins�re l'objet dans la BDD via le ContentValues
		return bdd.insert(TABLE_USER, null, values);
	}

	public int updateUser(int id, User user) {
		// La mise � jour d'une demande dans la BDD fonctionne plus ou moins
		// comme une insertion
		// il faut simple pr�ciser quelle livre on doit mettre � jour gr�ce �
		// l'ID
		ContentValues values = new ContentValues();
		values.put(COL_ID_USER, user.getId());
		values.put(COL_LOGIN, user.getLogin());
		values.put(COL_PASS, user.getPass());
		return bdd.update(TABLE_USER, values, COL_ID + " = " + id, null);
	}

	public int removeUserWithID(int id) {
		// Suppression d'une demande de la BDD gr�ce � l'ID
		return bdd.delete(TABLE_USER, COL_ID + " = " + id, null);
	}

	public ArrayList<User> getUserWithLogin(String login) {
		// R�cup�re dans un Cursor les valeur correspondant � une demande
		// contenu dans la BDD (ici on s�lectionne les demandes grace a son
		// login)
		Cursor c = bdd.query(TABLE_USER, new String[] { COL_ID, COL_ID_USER,
				COL_LOGIN, COL_PASS }, COL_LOGIN + " LIKE \"" + login + "\"",
				null, null, null, null);
		return cursorToUser(c);
	}

	public ArrayList<User> getAllUser() {
		// R�cup�re dans un Cursor les valeur correspondant � une demande
		// contenu dans la BDD (ici on s�lectionne les demandes grace a son
		// login)
		Cursor c = bdd.rawQuery("SELECT * FROM TABLE_USER", null);
		return cursorToUser(c);
	}

	public ArrayList<User> getUserWithId(int id) {
		// Recupere dans un Cursor les valeur correspondant a une demande
		// contenu dans la BDD (ici on selectionne les demandes grace a son
		// login)
		Cursor c = bdd.query(TABLE_USER, new String[] { COL_ID, COL_ID_USER,
				COL_LOGIN, COL_PASS }, COL_ID_USER + " = " + id + "", null,
				null, null, null);
		return cursorToUser(c);
	}

	public ArrayList<User> getUserWithoutLogin(String login) {
		// R�cup�re dans un Cursor les valeur correspondant � une demande
		// contenu dans la BDD (ici on s�lectionne les demandes grace a son
		// login)
		Cursor c = bdd.query(TABLE_USER, new String[] { COL_ID, COL_ID_USER,
				COL_LOGIN, COL_PASS }, COL_LOGIN + " NOT LIKE \"" + login
				+ "\"", null, null, null, null);
		return cursorToUser(c);
	}

	// Cette m�thode permet de convertir un cursor en une demande
	private ArrayList<User> cursorToUser(Cursor c) {
		// si aucun �l�ment n'a �t� retourn� dans la requ�te, on renvoie null
		Log.d(String.valueOf(Log.DEBUG), String.valueOf(c.getCount()));
		if (c.getCount() == 0)
			return null;

		ArrayList<User> users = new ArrayList<User>();

		c.moveToFirst();

		do {
			// On cr�� une demande
			User user = new User();

			// on lui affecte toutes les infos gr�ce aux infos contenues dans le
			// Cursor
			user.setId(c.getInt(NUM_COL_ID_USER));
			user.setLogin(c.getString(NUM_COL_LOGIN));
			user.setPass(c.getString(NUM_COL_PASS));
			users.add(user);
		} while (c.moveToNext());

		// On ferme le cursor
		c.close();

		// On retourne le livre
		return users;
	}
}