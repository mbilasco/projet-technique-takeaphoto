package com.takeaphoto.model;

public class Demande {
	private int id ;
	private String userId;
	private Double lat ;
	private Double lng ;
	private String description ;
	private int etat ;
	
	public Demande(){}
	
	public Demande(String userId, Double lat, Double lng, String description) {
		this.userId = userId;
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
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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
		return "Demande [id=" + id + ", userId=" + userId + ", lat=" + lat
				+ ", lng=" + lng + ", description=" + description + ", etat="
				+ etat + "]";
	}
	
}
