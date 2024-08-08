package com.project.bsky.bean;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Createcpdbean {
	
	private MultipartFile file;
	private String branchname;
	private String fullName;
	private String userName;
	private String mobile;
	private String email;
	private String districtname;
	private String date;
	private String State;
//	private String hospital;
	private String  Doctor;
	private String Payee;
	private String BankACC;
	private String ifscCode;
	private String bankName;
	private String createdby;
	
	
//	fullname:new FormControl(''),
//    Username:new FormControl(''),
//    createdby:new FormControl(''),
//    mobile:new FormControl(''),
//    email:new FormControl(''),
//    date:new FormControl(''),
//    State:new FormControl(''),
//    district:new FormControl(''),
//    hospital:new FormControl(''),
//    Doctor:new FormControl(''),
//    Payee:new FormControl(''),
//    BankACC:new FormControl(''),
//    ifscCode:new FormControl(''),
//    bankName:new FormControl(''),
//    branchname:new FormControl(''),
//    file:new FormControl('')
	

}
