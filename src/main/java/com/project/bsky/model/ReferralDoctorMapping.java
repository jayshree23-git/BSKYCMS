/**
 * 
 */
package com.project.bsky.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * @author rajendra.sahoo
 *
 */
@Entity
@Table(name = "USER_REFERAL_DOCTOR_MAPPING")
@Data
public class ReferralDoctorMapping {

	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") // for oracle sequence generate
	@Column(name = "REFERAL_DOCTOR_MAPPING_ID")	
	private Long doctormappingid;
	
	@Column(name = "REFERAL_DOCTOR_ID")	
	private Long doctorid;
	
	@Column(name = "USERID")
	private Long userid;
	
	@Column(name = "STATECODE")
	private String statecode;
	
	@Column(name = "DISTRICTCODE")
	private String distcode;
	
	@Column(name = "BLOCKCODE")
	private String blockcode;
	
	@Column(name = "HOSPITAL_CODE")
	private String hospitalcode;
	
	@Column(name = "HOSPITALTYPE")
	private String hospitaltype;
	
	@Column(name = "CREATED_ON")
	private Date createon;	
	
	@Column(name = "CREATED_BY")
	private Long createby;
	
	@Column(name = "UPDATED_ON")
	private Date updateon;
	
	@Column(name = "UPDATED_BY")
	private Long updateby;
	
	@Column(name = "STATUS_FLAG")
	private Integer status;
}
