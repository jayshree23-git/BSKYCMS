package com.project.bsky.bean;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class UserLocationBeanMapper implements RowMapper<UserLocation> {

	@Override
	public UserLocation mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserLocation loc = new UserLocation();
		loc.setDistId(rs.getString("DISTRICT_CODE"));
		loc.setStateId(rs.getString("STATE_CODE"));
		return loc;
	}

}
