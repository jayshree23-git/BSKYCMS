package com.project.bsky.bean;

import lombok.Data;

@Data
public class HospitalDoctorprofile {
	private String statecodename;
	private String statename;
	private String districtname;
	private String districtcodename;
	private String hospitalCodename;
	private String hospitalname;
	private String docname;
	private String contactnumber;
	private String regnumber;
	private String dateofjoining;
	public String[] specialitycode;
	public String[] packageheader;
	public Long   userid;
	public Long   profileid;
	private String hospitalCodedata;
	private String speciality_code;
	private String speciality_name;
	private Long speciality_id;
	public Long[] specialityidval;
	
}
