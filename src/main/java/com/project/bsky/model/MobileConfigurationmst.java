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
import javax.persistence.Table;

import lombok.Data;

/**
 * Rajendra
 */
@Entity
@Data
@Table(name = "TBL_MOBILE_ATTENDANCE_GROUP")
public class MobileConfigurationmst {

	@Id
	@Column(name = "CONFIGURATION_ID",unique = true,nullable = false)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long configid;
	
	@Column(name = "GROUPID")
	private Long groupid;
	
	@Column(name = "MOBILE_ATTENDANCE_STATUS")
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
