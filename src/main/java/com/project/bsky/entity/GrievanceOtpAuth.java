/**
 * 
 */
package com.project.bsky.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * @author arabinda.guin
 *
 */
@Data
@Entity
@Table(name="tbl_grievance_otp_auth")
public class GrievanceOtpAuth implements Serializable{
	
	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name = "AUTH_ID")
	private Long authId;
	
	@Column(name = "CREATED_BY")
	private Integer createdBy;
	
	@Column(name = "CREATED_ON")
	private Date createdOn;
	
	@Column(name = "MOBILE_NO")
	private Long mobileNo;
	
	@Column(name = "OTP")
	private Integer otp;
	
	@Column(name = "OTP_VERIFY_STATUS")
	private Character otpVarifierStatus;
	
	@Column(name = "STATUS_FLAG")
	private Character statusFlag; 
	
	@Column(name = "UPDATED_ON")
	private Date updatedOn; 
	
	@Column(name = "UPDATED_BY")
	private Integer updatedBy;
	
}
