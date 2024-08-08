package com.project.bsky.util;

import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;

public class ConvertClobToJson {
	public static String convertClobToJSONString(Clob clob) {
		Reader r = null;
		try {
			r = clob.getCharacterStream();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		StringBuffer buffer = new StringBuffer();
		int ch;
		try {
			while ((ch = r.read())!=-1) {
			   buffer.append(""+(char)ch);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}
}
