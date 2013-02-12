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
echo -n dat.add\(new Valuefist\(\"$i\""," ;echo \"`ex`\" \)\)";";
done

	 * le script affiche les distances entre chaque couple de photo
	 * compatible avec un algo de hac
	 */
	public static void main(String[] args) {

		new TestHAC();
	}

	private List<Valuefist> dat;

	public TestHAC(){
		dat=new ArrayList<Valuefist>();
		//ici
		dat.add(new Valuefist("CIMG0760.JPG"," 2010:03:05 23:50" ));
		dat.add(new Valuefist("CIMG0761.JPG"," 2010:03:05 23:50" ));
		dat.add(new Valuefist("CIMG0762.JPG"," 2010:03:05 23:52" ));
		dat.add(new Valuefist("CIMG0763.JPG"," 2010:03:05 23:52" ));
		dat.add(new Valuefist("CIMG0766.JPG"," 2010:03:05 23:55" ));
		dat.add(new Valuefist("CIMG0767.JPG"," 2010:03:05 23:55" ));
		dat.add(new Valuefist("CIMG0768.JPG"," 2010:03:05 23:55" ));
		dat.add(new Valuefist("CIMG0769.JPG"," 2010:03:05 23:55" ));
		dat.add(new Valuefist("CIMG0770.JPG"," 2010:03:05 23:56" ));
		dat.add(new Valuefist("CIMG0771.JPG"," 2010:03:05 23:56" ));
		dat.add(new Valuefist("CIMG0772.JPG"," 2010:03:05 23:56" ));
		dat.add(new Valuefist("CIMG0774.JPG"," 2010:03:05 23:57" ));
		dat.add(new Valuefist("CIMG0775.JPG"," 2010:03:05 23:57" ));
		dat.add(new Valuefist("CIMG0776.JPG"," 2010:03:05 23:57" ));
		dat.add(new Valuefist("CIMG0777.JPG"," 2010:03:05 23:57" ));
		dat.add(new Valuefist("CIMG0778.JPG"," 2010:03:05 23:59" ));
		dat.add(new Valuefist("CIMG0780.JPG"," 2010:03:06 00:00" ));
		dat.add(new Valuefist("CIMG0781.JPG"," 2010:03:06 00:02" ));
		dat.add(new Valuefist("CIMG0783.JPG"," 2010:03:06 00:03" ));
		dat.add(new Valuefist("CIMG0784.JPG"," 2010:03:06 00:04" ));
		dat.add(new Valuefist("CIMG0786.JPG"," 2010:03:06 00:06" ));
		dat.add(new Valuefist("CIMG0787.JPG"," 2010:03:06 00:06" ));
		dat.add(new Valuefist("CIMG0788.JPG"," 2010:03:06 00:08" ));
		dat.add(new Valuefist("CIMG0789.JPG"," 2010:03:06 00:09" ));
		dat.add(new Valuefist("CIMG0790.JPG"," 2010:03:06 00:09" ));
		dat.add(new Valuefist("CIMG0791.JPG"," 2010:03:06 00:09" ));
		dat.add(new Valuefist("CIMG0792.JPG"," 2010:03:06 00:10" ));
		dat.add(new Valuefist("CIMG0793.JPG"," 2010:03:06 00:10" ));
		dat.add(new Valuefist("CIMG0794.JPG"," 2010:03:06 00:11" ));
		dat.add(new Valuefist("CIMG0795.JPG"," 2010:03:06 00:17" ));
		dat.add(new Valuefist("CIMG0796.JPG"," 2010:03:06 01:16" ));
		dat.add(new Valuefist("CIMG0797.JPG"," 2010:03:06 01:38" ));
		dat.add(new Valuefist("CIMG0798.JPG"," 2010:03:06 01:39" ));
		dat.add(new Valuefist("CIMG0799.JPG"," 2010:03:06 01:44" ));
		dat.add(new Valuefist("CIMG0800.JPG"," 2010:03:06 01:44" ));
		dat.add(new Valuefist("CIMG0801.JPG"," 2010:03:06 01:44" ));
		dat.add(new Valuefist("CIMG0802.JPG"," 2010:03:06 01:44" ));
		dat.add(new Valuefist("CIMG0803.JPG"," 2010:03:06 01:44" ));
		dat.add(new Valuefist("CIMG0804.JPG"," 2010:03:06 01:45" ));
		dat.add(new Valuefist("CIMG0805.JPG"," 2010:03:06 01:45" ));
		dat.add(new Valuefist("CIMG0806.JPG"," 2010:03:06 01:46" ));
		dat.add(new Valuefist("CIMG0807.JPG"," 2010:03:06 01:46" ));
		dat.add(new Valuefist("CIMG0808.JPG"," 2010:03:06 01:49" ));
		dat.add(new Valuefist("CIMG0809.JPG"," 2010:03:06 01:53" ));
		dat.add(new Valuefist("CIMG0810.JPG"," 2010:03:06 01:53" ));
		dat.add(new Valuefist("CIMG0811.JPG"," 2010:03:06 01:53" ));
		dat.add(new Valuefist("CIMG0812.JPG"," 2010:03:06 01:57" ));
		dat.add(new Valuefist("CIMG0813.JPG"," 2010:03:06 01:57" ));
		dat.add(new Valuefist("CIMG0814.JPG"," 2010:03:06 01:58" ));
		dat.add(new Valuefist("CIMG0815.JPG"," 2010:03:06 02:00" ));
		dat.add(new Valuefist("CIMG0816.JPG"," 2010:03:06 02:00" ));
		dat.add(new Valuefist("CIMG0818.JPG"," 2010:03:06 02:06" ));
		dat.add(new Valuefist("CIMG0819.JPG"," 2010:03:06 02:06" ));
		dat.add(new Valuefist("CIMG0820.JPG"," 2010:03:06 02:06" ));
		dat.add(new Valuefist("CIMG0821.JPG"," 2010:03:06 02:09" ));
		dat.add(new Valuefist("CIMG0822.JPG"," 2010:03:06 02:09" ));
		dat.add(new Valuefist("CIMG0823.JPG"," 2010:03:06 02:09" ));
		dat.add(new Valuefist("CIMG0824.JPG"," 2010:03:06 02:10" ));
		dat.add(new Valuefist("CIMG0825.JPG"," 2010:03:06 02:12" ));
		dat.add(new Valuefist("CIMG0826.JPG"," 2010:03:06 02:13" ));
		dat.add(new Valuefist("CIMG0827.JPG"," 2010:03:06 02:13" ));
		dat.add(new Valuefist("CIMG0828.JPG"," 2010:03:06 02:15" ));
		dat.add(new Valuefist("CIMG0829.JPG"," 2010:03:06 02:15" ));
		dat.add(new Valuefist("CIMG0830.JPG"," 2010:03:06 02:21" ));
		dat.add(new Valuefist("CIMG0831.JPG"," 2010:03:06 02:21" ));
		dat.add(new Valuefist("CIMG0832.JPG"," 2010:03:06 02:21" ));
		dat.add(new Valuefist("CIMG0835.JPG"," 2010:03:06 02:23" ));
		dat.add(new Valuefist("CIMG0836.JPG"," 2010:03:06 02:25" ));
		dat.add(new Valuefist("CIMG0837.JPG"," 2010:03:06 02:25" ));
		dat.add(new Valuefist("CIMG0838.JPG"," 2010:03:06 02:26" ));
		dat.add(new Valuefist("CIMG0840.JPG"," 2010:03:06 02:35" ));



		//la

		fucktepate();

	}

	private void fucktepate(){
		for(Valuefist one: dat){
			for(Valuefist two: dat){
//				System.out.println(one.getName()+" "+two.getName()+" "+String.valueOf((one.getTime()-two.getTime())));
			}
		}

	}
	private class Valuefist{
		String name;
		int time;
		private List<Valuefist> distances;
		public Valuefist(String name,String strDate){

			SimpleDateFormat formatter = new SimpleDateFormat(" yyyy:MM:DD HH:mm");
			Date dateStr=new Date();
			try {
				dateStr = formatter.parse(strDate);
			} catch (ParseException e) {

				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("popo"+dateStr);
//			String formattedDate = formatter.format(dateStr);
//			System.out.println("yyyy-MM-dd date is ==>"+formattedDate);
//			Date date1 = formatter.parse(formattedDate);

			this.name=name;
			this.time=time;
		}

		public String toString(){
			return "name "+name+" timestamp "+time;
		}
		public String getName(){return name;}
		public int getTime(){return time;}

	}
}
