/**
 * 
 */
package com.project.bsky.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * @author rajendra.sahoo
 *
 */
@Data
@Entity
@Table(name = "HOSPITAL_INFO_REFERRAL_LOG")
public class ReferralHospitalLog {
	
	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") // for oracle sequence generate
	@Column(name = "HOSPITALLOGID")	
	private Long hospitallogid;
	
	@Column(name = "HOSPITALID")	
	private Long hospitalid;
	
	@Column(name = "STATECODE")
	private String statecode;
	
	@Column(name = "DISTCODE")
	private String distcode;
	
	@Column(name = "BLOCKCODE")
	private String blockcode;
	
	@Column(name = "HOSPITALTYPE")
	private Integer hospitaltype;
	
	@Column(name = "HOSPITALNAME")
	private String hospitalname;
	
	@Column(name = "CONTACTPERSONNAME")
	private String cnctperson;
	
	@Column(name = "MOBILENO")
	private String mobileno;
	
	@Column(name = "ADDRESS")
	private String address;
	
	@Column(name = "CREATEDBY")
	private Long createdby;
	
	@Column(name = "CREATEDON")
	private Date createdon; 
	
	@Column(name = "UPDATEDBY")
    private Long updatedby;
	
	@Column(name = "UPDATEDON")
	private Date updatedon; 
	
	@Column(name = "STATUSFLAG")
	private Integer status;
	
}
