package fr.istic.project.hac;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * mode d'emploi: instancier la classe
 * intancier autant d'objets PictInfo que necessaire
 * et les ajouter avec addPict
 * @author antoine
 *
 */
public class HAC {
	private final static int NBCLASSES=4;
	/**
	 * executer ce script pour generer des couples nom / timestamp depuis un dossier de photo
	 * puis coller le résultat en dessous entre //ici et //la
	 * OBSOLETE
	 * 
	#/bin/bash
	function ex {
	exiv2 $i|grep timestamp|cut -d:  -f2-5;
	}

	for i in *.JPG;
	do 
	echo -n dat.add\(new CrabeMou\(\"$i\""," ;echo \"`ex`\" \)\)";";
	done

	 * le script affiche les distances entre chaque couple de photo
	 * compatible avec un algo de hac
	 */

	private List<PictInfo> dat;
	private CustomArrayList dist;
	//	private List<Long> mids;//valeurs moyennes pour établir les classes
	private List<CustomArrayList> clusters;
	private List<CustomArrayList> clusters_copy;

	/**
	 * ajoute des PictInfo a analyser
	 * @param name nom du fichier image
	 * @param timestamp date de prise de vue au format yyyy:MM:DD HH:mm
	 */
	public void addPict(String name,String timestamp){
		dat.add(new PictInfo(name,timestamp));
	}
	public HAC(){
		dat=new ArrayList<PictInfo>();//utilisé une fois pour parcourir la liste des images et calculer les distances
		dist=new CustomArrayList();//<-- TODO rendre ça générique
		clusters=new ArrayList<HAC.CustomArrayList>();//clusters 1 -----> * CustomArrayList 1 -----> * Distances 
	}
	
	public void printResults(){

		for (CustomArrayList clu:clusters){
			System.out.println("UN CLUSTER");
			for (Distance d:clu){
				System.out.println(d);
			}
		}
	}

	public void findMiddle(){
		long distance=0;
		CustomArrayList[] halfs;
		for(PictInfo one: dat){
			for(PictInfo two: dat){
				distance=one.getTime()-two.getTime();
				if(distance>=0){//hypothèse: si une distance est négative son équivalent positif sera calculé ailleurs
					dist.add(new Distance(one, two,distance));//la moitié des objets du tableau sont des doublons
				}
			}
		}
		clusters.add(dist);//on démarre avec tout dans le même cluster
		for(int i=0;i<NBCLASSES;i++){
			clusters_copy=new ArrayList<HAC.CustomArrayList>();//on édite une copie de la liste
			System.out.println("PASS"+i+" "+clusters.size());
			for (CustomArrayList cal:clusters){//on parcourt la liste originale
				halfs=halve(cal);//recupere deux moitiés de cluster, classés par rapport a la valeur moyenne
				if(halfs[0].size()>0){clusters_copy.add(halfs[0]);
				System.out.println("\t ajout de "+halfs[0].size());
				}

				if(halfs[1].size()>0){clusters_copy.add(halfs[1]);
				System.out.println("\t ajout de "+halfs[1].size());
				}


			}

			clusters=clusters_copy;
		}
		graph(clusters);

	}
	private void graph(List<CustomArrayList> l){
		long sum=0;
		for(CustomArrayList o:l){
			System.out.print("["+o.size()+"]");
			sum+=o.size();
		}
		System.out.println("--------");
	}

	/**
	 * structure de donnée qui contient le nom et timestamp en epoch d'une photo
	 * @author antoine
	 *
	 */
	class PictInfo{
		long time;// no see
		String name;

		public PictInfo(String name,String strDate){

			SimpleDateFormat formatter = new SimpleDateFormat(" yyyy:MM:DD HH:mm");
			Date dateStr=new Date();
			try {
				dateStr = formatter.parse(strDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			this.name=name;
			this.time=dateStr.getTime()/1000;//convert date -> epoch (en secondes)
		}

		public String toString(){
			return "name "+name+" timestamp "+time;
		}
		public String getName(){return name;}
		public long getTime(){return time;}
	}

	class Distance{
		private PictInfo p1;
		private PictInfo p2;
		private long distance;
		public long getDistance() {
			return distance;
		}
		@Override
		public String toString() {
			return "Distance [p1=" + p1.getName() + ", p2=" + p2.getName() + ", distance="
					+ distance + "]";
		}
		public Distance(PictInfo p1,PictInfo p2,long distance){
			this.p1=p1;this.p2=p2;this.distance=distance;
		}
	}
	/**
	 * extends ArrayList, mais peut renvoyer la moyenne des distances en un coup
	 * 
	 */
	class CustomArrayList extends ArrayList<Distance>{
		private static final long serialVersionUID = 1L;

		public long getMiddle(){
			long sum=0;long res=0;
			for(Distance d:this){
				sum=sum+d.getDistance();
			}
			try{
				res=sum/this.size();
			}
			catch(ArithmeticException e){}
			return res;
		}
	}



	/**
	 * 
	 * @return les valeurs de la liste, avec les valeurs sup a middle d'un coté, inf de l'autre
	 */
	public CustomArrayList[] halve(CustomArrayList cal){
		long mid=cal.getMiddle();

		CustomArrayList inf = new CustomArrayList();
		CustomArrayList sup = new CustomArrayList();
		for(Distance d:cal){
			if (d.getDistance()>mid){//les distances supérieures a la moyenne vont dans sup
				sup.add(d);
			}else{//sinon dans inf
				inf.add(d);
			}
		}
		CustomArrayList[] ret = new CustomArrayList[2];
		ret[0]=inf;
		ret[1]=sup;
		System.out.println("halve(): recu "+cal.size()+"; sup "+sup.size()+" items; inf "+inf.size()+" items");
		return ret;
	}
}


