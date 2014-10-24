package com.takeaphoto.model;

public class Demande {
	private int id ;
	private int id_user ;
	private Double lat ;
	private Double lng ;
	private String description ;
	private int etat ;
	
	public Demande(){}
	
	public Demande(int id_user, Double lat, Double lng, String description) {
		this.id_user = id_user;
		this.lat = lat ;
		this.lng = lng ;
		this.description = description;
		this.etat = 0 ;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getId_user() {
		return id_user;
	}

	public void setId_user(int id_user) {
		this.id_user = id_user;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getEtat() {
		return etat;
	}

	public void setEtat(int etat) {
		this.etat = etat;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	@Override
	public String toString() {
		return "Demande [id=" + id + ", id_user=" + id_user + ", lat=" + lat
				+ ", lng=" + lng + ", description=" + description + ", etat="
				+ etat + "]";
	}
	
}
