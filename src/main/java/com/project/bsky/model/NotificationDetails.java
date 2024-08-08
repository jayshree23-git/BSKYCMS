package com.project.bsky.model;

import java.io.Serializable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
@Table(name ="M_NOTIFICATION_DETAILS")
public class NotificationDetails implements Serializable {
	
	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name="NOTIFICATION_ID")
	private Long notificationId;
	
	@Column(name = "NOTICE_CONTENT")
	private String noticeContent;
	
	@Column(name = "FROM_DATE")
	private Date fromDate;
	
	@Column(name = "TODATE")
	private Date toDate;
	
	@Column(name = "STATUSFLAG")
	private Integer statusFlag;
	
	@ManyToOne
	@JoinColumn(name = "GROUPID", referencedColumnName = "TYPE_ID")
//	@Column(name = "GROUPID")
	private GroupTypeDetails groupId;
	
	@ManyToOne
	@JoinColumn(name = "CREATEDBY")
//	@Column(name = "CREATEDBY")
	private UserDetails createdBy;
	
	@Column(name = "UPDATEDBY")
	private Long updatedBy;
	
	@Column(name = "CREATEDON")
	private Date createdOn;
	
	@Column(name = "UPDATEDON")
	private Date updateOn;
	
	@Column(name = "DOCPATH")
	private String docpath;
	
	@Column(name = "POPUP_FLAG")
	private Long popupFlag;
	
	@Transient
	private String fdate;
	
	@Transient
	private String tdate;
	
	@Transient
	private String screate;
	
	@Transient
	private String sgroup;
	
	
	
	

}
