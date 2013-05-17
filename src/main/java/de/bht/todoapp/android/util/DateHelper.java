/**
 * 
 */
package de.bht.todoapp.android.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author markus
 *
 */
public class DateHelper
{
	private static final String FORMAT_DATE = "dd.MM.yyyy";
	private static final String FORMAT_TIME = "HH:mm";
	
	public static String getDateString(final Date date) {
		DateFormat f = new SimpleDateFormat(FORMAT_DATE);
		//f.setTimeZone(TimeZone.getDefault());
		return f.format(date);
	}
	
	public static String getTimeString(final Date date) {
		DateFormat f = new SimpleDateFormat(FORMAT_TIME);
		//f.setTimeZone(TimeZone.getDefault());
		return f.format(date);
	}


}
