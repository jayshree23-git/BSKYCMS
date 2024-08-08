package com.project.bsky.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
@Entity
@Data
@Table(name="m_process_name")
public class MProcessName implements Serializable {
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer intProcessId;
	private String vchProcessName; 
	private String vchProcessCode;
	private Integer intPayment;
	private Integer intApproval;
	private Integer intDocument;
	private Integer viewAtCitizen;
	private Integer landUseExist;
	private Integer tinProjectAreaExist;
	private Date dtmCreatedOn;
	private Integer intModuleId;
	private Integer tinStatus;
	private Integer tinLinkType;
	private String vchProcessClass;
	private Integer tinAccessBy;
	private Integer tinPaymentTime;
	private Integer tinDemandNoteExist;
	private String txtOtherInfo;
	private Integer vchXyzs;
	private Integer intCreatedBy;
	private Date stmUpdatedOn;
	private Integer intUpdatedBy;
	private Integer bitDeletedFlag;
	private Integer intBOQProcessId;
	private Integer tinProcessType; 
	private String txtDynCtrlTblNm;
	private String vchSection;
	private String vchSchemePoster;
	private String vchSchemeGuideline;
	private String m_process_namecol; 
	private Integer intServiceMode;
	private String txtSchemeDescription;
	private String vchRedirectURL;
	private String vchAPIRedirectURL;
	private String vchAPIRedirectURLDtls; 
	private String vchAPIReferenceURLDtls;
	private String vchAPIStatusURLDtls;
	private Integer intBaseType;
	private String vchProcessDtls;
	private String vchAPIURLDtls;
	private String vchConfigurationItemsDtls;
	private Integer tinPublishStatus;
	private Integer vchChangeStatus;
	private String vchFormType;
	private String vchTableName;
	private Integer intAdminApplication;
	private Integer intWebsiteApplication; 
	private String txtFormConfigDetails;
	private String vchredirectWindowType; 
	private Integer formStatus;
	private Integer tinFormType;
	private Integer tinFinalSubmitStatus;
	private Integer tinGridType;
	@OneToMany
	@JoinColumn(name= "itemId")
	private List<DynamicFormConfigurationTemp> DynamicFormList;
}
