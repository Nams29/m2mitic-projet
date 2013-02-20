package fr.istic.project.hac;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.util.Log;
import fr.istic.project.model.OPhoto;
import fr.istic.project.utils.FormatUtils;

/**
 * mode d'emploi: instancier la classe
 * ajouter avec addPict autant que necessaire
 * @author antoine
 *
 */
public class HAC {

	private int NBCLASSES=4;
	private ArrayList<ArrayList<OPhoto>> dat;
	HacAlgoInterface hai;

	public HAC(){
		dat=new ArrayList<ArrayList<OPhoto>>();
				hai=new HacWardAlgo();
		//autre algo dispo:
//		hai=new HacUnweightedAverageAlgo();
	}

	/**
	 * Distribue les photos recues en parametres dans la structure de données
	 * chaque photo est placée dans une nouvelle ArrayList, qui représente une classe
	 * destinée a être mergée avec les photos les plus proches 
	 * ArrayList<ArrayList<OPhoto>> dat;
	 * @param photos liste de photos à traiter
	 */
	public void addPhotos(List<OPhoto> photos) {
		for (OPhoto photo : photos) {
			ArrayList<OPhoto> t=new ArrayList<OPhoto>();
			t.add(photo);
			dat.add(t);
		}
		/*	for (OPhoto photo : photos) {
			ArrayList<PictInfo> t=new ArrayList<PictInfo>();
			PictInfo pi=new PictInfo(photo.getPath(), FormatUtils.dateToStringHac(photo.getDate()));
			t.add(pi);
			dat.add(t);
		}*/
	}

	/**
	 * indique le nombre de clusters à produire
	 * @param n
	 */
	public void setNbClusters(int n){
		this.NBCLASSES=n;
	}

	public void findMiddle(){
		/**
		 * Pour i=1 à individus.longueur Faire
    	   classes.ajouter(nouvelle classe(individu[i]));
			Fin Pour
		 */
		float[][] dissim=null;

		while(dat.size()>NBCLASSES){
			dissim =new float[dat.size()][dat.size()];
			for (int i=0;i<dat.size();i++){
				for (int j=i+1;j<dat.size();j++){
					dissim[i][j]=hai.computeDissimilarities(dat.get(i),dat.get(j));
				}
			}

			int i=0,j=0;
			float min=dissim[0][dat.size()-1];

			for (int k=0;k<dat.size();k++){
				for (int l=k+1;l<dat.size();l++){
					if( (dissim[k][l]<min)&&(dissim[k][l]>0)){
						//TODO pas sur que ca sorte vraiment le minimum
						min = dissim[k][l];
						i = k;
						j = l;
						//System.out.println("found min="+min+"@ k:"+k+" l:"+l+" ");
					}
				}
			}

			List<OPhoto> toAdd = new ArrayList<OPhoto>();
			for(Iterator<OPhoto> it = dat.get(j).iterator(); it.hasNext();) {
				OPhoto element = it.next();
				toAdd.add(element);
			}           
			dat.get(i).addAll(toAdd);
			dat.remove(j);
			//			graph(dissim);

		}
		//displayGroups();
	}
	public void graph(float[][] dist){
		for(int i=0;i<dist[0].length;i++){
			System.out.print("|");
			for(int j=0;j<dist[i].length;j++){
				Log.d("hac",dist[i][j]+" ");
			}
			System.out.println("|");
		}
	}
	/**
	 * 
	 * @return une liste de liste, chaque liste est un regroupement
	 */
	public ArrayList<ArrayList<OPhoto>> getResults(){
		return dat;
	}

	public void displayGroups(){
		for (ArrayList<OPhoto> al:dat){
			System.out.println("----class----");
			for(OPhoto pi:al){
				System.out.println(pi);
			}
		}
	}


}


