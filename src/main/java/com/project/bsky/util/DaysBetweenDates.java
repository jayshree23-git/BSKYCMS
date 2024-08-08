package com.project.bsky.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DaysBetweenDates {
	public static int daysCountBetweenDates(Date ddDate){
		int daysBetween = 0;

//		For Current Date
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MM yyyy");
		LocalDateTime now = LocalDateTime.now();
		String currentDate = dtf.format(now);

//		For Created Date
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd MM yyyy");
		String cDate = sdf1.format(ddDate);

		SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");

//		Days Calculation
		try {
			Date createdDate1 = myFormat.parse(cDate);
			Date currentDate1 = myFormat.parse(currentDate);
			long difference = (createdDate1.getTime()-currentDate1.getTime());
			daysBetween = (int)(difference / (1000*60*60*24));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return daysBetween;
	}
	
	
	public static String getDateTimeDifference(String startDate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = null;
		Date d2 = null;
		String status="";
		int daysBetween =0;
		try {
			
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			String currentDate = dtf.format(now);			
			d1 = format.parse(startDate);
			d2 = format.parse(currentDate);
			long diff = d2.getTime() - d1.getTime();
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);

//			if (diffDays != 0) {
//				return Long.toString(diffDays) + " Days " +Long.toString(diffHours) + " Hours " +Long.toString(diffMinutes) + " Minutes Ago( 3 Days Remaining)";
//			} else if (diffHours != 0 && diffDays == 0) {
//				return Long.toString(diffHours) + " Hours " +Long.toString(diffMinutes) + " Minutes Ago( 3 Days Remaining)";
//			} else if (diffHours == 0 && diffDays == 0 && diffMinutes != 0) {
//				return Long.toString(diffMinutes) + " Minutes Ago( 3 Days Remaining)";
//			} else {
//				return " 1 Minute Ago";
//			}
//			
			  if(diffDays == 0) {
			    	daysBetween = 2;
			    }else if(diffDays == 1) {
			    	daysBetween = 1;
			    }else if(diffDays == 2) {
			    	daysBetween = 0;
			    }else {
			    	daysBetween = (int)diffDays;
			    }
			
//			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//			LocalDateTime now = LocalDateTime.now();
//			String currentDate = dtf.format(now);
//			////System.out.println("Current Data : " + currentDate);
//			
//			d1 = format.parse(startDate);
//			d2 = format.parse(currentDate);
//			long diff = d2.getTime() - d1.getTime();
//			daysBetween = (int)(diff / (1000*60*60*24));
//		    if(daysBetween == 0) {
//		    	daysBetween = 3;
//		    }else if(daysBetween == 1) {
//		    	daysBetween = 2;
//		    }else if(daysBetween == 2) {
//		    	daysBetween = 1;
//		    }
		    
//			long diffMinutes = diff / (60 * 1000) % 60;
//			long diffHours = diff / (60 * 60 * 1000) % 24;
//			long diffDays = diff / (24 * 60 * 60 * 1000);
//
//			if (diffDays != 0) {
//				return Long.toString(diffDays) + " days left";
//			} else if (diffHours != 0 && diffDays == 0) {
//				return Long.toString(diffHours) + " hours left";
//			} else if (diffHours == 0 && diffDays == 0 && diffMinutes != 0) {
//				return Long.toString(diffMinutes) + " minutes left";
//			} else {
//				return " 1 Minute left";
//			}
			
//			status =  TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) +"";
//			if (status.contains("0")) {
//				status = 1 + " days left";
//			} else {
//				status = status+ " days left";
//			}
		} catch (Exception e) {
			e.printStackTrace();
			//LOG.error("DateUtil:: getDateTimeDifference():" + e.getMessage());
		}
		return Integer.toString(daysBetween) +" days left";
	}
	
public static int DaysBetweenDatesCPD_SNA(Date ddDate) {
	 int daysBetween = 0;
//		For Current Date
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MM yyyy");
		LocalDateTime now = LocalDateTime.now();
		String currentDate = dtf.format(now);
//		For Created Date
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd MM yyyy");
		String cDate = sdf1.format(ddDate);
		SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
//		Days Calculation
		try {
			Date createdDate1 = myFormat.parse(cDate);
			Date currentDate1 = myFormat.parse(currentDate);
			long difference = (createdDate1.getTime()-currentDate1.getTime());
			daysBetween = (int)(difference / (1000*60*60*24))+6;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return daysBetween;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
