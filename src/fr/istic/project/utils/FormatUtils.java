package fr.istic.project.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormatUtils {
	private static SimpleDateFormat formatDateJour = new SimpleDateFormat("yyyy:MM:DD HH:mm", Locale.FRANCE);
	
	public static String dateToStringHac(Date date) {
		return formatDateJour.format(date);
	}
	
	public static Date stringHacToDate(String date) {
		Date result = null;
		try {
			result = formatDateJour.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
