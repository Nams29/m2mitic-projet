package fr.istic.project.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.util.Log;

public class FormatUtils {
	
	private static final String TAG = "FormatUtils";
	
	private static SimpleDateFormat formatDateJour = new SimpleDateFormat("yyyy:MM:DD HH:mm", Locale.FRANCE);
	private static SimpleDateFormat formatDB = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
	
	public static String dateToStringHac(Date date) {
		return formatDateJour.format(date);
	}
	
	public static Date stringHacToDate(String date) {
		Date result = null;
		try {
			result = formatDateJour.parse(date);
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage());
		}
		return result;
	}
	
	public static String dateToStringDb(Date date) {
		return formatDB.format(date);
	}
	
	public static Date dbStringToDate(String string) {
		Date date = null;
		try {
			date = formatDB.parse(string);
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage());
		}
		return date;
	}
}
