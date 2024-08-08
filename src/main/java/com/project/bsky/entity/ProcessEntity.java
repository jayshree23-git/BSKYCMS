package com.project.bsky.entity;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;



@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "M_PROCESS_NAME")
public class ProcessEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="intProcessId")
	private int intProcessId;
	
	@Column(name="vchProcessName")
	private String vchProcessName;
	
	@Column(name="vchProcessCode")
	private String vchProcessCode;
	
	@Column(name="intPayment")
	private Integer intPayment;
	
	@Column(name="intApproval")
	private Integer intApproval;
	

	
	@Column(name="intDocument")
	private Integer intDocument;
	
	
	@Column(name="viewAtCitizen")
	private String viewAtCitizen;
	
	
	
	@Column(name="landUseExist")
	private String landUseExist;
	
	@Column(name="tinProjectAreaExist")
	private String tinProjectAreaExist;
	
	
	@Column(name="dtmCreatedOn")
	private Date dtmCreatedOn;
	
	
	@Column(name="intModuleId")
	private Integer intModuleId;
	
	
	@Column(name="tinStatus")
	private Integer tinStatus;
	
	
	@Column(name="vchProcessClass")
	private String vchProcessClass;
	
	
	@Column(name="tinAccessBy")
	private Integer tinAccessBy;
	
	
	@Column(name="tinPaymentTime")
	private Integer tinPaymentTime;
	
	
	@Column(name="tinDemandNoteExist")
	private Integer tinDemandNoteExist;
	
	
	
	@Column(name="txtOtherInfo")
	private String txtOtherInfo;
	
	
	@Column(name="vchXyzs")
	private String vchXyzs;
	
	
	@Column(name="intCreatedBy")
	private Integer intCreatedBy;
	
	@Column(name="stmUpdatedOn")
	private Date stmUpdatedOn;
	
	@Column(name="intUpdatedBy")
	private Integer intUpdatedBy;
	
	@Column(name="bitDeletedFlag")
	private Integer bitDeletedFlag;
	
	@Column(name="intBOQProcessId")
	private Integer intBOQProcessId;
	
	@Column(name="tinProcessType")
	private Integer tinProcessType;
	
	@Column(name="txtDynCtrlTblNm")
	private String txtDynCtrlTblNm;
	
	
    @Column(name ="vchSection")
	private String vchSection;
	 
	
	@Column(name="vchSchemePoster")
	private String vchSchemePoster;
	
	@Column(name="vchSchemeGuideline")
	private String vchSchemeGuideline;
	
	@Column(name="m_process_namecol")
	private String m_process_namecol;
	
	@Column(name="intServiceMode")
	private Integer intServiceMode;
	
	@Column(name="txtSchemeDescription")
	private String txtSchemeDescription;
	
	
	@JsonIgnore
	@Column(name="vchRedirectURL")
	private String vchRedirectURL;
	
	
	@JsonIgnore
	@Column(name="vchAPIRedirectURLDtls")
	private String vchAPIRedirectURLDtls;
	
	
	@JsonIgnore
	@Column(name="vchAPIReferenceURLDtls")
	private String vchAPIReferenceURLDtls;
	
	@JsonIgnore
	@Column(name="vchAPIStatusURLDtls")
	private String vchAPIStatusURLDtls;
	
	@Column(name="intBaseType")
	private Integer intBaseType;
	
	@Column(name="vchProcessDtls")
	private String vchProcessDtls;
	
	@JsonIgnore
	@Column(name="vchAPIURLDtls" )
	private String vchAPIURLDtls;
	
	@Column(name="vchConfigurationItemsDtls")
	private String vchConfigurationItemsDtls;
	
	@Column(name="tinPublishStatus")
	private Integer tinPublishStatus;
	
	@Column(name="vchFormType")
	private String vchFormType;
	
	@Column(name="vchTableName")
	private String vchTableName;
	
	
	@Column(name="intAdminApplication")
	private Integer intAdminApplication;
	
	
	@Column(name="intWebsiteApplication")
	private int intWebsiteApplication;
	
	@JsonIgnore
	@Column(name="txtFormConfigDetails" )
	private String txtFormConfigDetails;
	
	@Column(name="vchredirectWindowType")
	private String vchredirectWindowType;
	
	@Column(name="formStatus")
	private String formStatus;
	
	@Column(name="tinFormType")
	private Integer tinFormType;
	
	@Column(name="tinFinalSubmitStatus")
	private Integer tinFinalSubmitStatus;
	
	@Column(name="tinGridType")
	private Integer tinGridType;
}
