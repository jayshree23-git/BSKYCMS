package com.project.bsky.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultSetToListConverter {
	
	public static List<Map<String, Object>> getListFromResultSet(ResultSet rs) {
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		if (rs != null) {
			try {
				ResultSetMetaData md = rs.getMetaData();
				
				int noOfColumns = md.getColumnCount();
				 
				while(rs.next()) {
					Map<String, Object> row = new HashMap<String, Object>();
					for(int i=1; i<= noOfColumns; i++) {
						row.put(md.getColumnName(i), rs.getObject(i));
					}
					rows.add(row);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return rows;
	}

	public static List<Map<String, Object>> getListFromResultSetForStringType(ResultSet rs) {
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		if (rs != null) {
			try {
				ResultSetMetaData md = rs.getMetaData();
				int noOfColumns = md.getColumnCount();
				while (rs.next()) {
					Map<String, Object> row = new HashMap<String, Object>();
					for (int i = 1; i <= noOfColumns; i++) {
						row.put(md.getColumnName(i), rs.getString(i));
					}
					rows.add(row);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return rows;
	}

}
