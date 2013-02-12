package fr.istic.project.hac;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestHAC {

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

	private List<CrabeMou> dat;

	public TestHAC(){
		dat=new ArrayList<CrabeMou>();
		//ici
		dat.add(new CrabeMou("CIMG0760.JPG"," 2010:03:05 23:50" ));
		dat.add(new CrabeMou("CIMG0761.JPG"," 2010:03:05 23:50" ));
		dat.add(new CrabeMou("CIMG0762.JPG"," 2010:03:05 23:52" ));
		dat.add(new CrabeMou("CIMG0763.JPG"," 2010:03:05 23:52" ));
		dat.add(new CrabeMou("CIMG0766.JPG"," 2010:03:05 23:55" ));
		dat.add(new CrabeMou("CIMG0767.JPG"," 2010:03:05 23:55" ));
		dat.add(new CrabeMou("CIMG0768.JPG"," 2010:03:05 23:55" ));
		dat.add(new CrabeMou("CIMG0769.JPG"," 2010:03:05 23:55" ));
		dat.add(new CrabeMou("CIMG0770.JPG"," 2010:03:05 23:56" ));
		dat.add(new CrabeMou("CIMG0771.JPG"," 2010:03:05 23:56" ));
		dat.add(new CrabeMou("CIMG0772.JPG"," 2010:03:05 23:56" ));
		dat.add(new CrabeMou("CIMG0774.JPG"," 2010:03:05 23:57" ));
		dat.add(new CrabeMou("CIMG0775.JPG"," 2010:03:05 23:57" ));
		dat.add(new CrabeMou("CIMG0776.JPG"," 2010:03:05 23:57" ));
		dat.add(new CrabeMou("CIMG0777.JPG"," 2010:03:05 23:57" ));
		dat.add(new CrabeMou("CIMG0778.JPG"," 2010:03:05 23:59" ));
		dat.add(new CrabeMou("CIMG0780.JPG"," 2010:03:06 00:00" ));
		dat.add(new CrabeMou("CIMG0781.JPG"," 2010:03:06 00:02" ));
		dat.add(new CrabeMou("CIMG0783.JPG"," 2010:03:06 00:03" ));
		dat.add(new CrabeMou("CIMG0784.JPG"," 2010:03:06 00:04" ));
		dat.add(new CrabeMou("CIMG0786.JPG"," 2010:03:06 00:06" ));
		dat.add(new CrabeMou("CIMG0787.JPG"," 2010:03:06 00:06" ));
		dat.add(new CrabeMou("CIMG0788.JPG"," 2010:03:06 00:08" ));
		dat.add(new CrabeMou("CIMG0789.JPG"," 2010:03:06 00:09" ));
		dat.add(new CrabeMou("CIMG0790.JPG"," 2010:03:06 00:09" ));
		dat.add(new CrabeMou("CIMG0791.JPG"," 2010:03:06 00:09" ));
		dat.add(new CrabeMou("CIMG0792.JPG"," 2010:03:06 00:10" ));
		dat.add(new CrabeMou("CIMG0793.JPG"," 2010:03:06 00:10" ));
		dat.add(new CrabeMou("CIMG0794.JPG"," 2010:03:06 00:11" ));
		dat.add(new CrabeMou("CIMG0795.JPG"," 2010:03:06 00:17" ));
		dat.add(new CrabeMou("CIMG0796.JPG"," 2010:03:06 01:16" ));
		dat.add(new CrabeMou("CIMG0797.JPG"," 2010:03:06 01:38" ));
		dat.add(new CrabeMou("CIMG0798.JPG"," 2010:03:06 01:39" ));//oui
		dat.add(new CrabeMou("CIMG0799.JPG"," 2010:03:06 01:44" ));
		dat.add(new CrabeMou("CIMG0800.JPG"," 2010:03:06 01:44" ));
		dat.add(new CrabeMou("CIMG0801.JPG"," 2010:03:06 01:44" ));
		dat.add(new CrabeMou("CIMG0802.JPG"," 2010:03:06 01:44" ));
		dat.add(new CrabeMou("CIMG0803.JPG"," 2010:03:06 01:44" ));
		dat.add(new CrabeMou("CIMG0804.JPG"," 2010:03:06 01:45" ));
		dat.add(new CrabeMou("CIMG0805.JPG"," 2010:03:06 01:45" ));

		middletourte();

	}

	private void middletourte(){
		for(CrabeMou one: dat){
			for(CrabeMou two: dat){
				//System.out.println(one.getName()+" "+two.getName()+" "+String.valueOf(Math.abs((one.getTime()-two.getTime()))));
				System.out.println(one.getName()+","+two.getName()+","+String.valueOf(Math.abs((one.getTime()-two.getTime()))));
			}
		}

	}
	private class CrabeMou{
		long time;// no see
		String name;
		private List<CrabeMou> distances;
		public CrabeMou(String name,String strDate){

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
}
