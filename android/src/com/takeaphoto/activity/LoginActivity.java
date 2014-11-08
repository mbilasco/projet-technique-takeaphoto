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

import com.takeaphoto.model.User;
import com.takeaphoto.server.UserServeur;

/**
 * 
 * @author Victor Paumier et Jeremie Samson
 * 
 *         LoginActivity est l'activity d'entr�e dans l'application qui test le
 *         login et mot de pass de l'utilisateur
 * 
 */
public class LoginActivity extends Activity {

	final String EXTRA_ID_USER = "ID_User";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// on d�clare les champs
		final EditText login = (EditText) findViewById(R.id.user_email);
		final EditText pass = (EditText) findViewById(R.id.user_password);

		// si il existe un utilisateur unique dans la base local (derniere
		// personne connect�e)
		// alors on pr�rempli les champs
		User user = new UserServeur().getOnlyUser(getApplicationContext());
		if (user != null) {
			login.setText(user.getLogin());
			pass.setText(user.getPass());
		}

		// Impl�mentation du listener pour le bouton d'authentification
		final Button loginButton = (Button) findViewById(R.id.connect);
		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// recup�ration des valeurs
				final String loginTxt = login.getText().toString();
				final String passTxt = pass.getText().toString();

				// test login et pass sont diff�rents de vide et ne contiennent
				// pas deux espaces de suite
				if (loginTxt.equals("") || loginTxt.contains("  ")
						|| passTxt.equals("") || passTxt.contains("  ")) {
					Toast.makeText(LoginActivity.this,
							R.string.email_or_password_empty,
							Toast.LENGTH_SHORT).show();
					return;
				}

				// test taille login > 4
				if (loginTxt.length() < 4) {
					Toast.makeText(LoginActivity.this, R.string.login_min,
							Toast.LENGTH_SHORT).show();
					return;
				}

				// test taille pass > 4
				if (passTxt.length() < 4) {
					Toast.makeText(LoginActivity.this, R.string.pass_min,
							Toast.LENGTH_SHORT).show();
					return;
				}

				// On declare le pattern que l'on doit suivre
				Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
				// On declare un matcher, qui comparera le pattern avec la
				// string pass�e en argument
				Matcher m = p.matcher(loginTxt);
				// Si laadresse mail saisie ne correspond au format d�une
				// adresse mail
				if (!m.matches()) {
					// Toast est une classe fournie par le SDK Android
					// pour afficher les messages dans des minis pop up
					// Le premier argument est le Context, puis
					// le message et a la fin la duree de ce dernier
					Toast.makeText(LoginActivity.this,
							R.string.email_format_error, Toast.LENGTH_SHORT)
							.show();
					return;
				}
				// On test la connexion
				HashMap<String, Object> result = new UserServeur()
						.authentification(getApplicationContext(), loginTxt,
								passTxt);

				// Si le resultat de la connexion n'est pas null
				if (result != null) {
					// soit il contient l'id du user
					if (result.containsKey("id")) {
						int id = Integer.parseInt((String) result.get("id"));

						// donc on acc�de au main de l'application en passant
						// l'id utilisateur en param�tre
						Intent intent = new Intent(LoginActivity.this,
								MainActivity.class);
						intent.putExtra(EXTRA_ID_USER, id);
						startActivity(intent);
						// soit le resultat contient le message d'erreur
					} else
						Toast.makeText(getApplicationContext(),
								(String) result.get("message"),
								Toast.LENGTH_SHORT).show();
					// sinon la connexion a echou�e
				} else
					Toast.makeText(getApplicationContext(),
							R.string.erreur_connextion, Toast.LENGTH_SHORT)
							.show();
			}
		});

		// impl�mentation du listener de creation de compte qui redirige vers la
		// page concern�e
		final Button createButton = (Button) findViewById(R.id.create_account);
		createButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,
						CreateAccountActivity.class);
				startActivity(intent);
			}
		});
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) {
	 * getMenuInflater().inflate(R.menu.login, menu); return true; }
	 */

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return (LinearLayout) inflater.inflate(R.layout.activity_login,
				container, false);
	}

}
