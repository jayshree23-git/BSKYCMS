package com.project.bsky.entity;

import java.sql.Clob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Table(name="t_online_serv_app_notings")
@Entity
public class OnlineServiceApprovalNotings {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "T_ONLINE_SERV_APP_NOTINGS_SEQ")
	@SequenceGenerator(name = "T_ONLINE_SERV_APP_NOTINGS_SEQ", sequenceName = "T_ONLINE_SERV_APP_NOTINGS_SEQ", allocationSize = 1)
	@Column(name = "INTNOTINGSID")
	private Integer INTNOTINGSID;

	@Column(name = "INTONLINESERVICEID")
	private Integer INTONLINESERVICEID;

	@Column(name = "INTPROFILEID")
	private Integer INTPROFILEID;

	@JsonIgnore
	@Column(name = "INTFROMAUTHORITY")
	private Integer INTFROMAUTHORITYID;
	
	@Transient
	private String INTFROMAUTHORITY;

	@Column(name = "INTTOAUTHORITY")
	private String INTTOAUTHORITY;

	@Column(name = "DTACTIONTAKEN")
	private Date DTACTIONTAKEN;

	@Column(name = "INTSTATUS")
	private Integer INTSTATUS;

	@JsonIgnore
	@Column(name = "TXTNOTING",length=10485760)
	private String TXTNOTINGOriginal;
	@Column(name = "VCHPRIORITY")
	private String priority;
	
	@Transient
	private String TXTNOTING;

	@Column(name = "TINRESUBMITSTATUS")
	private Integer TINRESUBMITSTATUS;

	@JsonIgnore
	@Column(name = "TXTREVERTREMARK")
	private Clob TXTREVERTREMARKOriginal;
	
	@Transient
	private String TXTREVERTREMARK;

	@Column(name = "DTMRESUBMITDATE	")
	private Date DTMRESUBMITDATE;

	@Column(name = "TINSTAGECTR	")
	private Integer TINSTAGECTR;

	@Column(name = "TINQUERYTO")
	private Integer TINQUERYTO;

	@Column(name = "VCHIPADDRESS")
	private String VCHIPADDRESS;

	@Column(name = "DTMCREATEDON")
	private Date DTMCREATEDON;

	@Column(name = "INTCREATEDBY")
	private Integer INTCREATEDBY;

	@Column(name = "BITDELETEDFLAG")
	private Integer BITDELETEDFLAG;

	@Column(name = "INTPROCESSID")
	private Integer INTPROCESSID;

	@JsonIgnore
	@Column(name = "JSNOTHERDETAILS",length=10485760)
	private String JSNOTHERDETAILSOriginal;
	
	@Transient
	private String JSNOTHERDETAILS;

	@JsonIgnore
	@Column(name = "JSNOMEDIADETAILS",length=10485760)
	private String JSNOMediaDETAILSOriginal;



}
