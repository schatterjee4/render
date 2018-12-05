package com.example.json.core.models;
public class UserBean {
	 
	//Stores the title from the diaolog
	private String title;
	
	//Stores the apth from the dialog
	private String path;
	
	//Stores the Flag from the dialog
	private String activity;

	//Stores the RTE from the dialog
	private String text;  

	public String getText() {
		   
	    return text;

	}

	public void setText(String text) {

	    this.text = text;

	}

	 
	public String getFlag() {
	return activity;
	}
	public void setFlag(String flag) {
	this.activity = flag;
	}
	public String getTitle() {
	return title;
	}
	public void setTitle(String title) {
	this.title = title;
	}
	public String getPath() {
	return path;
	}
	public void setPath(String path) {
	this.path = path;
	}
	 
	}

