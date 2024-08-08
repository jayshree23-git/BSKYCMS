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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.transaction.Transactional;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * @author rajendra.sahoo
 *
 */

@Entity
@Data
@Table(name = "CPD_LEAVE_INFO")
public class CPDLeaveInfo implements Serializable {

	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") //for oracle sequence generate
	@Column(name = "LEAVE_ID")
	private Long leaveId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="CPD_USER_ID")
//	@Column(name = "CPD_USER_ID")
	private UserDetailsCpd cpduserId;
	
	@Column(name = "FROM_DATE")
	private Date formdate;
	
	@Column(name = "TO_DATE")
	private Date todate;
	
	@Column(name = "REMARKS")
	private String remarks;
	
	@Column(name = "STATUS")
	private Integer status;
	
	@Column(name = "ACTION_TAKEN_BY")
	private Long actiontakenby;
	
	@Column(name = "ACTION_TAKEN_ON")
	private Date actiontakenon;
	
//	@Column(name = "CREATED_BY")
//	private Long createdby;
	@ManyToOne
	@JoinColumn(name = "CREATED_BY")
	private UserDetails createdby;
	
	@Column(name = "CREATED_ON")
	private Date createon;
	
	@Column(name = "ASSIGNED_SNA")
	private Long assignedsna;
	
	@Transient
	private String stodate;
	
	@Transient
	private String sformdate;
	
	@Transient
	private String screateon;
	
	@Transient
	private Long noofdays;
	
}
