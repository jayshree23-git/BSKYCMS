package com.project.bsky.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
@Table(name = "USER_DETAILS_CPD_LOG")
public class UserDetailsCpdLog {

	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") // for oracle sequence generate
	@Column(name = "LOG_ID")
	private Integer logId;

	@Column(name = "BSKYUSERID")
	private Integer bskyUserId;

	@Column(name = "FULLNAME")
	private String fullName;

	@Column(name = "USERNAME")
	private String userName;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "MOBILENO")
	private Long mobileNo;

	@Column(name = "CREATEDBY")
	private Integer createdBy;

	@Column(name = "CREATEDDATE")
	private Date createdDate;

	@Column(name = "EMAILID")
	private String emailId;

	@Column(name = "DATEOFJOINING")
	private Timestamp dateofJoining;

	@Column(name = "SPECIALITYID")
	private Integer specialityId;

	@Column(name = "HOSPITALID")
	private Integer hospitalId;

	@Column(name = "DOCTORLICENSENO")
	private String doctorLicenseNo;

	@Column(name = "LASTUPDATEDBY")
	private Integer lastUpdatedBy;

	@Column(name = "LASTUPDATEDDATE")
	private Date lastUpdatedDate;

	@Column(name = "STATUS_FLAG")
	private Integer status;

	@Column(name = "USER_ID")
	private Long userid;
	
	@Column(name = "BANKID")
	private Integer bankId;
	
	@Column(name = "PAYEENAME")
	private String payeeName;
	
	@Column(name = "BANKACCNO")
	private String bankAccNo;
	
	@Column(name = "IFSCCODE")
	private String ifscCode;
	
	@Column(name = "BANKNAME")
	private String bankName;
	
	@Column(name = "BRANCHNAME")
	private String branchName;
	    
	@Column(name = "UPLOADPASSBOOK")
	private String uploadPassbook;
	
	@Column(name = "MAX_CLAIM")
	private Integer maxClaim;
		
}
