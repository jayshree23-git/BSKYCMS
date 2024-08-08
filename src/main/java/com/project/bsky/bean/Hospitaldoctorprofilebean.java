package com.project.bsky.bean;

import lombok.Data;

@Data
public class Hospitaldoctorprofilebean {
	private String hospitalcode;
	private String hospitalname;
	private String statecode;
	private String statename;
	private String districtcode;
	private String districtname;
	
	//Speciality
	private Long id;
	private String packageheadercode;
	private String packageheader;
	
}
