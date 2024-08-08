package com.project.bsky.bean;

import com.project.bsky.model.ReferralVitalParameters;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReferralBean {

	private Long refId;

	private Long memberId;

	private String urn;

	private String procedureCode;

	private String packageCode;

	private String hospitalCode;

	private String pdfName;

	private Long noOfDays;

	private Date referralDate;

	private String referralCode;

	private String referralStatus;

	private String approvedBy;

	private Date approvedDate;

	private String remarks;

	private String patientName;

	private Long age;

	private String gender;

	private String regdno;

	private String fromHospitalName;

	private String fromDrName;

	private String fromDeptName;

	private Date fromReferralDate;

	private String stateId;

	private String districtId;

	private String toHospital;

	private String reasonForRefer;

	private Date toReferralDate;

	private String diagnosis;

	private String briefHistory;

	private String treatmentGiven;

	private String investigationRemark;

	private String treatmentAdvised;

	//	private String document;
	private MultipartFile file;

	private Integer createdBy;

	private Date createdOn;

	private Integer updatedBy;

	private Date updatedOn;

	private Integer deletedFlag;

	private String referredThrough;

	private Integer action;

	private Integer userId;

	private String toHospitalCode;
	private String ctrOn;
	private String authStatus;
	private String investigationDoc;

//	public String getDocument() {
//		return document;
//	}
//
//	public void setDocument(String document) {
//		this.document = document;
//	}

	List<ReferralVitalParametersBean> vitalParam;
	private Integer schemeName;
	private Integer categoryName;
}
