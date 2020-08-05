package com.demo.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateOperator {
	
	public static String getCurrentDate(){
		
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}
	
	public static String convertDateToString(Date date){
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}
	
}
