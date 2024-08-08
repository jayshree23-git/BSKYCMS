package com.project.bsky.bean;

import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
public class CceDto {

    private Integer pendingDcCount;
    private Integer pendingDgoCount;
    private Integer pendingGoCount;
    private Integer actionDcCount;
    private Integer actionDgoCount;
    private Integer actionCceCount;
    private String dgoQueryRemarks;
    private String dgoQueryDoc;
    private String goStatus;
    private String goQueryRemarks;
    private String dgoReplay;
    private Integer goQueryCount;
    private String pendingAt;
    private String actionBy;
    private boolean statusFlag;
    private Long id;
    private String status;

    private String categoryName;

    private Long transactionId;

    private String urn;

    private String patientName;

    private String invoice;

    private String stateName;

    private String districtName;

    private String blockName;

    private String panchayatName;

    private String villageName;

    private String admissionDate;

    private String patientContactNumber;

    private String totalAmoutClaimed;

    private String hospitalDistrict;

    private String hospitalName;

    private String hospitalCode;

    private String packageName;

    private String procedureName;

    private String question1Response;

    private String question2Response;

    private String question3Response;

    private String question4Response;

    private String dcRemarks;

    private String actualDateOfAdmission;

    private String alternatePhoneno;

    private Integer createdBy;

    private Date createdOn;

    private Integer updatedBy;

    private Date updatedOn;

    private Integer deletedFlag;

    private Integer attemptCount;

    private String feedBackDate;

    private Long executiveUserId;

    private Long dcUserId;

    private String dcSubmittedDDate;

    private String dcUploadPdfPath;

    private String nodalOfficerFlag;

    private String nodalOfficerDecisionDate;

    private String executiveRemarks;

    private String allottedDate;

    private Long nodalOfficerUserId;

    private String dcUploadAudio;

    private String dcUploadVideo;

    private Integer connectedStatus;

    private String dgoRemarks;

    private String dgoDoc;

    private String goRemarks;

    private Long dgoUserId;

    private Long goUserId;

    private String dgoSubmittedDDate;

    private String goSubmittedDDate;

    private Long memberId;

    private String caseNo;

    private String reAssignRemark;

    private Date reAssignDate;

    private Integer reAssignFlag;
    
    private Integer showction;
    private Integer expiryStatus;
    private String dcReply;

}
