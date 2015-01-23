package com.takeaphoto.activity;

import java.util.ArrayList;
import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;

//import com.takeaphoto.database.DemandesBDD;

import com.takeaphoto.model.Demande;
import com.takeaphoto.model.User;
import com.takeaphoto.server.DemandeServeur;
import com.takeaphoto.utils.CustomViewPager;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

	private SectionsPagerAdapter mSectionsPagerAdapter;
	private CustomViewPager mViewPager;
	private ManagerActivity manager = new ManagerActivity();
	private MapAdd mapAdd = new MapAdd();
	private MapReponse mapRep = new MapReponse();
	private User user;
	
	private ArrayList<Demande> demandes;
	private ArrayList<Demande> demandesCurrentUser;
	private ArrayList<Demande> demandesOtherUsers;
	
	final int NB_ONGLET = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	//	DemandesBDD demandesBDD = new DemandesBDD(this);
		/*
		UserBDD userBDD = new UserBDD(this);
		userBDD.open();
		//ArrayList<User> tmp = userBDD.getUserWithId(this.getIntent().getIntExtra("EXTRA_ID_USER", -1));
		ArrayList<User> tmp = userBDD.getAllUser();
		User user = tmp.get(0);
		userBDD.close();
		*/
	//	mapAdd.setDemandeBDD(demandesBDD);
	//	mapRep.setDemandeBDD(demandesBDD);
		mapAdd.setMainActivity(this);
		mapRep.setMainActivity(this);
		
		user = new User(this.getIntent().getStringExtra("USER_ID"),this.getIntent().getStringExtra("USER_NAME"));
		manager.setUser(user);
		mapAdd.setUser(user);
		mapRep.setUser(user);
		
		mapRep.setmContext(this);
		mapRep.setOauth(this.getIntent().getSerializableExtra("OAUTH"));
				
		demandes = new DemandeServeur().getDemandes();
		demandesCurrentUser = new ArrayList<Demande>();
		demandesOtherUsers = new ArrayList<Demande>();
		
		Log.i("current user : ", user.getUserId()+"");	
		
		for(int i=0; i<demandes.size(); i++){
			Log.i("other user : ", demandes.get(i).getUserId()+"");	
			if(demandes.get(i).getUserId().compareTo(user.getUserId())==0){
				demandesCurrentUser.add(demandes.get(i));
			}
			else{
				demandesOtherUsers.add(demandes.get(i));
			}
		}
		mapAdd.setDemandes(demandesCurrentUser);
		mapRep.setDemandes(demandesOtherUsers);
		manager.setDemandes(demandesCurrentUser);
		Log.i("autres demandes taille : ", demandesOtherUsers.size()+"");	
		Log.i("mes demandes taille : ", demandesCurrentUser.size()+"");		
		
		// Set up the action bar.
		final ActionBar actionBar = this.getActionBar();
		if (actionBar != null) {
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		}
		
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		
		// Set up the ViewPager with the sections adapter.
		mViewPager = (CustomViewPager) findViewById(R.id.photosViewPager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});
		
		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		mViewPager.setPagingEnabled(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onResume() {
		super.onResume();
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) { }

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) { }

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			
		}

		@Override
		public Fragment getItem(int position) {

			Fragment frag = new Fragment();
			switch (position) {
			case 0:
				frag = mapAdd;
				break;

			case 1:
				frag = mapRep;
				break;

			case 2:
				frag = manager;
				break;
			}

			return frag;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return NB_ONGLET;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}
}
