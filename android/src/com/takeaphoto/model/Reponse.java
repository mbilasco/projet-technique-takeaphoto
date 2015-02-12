package com.takeaphoto.model;

import java.io.Serializable;

public class Reponse implements Serializable {
	private int id ;
	private String url;
	private int id_demande;
	
	public Reponse() {	}

	public Reponse(int id_reponse, String url, int id_demande) {
		super();
		this.id = id_reponse;
		this.url = url;
		this.id_demande = id_demande;
	}
	
	public Reponse(String url, int id_demande) {
		super();
		this.url = url;
		this.id_demande = id_demande;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getId_demande() {
		return id_demande;
	}

	public void setId_demande(int id_demande) {
		this.id_demande = id_demande;
	}

	public String toString() {
		return "Reponse [id=" + id + ", url=" + url + ", id_demande="
				+ id_demande + "]";
	}
}
