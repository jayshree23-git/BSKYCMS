package com.project.bsky.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name="t_set_authority")
@Entity
public class AuthorityEntity {
	
	@Id
	private int intSetAuthId;
	private int intHierarchyId;
	private int intProcessId;
	private int intProjectId;
	private int intSubParamC1;
	private int intSubParamC2;
	private String vchSubStage;
	private int tinStageNo;
	private int intRoleId;
	private int intPrimaryAuth;
	private int tinTimeLine;
	private int tinSignature;
	private String vchAuthTypes;
	private Date stmUpdatedOn;
	private int intCreatedBy;
	private int bitDeletedFlag;
	private int intATAProcessId;
	private int intSetAuthLinkId;
	private int intSetLetterLinkId;
	private int tinAutoEscalate;
	private int tinDemandNotePaymentStatus;
	private String vchCtrlName;
	private int intLabelId;
	private int tinType;
	private String jsnApprovalDocument;
	private String vchPcategoryName;
	
	

}
