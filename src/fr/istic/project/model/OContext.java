package fr.istic.project.model;

import java.util.LinkedList;
import java.util.List;

public class OContext { // Context est déjà utilisé par Android...

	private Long _id;
	private String name;
	private List<OPhoto> photos;
	
	/**
	 * Constructeur côté objet
	 * @param name
	 */
	public OContext(String name) {
		this._id = null;
		this.name = name;
		this.photos = new LinkedList<OPhoto>();
	}
	
	/**
	 * Constructeur côté SQLite
	 * @param _id
	 */
	public OContext(Long _id) {
		this._id = _id;
	}
	
	public void addPicture(OPhoto photo) {
		photos.add(photo);
	}
	
	/* GETTERS */
	
	public Long get_id() {
		return _id;
	}
	
	public String getName() {
		return name;
	}
	
	/* SETTERS */
	
	public void setName(String name) {
		this.name = name;
	}

}
