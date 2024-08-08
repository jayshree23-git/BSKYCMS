package com.project.bsky.bean;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ClaimRaiseInsert {

	private Long  transaction;
	private String UrnNumber;
	private String invoiceNumber;
	private String hospitalCode;
	private String PENDINGAT;
	private String CLAIMSTATUS;
	private MultipartFile TreatmentDetailsSlip;
	private MultipartFile HospitalBill;
	private MultipartFile presurgry;
	private MultipartFile postsurgery;
	private String CREATEDON;
	private String packageCode;
	private String refractionid;
	private String STATUSFLAG;
	private String name;
	private String gender;
	private String age;
	private String dateofAdmission;
	
	

}
