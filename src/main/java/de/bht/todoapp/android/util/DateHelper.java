/**
 * 
 */
package de.bht.todoapp.android.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;


/**
 * @author markus
 *
 */
public class DateHelper
{
	private static final String FORMAT_DATE = "dd.MM.yyyy";
	private static final String FORMAT_TIME = "HH:mm";
	
	/**
	 * @param dateInMillis
	 * @return
	 */
	public static String getDateString(final Long dateInMillis) {
		final Calendar c = Calendar.getInstance(Locale.GERMANY);
		c.setTimeInMillis(dateInMillis);
		final DateFormat f = new SimpleDateFormat(FORMAT_DATE);
		f.setTimeZone(TimeZone.getDefault());
		return f.format(c.getTime());
	}
	
	public static String getTimeString(final Long dateInMillis) {
		final Calendar c = Calendar.getInstance(Locale.GERMANY);
		c.setTimeInMillis(dateInMillis);
		final DateFormat f = new SimpleDateFormat(FORMAT_TIME);
		f.setTimeZone(TimeZone.getDefault());
		return f.format(c.getTime());
	}
	
	
}
