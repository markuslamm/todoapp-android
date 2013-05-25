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
	private static final Locale LOCALE = Locale.GERMANY;
	
	private DateHelper() { }
	/**
	 * @param dateInMillis
	 * @return
	 */
	public static String getDateString(final Long dateInMillis) {
		final Calendar c = Calendar.getInstance(LOCALE);
		c.setTimeInMillis(dateInMillis);
		final DateFormat f = new SimpleDateFormat(FORMAT_DATE, LOCALE);
		f.setTimeZone(TimeZone.getDefault());
		return f.format(c.getTime());
	}
	
	public static String getTimeString(final Long dateInMillis) {
		final Calendar c = Calendar.getInstance(LOCALE);
		c.setTimeInMillis(dateInMillis);
		final DateFormat f = new SimpleDateFormat(FORMAT_TIME, LOCALE);
		f.setTimeZone(TimeZone.getDefault());
		return f.format(c.getTime());
	}
	
	/**
	 * @param dateInMillis
	 * @return
	 */
	public static String generateDateTimeString(final Long dateInMillis) {
		final String date = DateHelper.getDateString(dateInMillis);
		final String time = DateHelper.getTimeString(dateInMillis);
		return new StringBuffer(date).append(" | ").append(time).toString();
	}
	
	
}
