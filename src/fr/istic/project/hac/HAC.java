package fr.istic.project.hac;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * mode d'emploi: instancier la classe
 * ajouter avec addPict autant que necessaire
 * @author antoine
 *
 */
public class HAC {
	private int NBCLASSES=4;
	private ArrayList<ArrayList<PictInfo>> dat;
	/**
	 * ajoute des PictInfo a analyser
	 * @param name nom du fichier image
	 * @param timestamp date de prise de vue au format yyyy:MM:DD HH:mm
	 */
	public void addPict(String name,String timestamp){
		ArrayList<PictInfo> t=new ArrayList<PictInfo>();
		PictInfo pi=new PictInfo(name,timestamp);
		t.add(pi);
		dat.add(t);
	}
	/**
	 * indique le nombre de clusters à produire
	 * @param n
	 */
	public void setNbClusters(int n){
		this.NBCLASSES=n;
	}
	public HAC(){
		dat=new ArrayList<ArrayList<PictInfo>>();
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
					dissim[i][j]=dissim(dat.get(i),dat.get(j));
				}
			}

			int i=0,j=0;
			float min=dissim[0][dat.size()-1];

			for (int k=0;k<dat.size();k++){
				for (int l=k+1;l<dat.size();l++){
					if( (dissim[k][l]<min)&&(dissim[k][l]>0)){
						min=dissim[k][l];i=k;j=l;//TODO pas sur que ca sorte vraiment le minimum
//						System.out.println("found min="+min+"@ k:"+k+" l:"+l+" ");
					}
				}
			}
			List<PictInfo> toAdd = new ArrayList<PictInfo>();
			for(Iterator<PictInfo> it = dat.get(j).iterator(); it.hasNext();) {
				PictInfo element = it.next();
				toAdd.add(element);
			}           
			dat.get(i).addAll(toAdd);
			dat.remove(j);//ne suffit pas pour virer une colonne
			graph(dissim);

		}
		//		displayGroups();
	}
	public void graph(float[][] dist){
		for(int i=0;i<dist[0].length;i++){
//			System.out.print("|");
			for(int j=0;j<dist[i].length;j++){
//				System.out.print(dist[i][j]+" ");
			}
//			System.out.println("|");
		}
	}
	/**
	 * 
	 * @return une liste de liste, chaque liste est un regroupement
	 */
	public ArrayList<ArrayList<PictInfo>> getResults(){
		return dat;
	}
	public void displayGroups(){
		for (ArrayList<PictInfo> al:dat){
//			System.out.println("----class----");
			for(PictInfo pi:al){
//				System.out.println(pi);
			}
		}
	}


	public float dissim(ArrayList<PictInfo> ap1,ArrayList<PictInfo> ap2){
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

		ward= ((sap1*sap2)/(sap1+sap2))*(mean_ap2-mean_ap1);//calcul de la distance de ward
		return ward;

	}

	/**
	 * 
	 * @param l une ArrayList qui contient des ArrayList
	 */
	private void graph(List<PictInfo> l){
		long sum=0;
		for(PictInfo o:l){
//			System.out.print("["+o.getName()+"]");
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
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy:MM:DD HH:mm");
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
}


