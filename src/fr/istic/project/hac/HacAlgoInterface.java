package fr.istic.project.hac;

import java.util.ArrayList;

import fr.istic.project.model.OPhoto;

public interface HacAlgoInterface {
	/**
	 * renvoie une distance entre deux classes
	 * @param ap1 classe 1
	 * @param ap2 classe 2
	 * @return
	 */
	public float computeDissimilarities(ArrayList<OPhoto> ap1,ArrayList<OPhoto> ap2);
}
