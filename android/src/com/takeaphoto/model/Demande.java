package com.takeaphoto.model;

public class Demande {
	private int id ;
	private int idUser ;
	private Double lat ;
	private Double lng ;
	private String description ;
	private int etat ;
	
	public Demande(){}
	
	public Demande(int idUser, Double lat, Double lng, String description) {
		this.idUser = idUser;
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
		return idUser;
	}

	public void setId_user(int id_user) {
		this.idUser = id_user;
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
		return "Demande [id=" + id + ", id_user=" + idUser + ", lat=" + lat
				+ ", lng=" + lng + ", description=" + description + ", etat="
				+ etat + "]";
	}
	
}
