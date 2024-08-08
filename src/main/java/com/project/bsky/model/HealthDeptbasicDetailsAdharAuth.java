package com.project.bsky.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
@Table(name = "HEALTHDEPARTMENTBASICDETAILS_AADHARAAUTH")
public class HealthDeptbasicDetailsAdharAuth implements Serializable {
	
	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name="HEALTHSLNO")
	private Integer healthslNo;
	
	@Column(name = "RATIONCARDNUMBER")
	private String rationcardNumber;
	
	@Column(name = "RATIONCARDTYPE")
	private String rationcardType;
	
	@Column(name = "FULLNAMEENGLISH")
	private String fullNameEnglish;
	
	@Column(name = "FULLNAMEODIYA")
	private String fullNameOdiya;
	
	@Column(name = "AADHAARNUMBER")
	private String aadharrNumber;
	
	@Column(name = "GENDER")
	private String gender;
	
	@Column(name = "SPOUSEFULLNAME")
	private String spouseFullName;
	
	@Column(name = "FATHERFULLNAME")
	private String fatherfullName;
	
	@Column(name = "MOBILENUMBER")
	private String mobileNumber;
	
	@Column(name = "DISTRICT")
	private String district;
	
	@Column(name = "DISTRICTID")
	private Integer districtId;
	
	@Column(name = "BLOCK_ULB")
	private String blockUlb;
	
	@Column(name = "BLOCKID_ULBID")
	private Integer blockidUlbId;
	
	@Column(name = "GP_WARD")
	private String gpWard;
	
	@Column(name = "GPID_WARDID")
	private Integer gpidWardId;
	
	@Column(name = "LOCALITY_VILLAGE")
	private String localityVillage;
	
	@Column(name = "LOCALITYID_VILLAGEID")
	private Integer localityIdVillageId;
	
	
	@Column(name = "FPSNAME")
	private String fpSname;
	
	@Column(name = "SCHEMETYPE")
	private String schemeType;
	
	@Column(name = "STATUS")
	private Integer status;
	
	@Column(name = "ADDITIONDELETIONSTATUS")
	private String additiondeletionStatus;
	
	@Column(name = "EXPORTDATE")
	private Date exportDate;

	@Transient
	private String expoDate;
	
	

}
