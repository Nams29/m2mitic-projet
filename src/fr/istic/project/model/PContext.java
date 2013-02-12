package fr.istic.project.model;

import java.util.LinkedList;
import java.util.List;

public class PContext { // Context est déjà utilisé par Android...

	private Long _id;
	private String name;
	private List<PPicture> pictures;
	
	/**
	 * Constructeur côté objet
	 * @param name
	 */
	public PContext(String name) {
		this._id = null;
		this.name = name;
		this.pictures = new LinkedList<PPicture>();
	}
	
	/**
	 * Constructeur côté SQLite
	 * @param _id
	 */
	public PContext(Long _id) {
		this._id = _id;
	}
	
	public void addPicture(PPicture picture) {
		pictures.add(picture);
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
