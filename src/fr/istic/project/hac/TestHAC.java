package fr.istic.project.hac;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.istic.project.utils.FormatUtils;

public class TestHAC {
	private final static int nbClusters=12;
	/**
	 * executer ce script pour generer des couples nom / timestamp depuis un dossier de photo
	 * puis coller le résultat en dessous entre //ici et //la
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
	public static void main(String[] args) {

		new TestHAC();
	}

	private List<PictInfo> dat;
	private CustomArrayList dist;
	//private List<Long> mids;//valeurs moyennes pour établir les classes
	//private List<CustomArrayList> clusters;
	public TestHAC(){
		dat=new ArrayList<PictInfo>();//utilisé une fois pour parcourir la liste des images et calculer les distances
		dist=new CustomArrayList();//<-- TODO rendre ça générique
		//clusters=new ArrayList<TestHAC.CustomArrayList>();//clusters 1 -----> * CustomArrayList 1 -----> * Distances 
		//ici
		dat.add(new PictInfo("CIMG0760.JPG"," 2010:03:05 23:50" ));
		dat.add(new PictInfo("CIMG0761.JPG"," 2010:03:05 23:50" ));
		dat.add(new PictInfo("CIMG0762.JPG"," 2010:03:05 23:52" ));
		dat.add(new PictInfo("CIMG0763.JPG"," 2010:03:05 23:52" ));
		dat.add(new PictInfo("CIMG0766.JPG"," 2010:03:05 23:55" ));
		dat.add(new PictInfo("CIMG0767.JPG"," 2010:03:05 23:55" ));
		dat.add(new PictInfo("CIMG0768.JPG"," 2010:03:05 23:55" ));
		dat.add(new PictInfo("CIMG0769.JPG"," 2010:03:05 23:55" ));
		dat.add(new PictInfo("CIMG0770.JPG"," 2010:03:05 23:56" ));
		dat.add(new PictInfo("CIMG0771.JPG"," 2010:03:05 23:56" ));
		dat.add(new PictInfo("CIMG0772.JPG"," 2010:03:05 23:56" ));
		dat.add(new PictInfo("CIMG0774.JPG"," 2010:03:05 23:57" ));
		dat.add(new PictInfo("CIMG0775.JPG"," 2010:03:05 23:57" ));
		dat.add(new PictInfo("CIMG0776.JPG"," 2010:03:05 23:57" ));
		dat.add(new PictInfo("CIMG0777.JPG"," 2010:03:05 23:57" ));
		dat.add(new PictInfo("CIMG0778.JPG"," 2010:03:05 23:59" ));
		dat.add(new PictInfo("CIMG0780.JPG"," 2010:03:06 00:00" ));
		dat.add(new PictInfo("CIMG0781.JPG"," 2010:03:06 00:02" ));
		dat.add(new PictInfo("CIMG0783.JPG"," 2010:03:06 00:03" ));
		dat.add(new PictInfo("CIMG0784.JPG"," 2010:03:06 00:04" ));
		dat.add(new PictInfo("CIMG0786.JPG"," 2010:03:06 00:06" ));
		dat.add(new PictInfo("CIMG0787.JPG"," 2010:03:06 00:06" ));
		dat.add(new PictInfo("CIMG0788.JPG"," 2010:03:06 00:08" ));
		dat.add(new PictInfo("CIMG0789.JPG"," 2010:03:06 00:09" ));
		dat.add(new PictInfo("CIMG0790.JPG"," 2010:03:06 00:09" ));
		dat.add(new PictInfo("CIMG0791.JPG"," 2010:03:06 00:09" ));
		dat.add(new PictInfo("CIMG0792.JPG"," 2010:03:06 00:10" ));
		dat.add(new PictInfo("CIMG0793.JPG"," 2010:03:06 00:10" ));
		dat.add(new PictInfo("CIMG0794.JPG"," 2010:03:06 00:11" ));
		dat.add(new PictInfo("CIMG0795.JPG"," 2010:03:06 00:17" ));
		dat.add(new PictInfo("CIMG0796.JPG"," 2010:03:06 01:16" ));
		dat.add(new PictInfo("CIMG0797.JPG"," 2010:03:06 01:38" ));
		dat.add(new PictInfo("CIMG0798.JPG"," 2010:03:06 01:39" ));//oui
		dat.add(new PictInfo("CIMG0799.JPG"," 2010:03:06 01:44" ));
		dat.add(new PictInfo("CIMG0800.JPG"," 2010:03:06 01:44" ));
		dat.add(new PictInfo("CIMG0801.JPG"," 2010:03:06 01:44" ));
		dat.add(new PictInfo("CIMG0802.JPG"," 2010:03:06 01:44" ));
		dat.add(new PictInfo("CIMG0803.JPG"," 2010:03:06 01:44" ));
		dat.add(new PictInfo("CIMG0804.JPG"," 2010:03:06 01:45" ));
		dat.add(new PictInfo("CIMG0805.JPG"," 2010:03:06 01:45" ));
		//la
		findMiddle();
	}

	private void findMiddle(){
		long distance=0;
		long maxDistance=0;
		long minDistance=0;
		//long middle=0;

		for(PictInfo one: dat){
			for(PictInfo two: dat){
				distance=one.getTime()-two.getTime();
				if(distance>=0){//hypothèse: si une distance est négative son équivalent positif sera calculé ailleurs
					dist.add(new Distance(one, two,distance));//la moitié des objets du tableau sont des doublons
					if(minDistance<distance){minDistance=distance;}
					else if(maxDistance>distance){maxDistance=distance;}
				}
			}
		}

		System.out.println(dist.getMiddle());

		for(int i=0;i<nbClusters;i++){

			//CustomArrayList[] vals=halve(dist);
		}

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
			Date dateStr = FormatUtils.stringHacToDate(strDate);
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
		//private PictInfo p1;
		//private PictInfo p2;
		private long distance;
		
		public long getDistance() {
			return distance;
		}
		
		public Distance(PictInfo p1,PictInfo p2,long distance){
			//this.p1=p1;
			//this.p2=p2;
			this.distance=distance;
		}
	}
	/**
	 * extends ArrayList, mais peut renvoyer la moyenne des distances en un coup
	 * 
	 */
	class CustomArrayList extends ArrayList<Distance>{
		private static final long serialVersionUID = 1L;

		public long getMiddle(){
			long sum=0;
			for(Distance d:this){
				sum=sum+d.getDistance();
			}
			return sum/this.size();
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
		return ret;
	}
}

