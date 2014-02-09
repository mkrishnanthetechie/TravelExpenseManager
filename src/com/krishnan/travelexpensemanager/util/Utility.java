package com.krishnan.travelexpensemanager.util;

import com.krishnan.travelexpensemanager.R;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

/**
 * @author krishm90	
 * Utility class to log the application messages
 *
 */
public class Utility {

	public static void out(Object obj) {
		Log.d("project_debug", obj.toString());
	}

	public static String returnActionName(Context context, int index)
			throws Exception {
		try {
			Resources res = context.getResources();
			return res.getStringArray(R.array.actions)[index];
		}

		catch (ArrayIndexOutOfBoundsException indexException) {
			throw new ArrayIndexOutOfBoundsException("Error indexing at"
					+ index + "\n" + "error msg"
					+ indexException.getLocalizedMessage());
		}
	}
}
