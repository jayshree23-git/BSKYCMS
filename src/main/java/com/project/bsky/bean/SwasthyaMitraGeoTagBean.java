package com.project.bsky.bean;

import java.util.List;

import lombok.Data;

@Data
public class SwasthyaMitraGeoTagBean {

	private Integer actionCode;
	private Integer createdBy;
	private Integer userId;
	private List<SwasthyaMitraGeoTagAttendanceBean> attendance;
}
