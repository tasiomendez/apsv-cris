package es.upm.dit.apsv.webLab.cris.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;

@SuppressWarnings("serial")
@Entity
public class Researcher implements Serializable {
	
	@Id
	private String id;
	private String name;
	private String lastName;
	private String email;
	private String password;
	private String scopusUrl;
	private String eid;
	
	@ElementCollection(fetch=FetchType.EAGER)
	private List<String> publications;
	
	public Researcher() {
		this.publications = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getScopusUrl() {
		return scopusUrl;
	}

	public void setScopusUrl(String scopusUrl) {
		this.scopusUrl = scopusUrl;
	}

	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	public List<String> getPublications() {
		return publications;
	}

	public void setPublications(List<String> publications) {
		this.publications = publications;
	}
}
