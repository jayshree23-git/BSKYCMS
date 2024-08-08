package com.project.bsky.bean;

import java.util.List;

import lombok.Data;

@Data
public class HospitalBean {

	private Integer hospitalId;
	private String hospitalName;
	private String hospitalCode;
	private String mobileNo;
	private Integer status;
	private String emailId;
	private String stateName;
	private String stateCode;
	private String districtName;
	private String districtCode;
	private Integer cpdApprovalRequired;
	private Long assigndc;
	private String dcname;
	private Integer assignsna;
	private String snaname;
	private String longitude;
	private String latitude;
	private String createon;
	private String updateon;
	private String hospitalType;
	private Integer count;
	private String tmsActiveStat;
	
	private String hcvalidform;
	private String hcvalidto;
	private String moustartdate;
	private String mouenddate;
	private String backadmissionday;
	private String backdischargeday;
	private Character appstatus;
	
	private List<HospitalDetailBean> hospdetailbean;
	
}