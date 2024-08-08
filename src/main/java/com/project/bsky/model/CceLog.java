package com.project.bsky.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_PATIENT_BLOCKED_DATA_CCEUSER_LOG")
public class CceLog {
	private String URN;
	@Id
	private Long TRANSACTIONID;
	private String INVOICE;
	private String MEMBERNAME;
	private String PATIENTCONTACTNUMBER;
	private String DISTRICTNAME;
	private String BLOCKNAME;
	private String PANCHAYATNAME;
	private String VILLAGENAME;
	private Date ADMISSIONDATE;
	private String HOSPITALSTATENAME;
	private String HOSPITALSTATECODE;
	private String HOSPITALDISTRICTNAME;
	private String HOSPITALDISTRICTCODE;
	private String HOSPITALNAME;
	private String HOSPITALCODE;
	private Long MEMBERID;
	private Date CREATEDON;
	private String PROCEDURENAME;
	private String PACKAGEHEADERNAME;
	private Long ASSIGNED_BUCKET;
	private Long CCEUSERID;
	private Long STATUS;
	private Date CCEUSER_ALLOTEDDATE;
	private Long TOTALAMOUNTCLAIMED;
	private String CASENO;
	private Integer ACTIONTAKEN;
//	@Id
//	@GenericGenerator(name = "catInc", strategy = "increment")
//	@GeneratedValue(generator = "catInc")
//	@Column(name="ID")
//	private Long id;
//
//	@Column(name="MOBILENUMBERACTIVESTATUS")
//	private String status;
//
//	@Column(name="CALLRESPONSECATEGORY")
//	private String categoryName;
//
//	@Column(name="TRANSID")
//	private Long transactionId;
//
//	@Column(name="URN")
//	private String urn;
//
//	@Column(name="PATIENTNAME")
//	private String patientName;
//
//	@Column(name="INVOICENO")
//	private String invoice;
//
//	@Column(name="STATENAME")
//	private String stateName;
//
//	@Column(name="DISTRICTNAME")
//	private String districtName;
//
//	@Column(name="BLOCKNAME")
//	private String blockName;
//
//	@Column(name="PANCHAYATNAME")
//	private String panchayatName;
//
//	@Column(name="VILLAGENAME")
//	private String villageName;
//
//	@Column(name="DATEOFADMISSION")
//	private Date admissionDate;
//
//	@Column(name="MOBILENO")
//	private String patientContactNumber;
//
//	@Column(name="TOTALAMOUNTBLOCKED")
//	private String totalAmoutClaimed;
//
//	@Column(name="HOSPITALDISTRICT")
//	private String hospitalDistrict;
//
//	@Column(name="HOSPITALNAME")
//	private String hospitalName;
//
//	@Column(name="HOSPITALCODE")
//	private String hospitalCode;
//
//	@Column(name="PACKAGENAME")
//	private String packageName;
//
//	@Column(name="PROCEDURENAME")
//	private String procedureName;
//
//	@Column(name="QUESTION1RESPONSE")
//	private String question1Response;
//
//	@Column(name="QUESTION2RESPONSE")
//	private String question2Response;
//
//	@Column(name="QUESTION3RESPONSE")
//	private String question3Response;
//
//	@Column(name="QUESTION4RESPONSE")
//	private String question4Response;
//
//	@Column(name="DCREMARKS")
//	private String dcRemarks;
//
//	@Column(name="ACTUALDATEOFADMISSION")
//	private Date actualDateOfAdmission;
//
//	@Column(name="ALTERNATEPHONENO")
//	private String alternatePhoneno;
//
//	@Column(name="CREATEDBY")
//	private Integer createdBy;
//
//	@Column(name="CREATEDON")
//	private Date createdOn;
//
//	@Column(name="UPDATEDBY")
//	private Integer updatedBy;
//
//	@Column(name="UPDATEDON")
//	private Date updatedOn;
//
//	@Column(name = "DELETEDFLAG")
//	private Integer deletedFlag;
//
//	@Column(name="ATTEMPTCOUNT")
//	private Integer attemptCount;
//
//	@Column(name="FEEDBACKDATE")
//	private Date feedBackDate;
//
//	@Column(name="EXECUTIVEUSERID")
//	private Long executiveUserId;
//
//	@Column(name="DCUSERID")
//	private Long dcUserId;
//
//	@Column(name="DCSUBMITTEDDATE")
//	private Date dcSubmittedDDate;
//
//	@Column(name="DCUPLOADPDFPATH")
//	private String dcUploadPdfPath;
//
//	@Column(name="NODALOFFICERFLAG")
//	private String nodalOfficerFlag;
//
//	@Column(name="NODALOFFICERDECISIONDATE")
//	private Date nodalOfficerDecisionDate;
//
//	@Column(name="EXECUTIVEREMARKS")
//	private String executiveRemarks;
//
//	@Column(name="ALLOTTEDDATE")
//	private String allottedDate;
//
//	@Column(name="NODALOFFICERUSERID")
//	private Long nodalOfficerUserId;
//
//	@Column(name="DCUPLOADAUDIO")
//	private String dcUploadAudio;
//
//	@Column(name="DCUPLOADVIDEO")
//	private String dcUploadVideo;
//
//	@Column(name = "CONNECTEDSTATUS")
//	private Integer connectedStatus;
//
//	@Column(name="DGOREMARKS")
//	private String dgoRemarks;
//
//	@Column(name="DGODOC")
//	private String dgoDoc;
//
//	@Column(name="GOREMARKS")
//	private String goRemarks;
//
//	@Column(name="DGOUSERID")
//	private Long dgoUserId;
//
//	@Column(name="GOUSERID")
//	private Long goUserId;
//
//	@Column(name="DGOSUBMITTEDDATE")
//	private Date dgoSubmittedDDate;
//
//	@Column(name="GOSUBMITTEDDATE")
//	private Date goSubmittedDDate;
//	@Column(name="REASSIGNREMARK")
//	private String reAssignRemark;
//	@Column(name="REASSIGNDDATE")
//	private Date reAssignDate;
//	@Column(name="REASSIGNFLAG")
//	private Integer reAssignFlag;
//
//	@Column(name="REASSIGNCONNECTEDSTATUS")
//	private Integer reAssignConnectedStatus;
//	@Column(name="REASSIGNUSERID")
//	private Integer reAssignUserId;
}
