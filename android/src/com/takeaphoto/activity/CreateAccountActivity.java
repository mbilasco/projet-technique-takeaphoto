package com.takeaphoto.activity;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sample.activity.R;
import com.takeaphoto.server.UserServeur;

/**
 * 
 * @author Victor Paumier et Jeremie Samson
 * 
 * CreateAccountActivity est l'activité qui permet la 
 * création d'un nouveau compte utilisateur
 */

public class CreateAccountActivity extends Activity{
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create);
		
		//Définition des différents champs
		final EditText login = (EditText) findViewById(R.id.user_email);
		final EditText pass = (EditText) findViewById(R.id.user_password);
		final EditText pass2 = (EditText) findViewById(R.id.user_password2);
		final Button createButton = (Button) findViewById(R.id.create_account);
		
		//Implémentation du listener pour le bouton de creation de compte
		createButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v){
				//recupération des valeurs
				final String loginTxt = login.getText().toString();
				final String passTxt = pass.getText().toString();
				final String pass2Txt = pass2.getText().toString();
				
				//test pass1 = pass2
				if(!passTxt.equals(pass2Txt)){
					Toast.makeText(CreateAccountActivity.this,
							R.string.different_password,
							Toast.LENGTH_SHORT).show();
					return;
				}
				
				//test login et pass sont différents de vide et ne contiennent pas deux espaces de suite
				if (loginTxt.equals("") || loginTxt.contains("  ") || passTxt.equals("") || passTxt.equals("  ")) {
					Toast.makeText(CreateAccountActivity.this,
							R.string.email_or_password_empty,
							Toast.LENGTH_SHORT).show();
					return;
				}
				
				//test taille login > 4
				if (loginTxt.length() < 4) {
					Toast.makeText(CreateAccountActivity.this,
							R.string.login_min,
							Toast.LENGTH_SHORT).show();
					return;
				}
				
				//test taille pass > 4
				if (passTxt.length() < 4) {
					Toast.makeText(CreateAccountActivity.this,
							R.string.pass_min,
							Toast.LENGTH_SHORT).show();
					return;
				}
				
				// On déclare le pattern que l’on doit suivre
				Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
				// On déclare un matcher, qui comparera le pattern avec la
				// string passée en argument
				Matcher m = p.matcher(loginTxt);
				// Si l’adresse mail saisie ne correspond au format d’une
				// adresse mail
				if (!m.matches()) {
					Toast.makeText(CreateAccountActivity.this,
							R.string.email_format_error, Toast.LENGTH_SHORT)
							.show();
					return;
				}
				
				//On test la création du compte
				HashMap<String, Object> result = new UserServeur().createUser(getApplicationContext(), loginTxt, passTxt) ;
				
				//Si le resultat de la création n'est pas null
				if(result != null){
					//soit il contient l'id du nouveau user
					if(result.containsKey("id")){
		        		Toast.makeText(CreateAccountActivity.this, "Compte crée", Toast.LENGTH_SHORT).show();       
		        		
		        		//donc on retourne à la page de connexion
		        		Intent intent = new Intent(CreateAccountActivity.this,
								LoginActivity.class);
						startActivity(intent) ;
						
					//soit le login existe déjà	
					}else
						Toast.makeText(getApplicationContext(), R.string.login_exist, Toast.LENGTH_SHORT).show();
					
					//sinon la connexion a echouée
				}else
					Toast.makeText(getApplicationContext(), R.string.erreur_connextion, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        return (LinearLayout) inflater.inflate(R.layout.activity_create, container, false);
    }
}
