package com.project.bsky.bean;

import java.util.List;

import lombok.Data;

@Data
public class EnableHospitalDischargeBean {
	private Long snoid;
	private String hospitalcode;
	private String hospitalname;
	private String statename;
	private String distname;
	private String snastatus;
	private Boolean status;
	private List<HospBean> hospobj;
	private List<HospBean> hospobjd;

}
