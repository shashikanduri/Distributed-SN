package com.sn.socialEmulator.model;

import java.util.Date;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;
@Document
public class PostDetails {
	@Id
	private String digitalSignature;
	private String email;
	private String caption;
	private String name;
    private Date timestamp;

	
	public PostDetails() {
		
	}
	
	public PostDetails(String digitalSignature, String email, String caption, String name, Date time) {
		
		this.digitalSignature = digitalSignature;
		this.email = email;
		this.caption = caption;
		this.name = name;
		this.timestamp = time;
	}
		
	public String getDigitalSignature() {
		return digitalSignature;
	}
	public void setDigitalSignature(String digitalSignature) {
		this.digitalSignature = digitalSignature;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getTimeStamp() {
		return timestamp;
	}
	public void setTimeStamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	
	
	
	
	



}
