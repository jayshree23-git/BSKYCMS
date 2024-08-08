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
@Table(name="t_set_authority")
public class TSetAuthority implements Serializable {
	@Id	
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="T_SET_AUTHORITY_SEQ")
	@SequenceGenerator(name="T_SET_AUTHORITY_SEQ", sequenceName="T_SET_AUTHORITY_SEQ", allocationSize=1)
	private Integer intSetAuthId;
	private Integer intHierarchyId;
	private Integer intProcessId;
	private Integer intProjectId;
	private Integer intSubParamC1;
	private Integer intSubParamC2; 
	private String vchSubStage; 
	private Integer tinStageNo;
	private Integer intRoleId;
	private Integer intPrimaryAuth; 
	private Integer tinTimeLine;
	private Integer tinSignature;
	private Integer intSignatoryAuth;
	private String vchAuthTypes; 
	private Integer tinDelegateStatus;
	private Date dtmCreatedOn;
	private Date stmUpdatedOn; 
	private Integer intCreatedBy;
	private Integer intUpdatedBy;
	private Byte bitDeletedFlag; 
	private Integer intATAProcessId;
	private Integer intApprovingAuth; 
	@Column(length = Integer.MAX_VALUE)
	private Integer intSetAuthLinkId;
	@Column(length = Integer.MAX_VALUE)
	private Integer intSetLetterLinkId;
	private Integer tinAutoEscalate;
	private Integer tinDemandNotePaymentStatus; 
	private String vchCtrlName;
	private Integer intLabelId;
	private Integer tinType; 
//	@Lob
	@Column(length=10485760)
	private String jsnApprovalDocument; 
	private String vchPcategoryName;
	@Column(length=10485760)
	private String jsnVerifyDocument;
}
