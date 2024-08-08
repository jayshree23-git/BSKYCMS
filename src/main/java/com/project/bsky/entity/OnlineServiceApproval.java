package com.project.bsky.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name="T_ONLINE_SERVICE_APPROVAL")
@Entity
public class OnlineServiceApproval {
	
	@Id
	@Column(length=10485760)
	private Integer intOnlineServiceApprovalId;
	@Column(length=10485760)
	private Integer intOnlineServiceId;
	@Column(length=10485760)
	private Integer intATAProcessId;
	@Column(name="INTSTAGENO",length=10485760)
	private Integer intStageNo;
	@Column(length=10485760)
	private String intPendingAt;
	@Column(length=10485760)
	private Integer intForwardTo;
	@Column(length=10485760)
	private Integer intSentFrom;
	@Column(length=10485760)
	private Date dtmStatusDate;
	@Column(length=10485760)
	private Integer tinStatus;
	@Column(length=10485760)
	private Integer intAssistantId;
	@Column(length=10485760)
	private Integer tinQueryTo;
	@Column(length=10485760)
	private Integer tinResubmitStatus;
	@Column(length=10485760)
	private Date stmCreatedOn;
	@Column(length=10485760)
	private Integer intCreatedBy;
	@Column(length=10485760)
	private Date dtmUpdatedOn;
	@Column(length=10485760)
	private Integer intUpdatedBy;
	private Integer bitDeletedFlag;
	@Column(length=10485760)
	private Integer intProcessId;
	@Column(length=10485760)
	private Date dtmApprovalDate;
	@Column(length=10485760)
	private String vchApprovalDoc;
	@Column(length=10485760)
	private Integer intApproveDocIndexId;
	@Column(length=10485760)
	private Integer tinDemandNoteGenStatus;
	@Column(length=10485760)
	private Integer tinVerifiedBy;
	@Column(length=10485760)
	private String vchATAAuths;
	@Column(length=10485760)
	private String vchRemainingATA;
	@Column(length=10485760)
	private Integer tinDemandNoteApplicableStatus;
	@Column(length=10485760)
	private Date dtmDemandNoteDate;
	@Column(length=10485760)
	private Integer tinVerifyLetterGenStatus;


}
