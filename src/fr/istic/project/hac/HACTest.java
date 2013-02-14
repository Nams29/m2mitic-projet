package fr.istic.project.hac;


public class HACTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new HACTest();

	}
	public HACTest(){
		HAC hac=new HAC();
		hac.addPict("CIMG0760.JPG","2010:03:05 23:50" );
		hac.addPict("CIMG0761.JPG","2010:03:05 23:50" );
		hac.addPict("CIMG0762.JPG","2010:03:05 23:50" );
		hac.addPict("CIMG0763.JPG","2010:03:05 23:52" );
		hac.addPict("CIMG0766.JPG","2010:03:05 23:52" );
		hac.addPict("CIMG0767.JPG","2010:03:05 23:55" );
		
		hac.findMiddle();
		hac.displayGroups();

	}
}
