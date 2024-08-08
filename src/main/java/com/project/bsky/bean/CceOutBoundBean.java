package com.project.bsky.bean;

import lombok.Data;

@Data
public class CceOutBoundBean {
    private String urn;
    private String patientName;
    private String mobileNo;
    private String districtName;
    private String blockName;
    private String panchayatName;
    private String villageName;
    private String callResponse;
    private  String invoiceNo;
    private String dateOfAdm;
    private Integer totalAmountBlocked;
    private String hospitalDist;
    private  String hospitalName;
    private String procedureName;
    private String packageName;
    private String alottedDate;
    private String alternativeNo;
    private String transId;
    private String question1Response;

    private String question2Response;

    private String question3Response;

    private String question4Response;
    private String executiveRemarks;
    private Long cceId;
    private String mobileActiveStatus;
    private boolean status;
    private String hospitalCode;
    private Integer completed;
    private Integer yes;
    private Integer no;
    private Integer pending;
    private Integer positive;
    private Integer negetive;
    private Integer support;
    private Integer behaviour;
    private Integer bribe;
    private Integer freshCase;
    private Integer notConnected;
    private Integer supportPercent;
    private Integer behaviourPercent;
    private Integer bribePercent;
    private Integer freshPercent;
    private Integer notConnectedPercent;
    private String doc1;
    private String doc2;
    private String doc3;
    private String dcRemarks;
    private String dgoDoc;
    private String dgoRemarks;
    private Integer facilitate;
    private String createdOn;
    private Integer totalData;
    private String feedbackLoginId;
    private Integer reportCheck;
    private Integer dialedCount;
    private String reAssignRemark;
    private Integer reAssignFlag;
    private String dcSubmittedDate;
    private String DCUserId;
    private String dgoQueryRemarks;
    private String dgoQueryDoc;
    private Integer querySentDGO;
    private String dgoQueryDate;
    private String dcReplay;
    private String dgoReplay;
    private String dcAction;
    private String dgoAction;
    private String goAction;
    private Integer dgoQueryCount;
    private String goQueryRemarks;
    private Integer fresh;
    private String state;
    private Integer ITAFlag;
    private String goQueryDate;
    private String dgoITARemark;
    private Integer dgoITAFlag;
    private Integer expiryStatus;
}
