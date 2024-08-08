/**
 * 
 */
package com.project.bsky.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * @author rajendra.sahoo
 * 0-Active/Approved
 * 1-Inactive
 * 2-Applied
 * 3-Reject
 */
@Data
@Entity
@Table(name = "USER_HOSPITAL_OPERATOR")
public class HospitalOperator implements Serializable {
	
	@Id	
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_HOSPITAL_OPERATOR_ID_SEQ")
	@SequenceGenerator(name = "USER_HOSPITAL_OPERATOR_ID_SEQ", sequenceName = "USER_HOSPITAL_OPERATOR_ID_SEQ", allocationSize = 1)
	@Column(name = "HOSPITALOPERATOR_ID")
	private Long operatorId;
	
	@Column(name = "HOSPITALCODE")
	private String  hospitalCode;
	
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "USERID")
	private UserDetails userId;
	
	@Column(name = "USERNAME")
	private String userName;
	
	@Column(name = "FULL_NAME")
	private String fullName;
	
	@Column(name = "EMAILID")
	private String email;
	
	@Column(name = "MOBILENO")
	private String mobileNo;
	
	@Column(name = "STATE_CODE")
	private String stateCode;
	
	@Column(name = "DISTRICTCODE")
	private String distCode;
	
	@Column(name = "ADDRESS")
	private String address;	
	
	@Column(name = "STATUS_FLAG")
	private Integer statusFlag;
	
	@Column(name = "CREATED_BY")
	private Long createdBy;
	
	@Column(name = "UPDATED_BY")
	private Long updatedBy;
	
	@Column(name = "CREATED_ON")
	private Date createOn;
	
	@Column(name = "UPDATED_ON")
	private Date updateOn;
	
	@Transient
	private String status;
	
	@Transient
	private String hospitalname;
	
	@Transient
	private String hospmobile;
	
	@Transient
	private String applydate;
}
