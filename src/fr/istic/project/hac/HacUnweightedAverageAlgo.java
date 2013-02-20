package fr.istic.project.hac;

import java.util.ArrayList;

import fr.istic.project.model.OPhoto;

public class HacUnweightedAverageAlgo implements HacAlgoInterface {

	@Override
	public float computeDissimilarities(ArrayList<OPhoto> ap1,
			ArrayList<OPhoto> ap2) {
		float d=0;
		float res=0;

		for(OPhoto p1:ap1){
			for(OPhoto p2:ap2){
				long p1time=p1.getDate().getTime()/1000;
				long p2time=p2.getDate().getTime()/1000;

				d=d+Math.max(p2time,p1time)-Math.min(p2time,p1time);
			}
		}

		res=d/(ap1.size()*ap2.size());


		return res;
	}

}
