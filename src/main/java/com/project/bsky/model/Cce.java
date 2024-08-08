package com.project.bsky.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TBL_PATIENTBLOCKEDDATAFEEDBACK")
public class Cce implements Serializable {

    @Id
    @GenericGenerator(name = "catInc", strategy = "increment")
    @GeneratedValue(generator = "catInc")
    @Column(name = "ID")
    private Long id;

    @Column(name = "MOBILENUMBERACTIVESTATUS")
    private String status;

    @Column(name = "CALLRESPONSECATEGORY")
    private String categoryName;

    @Column(name = "TRANSID")
    private Long transactionId;

    @Column(name = "URN")
    private String urn;

    @Column(name = "PATIENTNAME")
    private String patientName;

    @Column(name = "INVOICENO")
    private String invoice;

    @Column(name = "STATENAME")
    private String stateName;

    @Column(name = "DISTRICTNAME")
    private String districtName;

    @Column(name = "BLOCKNAME")
    private String blockName;

    @Column(name = "PANCHAYATNAME")
    private String panchayatName;

    @Column(name = "VILLAGENAME")
    private String villageName;

    @Column(name = "DATEOFADMISSION")
    private Date admissionDate;

    @Column(name = "MOBILENO")
    private Long patientContactNumber;

    @Column(name = "TOTALAMOUNTBLOCKED")
    private String totalAmoutClaimed;

    @Column(name = "HOSPITALDISTRICT")
    private String hospitalDistrict;

    @Column(name = "HOSPITALNAME")
    private String hospitalName;

    @Column(name = "HOSPITALCODE")
    private String hospitalCode;

    @Column(name = "PACKAGENAME")
    private String packageName;

    @Column(name = "PROCEDURENAME")
    private String procedureName;

    @Column(name = "QUESTION1RESPONSE")
    private String question1Response;

    @Column(name = "QUESTION2RESPONSE")
    private String question2Response;

    @Column(name = "QUESTION3RESPONSE")
    private String question3Response;

    @Column(name = "QUESTION4RESPONSE")
    private String question4Response;

    @Column(name = "DCREMARKS")
    private String dcRemarks;

    @Column(name = "ACTUALDATEOFADMISSION")
    private Date actualDateOfAdmission;

    @Column(name = "ALTERNATEPHONENO")
    private String alternatePhoneno;

    @Column(name = "CREATEDBY")
    private Integer createdBy;

    @Column(name = "CREATEDON")
    private Date createdOn;

    @Column(name = "UPDATEDBY")
    private Integer updatedBy;

    @Column(name = "UPDATEDON")
    private Date updatedOn;

    @Column(name = "DELETEDFLAG")
    private Integer deletedFlag;

    @Column(name = "ATTEMPTCOUNT")
    private Integer attemptCount;

    @Column(name = "FEEDBACKDATE")
    private Date feedBackDate;

    @Column(name = "EXECUTIVEUSERID")
    private Long executiveUserId;

    @Column(name = "DCUSERID")
    private Long dcUserId;

    @Column(name = "DCSUBMITTEDDATE")
    private Date dcSubmittedDDate;

    @Column(name = "DCUPLOADPDFPATH")
    private String dcUploadPdfPath;

    @Column(name = "NODALOFFICERFLAG")
    private String nodalOfficerFlag;

    @Column(name = "NODALOFFICERDECISIONDATE")
    private Date nodalOfficerDecisionDate;

    @Column(name = "EXECUTIVEREMARKS")
    private String executiveRemarks;

    @Column(name = "ALLOTTEDDATE")
    private String allottedDate;

    @Column(name = "NODALOFFICERUSERID")
    private Long nodalOfficerUserId;

    @Column(name = "DCUPLOADAUDIO")
    private String dcUploadAudio;

    @Column(name = "DCUPLOADVIDEO")
    private String dcUploadVideo;

    @Column(name = "CONNECTEDSTATUS")
    private Integer connectedStatus;

    @Column(name = "DGOREMARKS")
    private String dgoRemarks;

    @Column(name = "DGODOC")
    private String dgoDoc;

    @Column(name = "GOREMARKS")
    private String goRemarks;

    @Column(name = "DGOUSERID")
    private Long dgoUserId;

    @Column(name = "GOUSERID")
    private Long goUserId;

    @Column(name = "DGOSUBMITTEDDATE")
    private Date dgoSubmittedDDate;

    @Column(name = "GOSUBMITTEDDATE")
    private Date goSubmittedDDate;
    @Column(name = "REASSIGNREMARK")
    private String reAssignRemark;
    @Column(name = "REASSIGNDDATE")
    private Date reAssignDate;
    @Column(name = "REASSIGNFLAG")
    private Integer reAssignFlag;

    @Column(name = "REASSIGNCONNECTEDSTATUS")
    private Integer reAssignConnectedStatus;
    @Column(name = "REASSIGNUSERID")
    private Integer reAssignUserId;

    @Column(name = "DGOQUERYREMARKS")
    private String dgoQueryRemarks;

    @Column(name = "DGOQUERYDOC")
    private String dgoQueryDoc;

    @Column(name = "DGOQUERYDATE")
    private Date dgoQueryDate;

    @Column(name = "DGOQUERYCOUNT")
    private Integer dgoQueryCount;

    @Column(name = "GOQUERYREMARKS")
    private String goQueryRemarks;

    @Column(name = "GOQUERYDATE")
    private Date goQueryDate;

    @Column(name = "GOQUERYCOUNT")
    private Integer goQueryCount;

    @Column(name = "DCREPLAY")
    private String dcReplay;

    @Column(name = "DGOREPLAY")
    private String dgoReplay;

    @Column(name = "DCACTION")
    private String dcAction;

    @Column(name = "DGOACTION")
    private String dgoAction;

    @Column(name = "GOACTION")
    private String goAction;
}
