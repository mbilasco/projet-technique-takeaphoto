package com.takeaphoto.model;

public class User {
	private int id ;
	private String login ;
	private String pass ;
	
	public User() {	}

	public User(int id, String login, String pass) {
		this.id=id ;
		this.login = login;
		this.pass = pass;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", login=" + login + ", pass=" + pass + "]";
	}
}
