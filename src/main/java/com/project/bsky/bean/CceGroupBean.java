package com.project.bsky.bean;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CceGroupBean {

    private Integer createdBy;

    private String stateName;

    private String hospitalCode;

    private Date feedBackDate;

    private Long dcUserId;

    private Date dcSubmittedDDate;

    private String dcUploadPdfPath;

    private String nodalOfficerFlag;

    private Date nodalOfficerDecisionDate;

    private String executiveRemarks;

    private Long nodalOfficerUserId;

    private String dcUploadAudio;

    private String dcUploadVideo;

    private String question1Response;

    private String question2Response;

    private String question3Response;

    private String question4Response;

    private String dcRemarks;

    private String alternatePhoneno;

    private Integer attemptCount;

    private String que1;
    private String que2;
    private String que3;
    private String que4;
    private String altNo;
    private String remk;
    List<TransactionInformationDto> ccelist;

}
