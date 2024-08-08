/**
 * 
 */
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

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * @author rajendra.sahoo
 *
 */

/**
 * ATTENDANCE_STATUS (attendancestatus)
 *   0 =Absent 
 *   1 =Present
 *   2 =On Leave
 *   3 =Holiday
 *	 4 =
 *   5=
 */


@Data
@Entity
@Table(name = "SWATHYAMITRA_ATTENDNACE_DTLS")
public class SwasthyamitraAttendance implements Serializable {

	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name = "ATTENDANCE_ID")
	private Long attendance;
	
//	@ManyToOne
//	@JoinColumn(name = "USER_ID")
	@Column(name = "USER_ID")
	private Integer userId;
	
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Kolkata")
	@Column(name = "LOGIN_TIME")
	private Date punchInTime;
	
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Kolkata")
	@Column(name = "LOGOUT_TIME")
	private Date punchOutTime;
	
	@Column(name = "STATUSFLAG")
	private Integer statusflag;
	
	@Column(name = "ATTENDANCE_STATUS")
	private Integer attendancestatus;	
	
	@JsonFormat(pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	@Column(name = "ATTENDANCE_DATE")
	private Date date;
	
	@Transient
	private String Statusdata;
	
}
