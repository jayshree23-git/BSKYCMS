package com.project.bsky.util;

import java.util.Random;

public class OTPGenerator {

	public static String otpGenerator() {
		try {
			Random rnd = new Random();
			int number = rnd.nextInt(999999);
			return String.format("%06d", number);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String generateOTP() {
		Random random = new Random();
		try {
			int number = 100000 + random.nextInt(900000);
			return String.valueOf(number);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
