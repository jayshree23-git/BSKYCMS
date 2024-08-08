/**
 * 
 */
package com.project.bsky.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

/**
 * Rajendra
 */
@Entity
@Data
@Table(name = "TBL_USER_MOBILE_ACTIVITY_STATUS")
public class AllowUserForHospitalVisit {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TBL_USER_MOBILE_ACTIVITY_STATUS_ID_SEQ")
	@SequenceGenerator(name = "TBL_USER_MOBILE_ACTIVITY_STATUS_ID_SEQ", sequenceName = "TBL_USER_MOBILE_ACTIVITY_STATUS_ID_SEQ", allocationSize = 1)
	@Column(name = "STATUS_ID")
	private Long statusid;
	
	@Column(name = "GROUPID")
	private Long groupid;
	
	@Column(name = "HOSPITAL_VISIT_STATUS")
	private Integer visitstatus;
	
	@Column(name = "FACE_ATTENDANCE_STATUS")
	private Integer attendancestatus;
	
	@Column(name = "CREATEDON")
	private Date createon;
	
	@Column(name = "CREATEDBY")
	private Long createby;
	
	@Column(name = "UPDATEDON")
	private Date updateon;
	
	@Column(name = "UPDATEDBY")
	private Long updateby;
	
	@Column(name = "STATUSFLAG")
	private Integer statusflag;
}
