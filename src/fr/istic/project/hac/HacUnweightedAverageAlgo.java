package fr.istic.project.hac;

import java.util.ArrayList;

import fr.istic.project.hac.HAC.PictInfo;

public class HacUnweightedAverageAlgo implements HacAlgoInterface {

	@Override
	public float computeDissimilarities(ArrayList<PictInfo> ap1,
			ArrayList<PictInfo> ap2) {
		float d=0;
		float res=0;

		for(PictInfo p1:ap1){
			for(PictInfo p2:ap2){
				d=d+Math.max(p2.getTime(),p1.getTime())-Math.min(p2.getTime(),p1.getTime());
			}
		}

		res=d/(ap1.size()*ap2.size());


		return res;
	}

}
