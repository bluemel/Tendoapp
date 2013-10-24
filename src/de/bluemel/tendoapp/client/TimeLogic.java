package de.bluemel.tendoapp.client;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DefaultDateTimeFormatInfo;

public class TimeLogic {

	private static final DateTimeFormat dtf = initDateTimeFormat();

	private static DateTimeFormat initDateTimeFormat() {
		String pattern = "dd.MM.yyyy";
		DefaultDateTimeFormatInfo info = new DefaultDateTimeFormatInfo();
		return new DateTimeFormat(pattern, info) {
		};
	}

	public static String format(final Date date) {
		return dtf.format(date);
	}

	public static Date parse(final String text) {
		return dtf.parse(text);
	}

	public static boolean dateRangesOverlap(Date lower1, Date upper1, Date lower2, Date upper2) {
		final long l1 = lower1.getTime();
		final long u1 = upper1.getTime();
		final long l2 = lower2.getTime();
		final long u2 = upper2.getTime();
		// range 2 within range 1 || range 1 within range 2 || l1 in range 2 || u1 in range 2
		return (l1 < l2 && u1 > u2) || (l2 < l1 && u2 > u1) || (l1 >= l2 && l1 <= u2) || (u1 >= l2 && u1 <= u2);
	}
}
