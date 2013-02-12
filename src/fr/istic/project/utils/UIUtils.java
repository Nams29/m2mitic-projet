package fr.istic.project.utils;

import android.os.Build;

public class UIUtils {

	public static boolean isHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

}
