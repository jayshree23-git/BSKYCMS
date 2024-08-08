package com.project.bsky.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
@Data
@Entity
@Table(name="t_online_service_approval")
public class TOnlineServiceApproval implements Serializable {
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="t_online_service_approval_seq")
	@SequenceGenerator(name="t_online_service_approval_seq", sequenceName="t_online_service_approval_seq", allocationSize=1)
	private Integer intOnlineServiceApprovalId;
	private Integer intOnlineServiceId;
	private Integer intProfileId;
	private Integer intATAProcessId;
	@Column(name="INTSTAGENO")
	private Integer intStageNo;
	private String intPendingAt;
	private Integer intForwardTo; 
	private Integer intSentFrom; 
	private Date dtmStatusDate;
	private Integer tinStatus;
	private Integer intAssistantId; 
	private Integer tinQueryTo;
	private Integer tinResubmitStatus; 
	private Date stmCreatedOn;
	private Integer intCreatedBy; 
	private Date dtmUpdatedOn; 
	private Integer intUpdatedBy; 
	private Byte bitDeletedFlag;
	private Integer intProcessId;
	private Date dtmApprovalDate;
	private String vchApprovalDoc; 
	private Integer intApproveDocIndexId;
	private Integer tinDemandNoteGenStatus;
	private Integer tinVerifiedBy; 
	private String vchATAAuths;
	private String vchRemainingATA; 
	private Integer tinDemandNoteApplicableStatus; 
	private Date dtmDemandNoteDate; 
	private Integer tinVerifyLetterGenStatus;

}
