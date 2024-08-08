/**
 * 
 */
package com.project.bsky.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author arabinda.guin
 *
 */
public class DateFormat {

	public static String FormatToDateString(String date) {
		String dd = date.substring(0, 2);
		String mm = date.substring(2, 4);
		String yy = date.substring(4);
		String date1 = mm + "-" + dd + "-" + yy;
		return date1;
	}

	public static Date FormatStringToDate(String date) {
		Date date2 = null;
		try {
			String dd = date.substring(0, 2);
			String mm = date.substring(2, 4);
			String yy = date.substring(4);
			String date1 = dd + "-" + mm + "-" + yy;
			return new SimpleDateFormat("dd-MM-yyyy").parse(date1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date2;
	}

	public static String DateString(String date) {
		String dd = date.substring(0, 2);
		String mm = date.substring(2, 4);
		String yy = date.substring(4);
		String date1 = dd + "-" + mm + "-" + yy;
		return date1;
	}

	public static String formatDateFun(String dt) {
		String dd = dt.substring(0, 2);
		String mm = dt.substring(2, 4);
		String yy = dt.substring(4);
		String date1 = dd + "-" + mm + "-" + yy;
		SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM-yyyy");
		Date date = null;
		try {
			date = format1.parse(date1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return format2.format(date);
	}

	public static String formatDateToTime(Date date) {
		return new SimpleDateFormat("HH:mm:ss a").format(date);
	}

	public static String formatDate(Date date) {
		return new SimpleDateFormat("dd-MMM-yyyy").format(date);
	}

	public static String formatDateWithTime(Date date) {
		return new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(date);
	}

	public static String formatStringToDate(String date) {
		try {
			return new SimpleDateFormat("dd-MMM-yyyy").format(new SimpleDateFormat("dd-MMM-yy").parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String dateConvertor(String date, String time) {
		try {
			String inputFormat = "yyyy-MM-dd";
			String outputFormat = "dd-MMM-yyyy";
			if (!date.equals(null) && !date.equalsIgnoreCase("null") && date != null && !date.equals("")) {
				if (!date.contains("-") || !date.contains(" ")) {
					inputFormat = "ddMMyyyy";
				}
				if (time.equals("time")) {
					inputFormat = inputFormat + " hh:mm:ss.SSS";
					outputFormat = outputFormat + " hh:mm:ss a";
				}
				date = new SimpleDateFormat(outputFormat).format(new SimpleDateFormat(inputFormat).parse(date));
			} else {
				date = "N/A";
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return date;
	}
}