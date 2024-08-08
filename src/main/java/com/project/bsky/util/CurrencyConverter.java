package com.project.bsky.util;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyConverter {

//	Input: "123456789", Output : "123,456,789"
	public static String indianCurrencyFormat(String rupees){
		NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
		String currency = format.format(Double.parseDouble(rupees));
//		////System.out.println(currency);
		currency = currency.replaceAll("₹", "");
//		////System.out.println(currency);
		return currency;

}
}
