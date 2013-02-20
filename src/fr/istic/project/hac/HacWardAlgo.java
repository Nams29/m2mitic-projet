package fr.istic.project.hac;

import java.util.ArrayList;

import android.util.Log;
import fr.istic.project.hac.HAC.PictInfo;

public class HacWardAlgo implements HacAlgoInterface{

	@Override
	public float computeDissimilarities(ArrayList<PictInfo> ap1,
			ArrayList<PictInfo> ap2) {
		float sum_ap1=0;float sum_ap2=0;
		float mean_ap1=0;float mean_ap2=0;
		float ward=0;
		for(PictInfo p:ap1){
			sum_ap1=sum_ap1+p.getTime();
		}
		mean_ap1=sum_ap1/ap1.size();//calcul de la moyenne ap1

		for(PictInfo p:ap2){
			sum_ap2=sum_ap2+p.getTime();
		}
		mean_ap2=sum_ap2/ap2.size();//calcul de la moyenne ap2
		float sap1=ap1.size();
		float sap2=ap2.size();

		ward= ((sap1*sap2)/(sap1+sap2))*(Math.max(mean_ap2,mean_ap1)-Math.min(mean_ap2,mean_ap1));//calcul de la distance de ward
		return ward;
	}

}
