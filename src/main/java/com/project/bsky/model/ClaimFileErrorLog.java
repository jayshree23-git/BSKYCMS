package com.project.bsky.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/*
@Auther : Sambit Kumar Pradhan
@Date : 16/01/2023
*/

@Entity
@Table(name = "CLAIM_FILE_ERROR_LOG")
public class ClaimFileErrorLog implements Serializable {
    private static final long serialVersionUID = -3045314662848597602L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CLAIM_FILE_ERROR_LOG_id_gen")
    @SequenceGenerator(name = "CLAIM_FILE_ERROR_LOG_id_gen", sequenceName = "CLAIM_FILE_ERROR_LOG_SEQ", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "CLAIMID")
    private Long claimid;

    @Column(name = "TRANCTIONDETAILSID")
    private Long tranctiondetailsid;

    @Size(max = 255)
    @Column(name = "URN")
    private String urn;

    @Size(max = 255)
    @Column(name = "DISCHRAGE")
    private String dischrage;

    @Size(max = 255)
    @Column(name = "ADDITIONAL_DOC")
    private String additionalDoc;

    @Size(max = 255)
    @Column(name = "ADDITIONAL_DOC1")
    private String additionalDoc1;

    @Size(max = 255)
    @Column(name = "ADDITIONAL_DOC2")
    private String additionalDoc2;

    @Size(max = 255)
    @Column(name = "INVETIGATION")
    private String invetigation;

    @Size(max = 255)
    @Column(name = "INVESTIGATION1")
    private String investigation1;

    @Size(max = 255)
    @Column(name = "PRE_SURGERY")
    private String preSurgery;

    @Size(max = 255)
    @Column(name = "POST_SURGERY")
    private String postSurgery;

    @Size(max = 255)
    @Column(name = "INTRA_SURGERY")
    private String intraSurgery;

    @Size(max = 255)
    @Column(name = "SPECIMEN_REMOVAL")
    private String specimenRemoval;

    @Size(max = 255)
    @Column(name = "PATIENT")
    private String patient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClaimid() {
        return claimid;
    }

    public void setClaimid(Long claimid) {
        this.claimid = claimid;
    }

    public Long getTranctiondetailsid() {
        return tranctiondetailsid;
    }

    public void setTranctiondetailsid(Long tranctiondetailsid) {
        this.tranctiondetailsid = tranctiondetailsid;
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public String getDischrage() {
        return dischrage;
    }

    public void setDischrage(String dischrage) {
        this.dischrage = dischrage;
    }

    public String getAdditionalDoc() {
        return additionalDoc;
    }

    public void setAdditionalDoc(String additionalDoc) {
        this.additionalDoc = additionalDoc;
    }

    public String getAdditionalDoc1() {
        return additionalDoc1;
    }

    public void setAdditionalDoc1(String additionalDoc1) {
        this.additionalDoc1 = additionalDoc1;
    }

    public String getAdditionalDoc2() {
        return additionalDoc2;
    }

    public void setAdditionalDoc2(String additionalDoc2) {
        this.additionalDoc2 = additionalDoc2;
    }

    public String getInvetigation() {
        return invetigation;
    }

    public void setInvetigation(String invetigation) {
        this.invetigation = invetigation;
    }

    public String getInvestigation1() {
        return investigation1;
    }

    public void setInvestigation1(String investigation1) {
        this.investigation1 = investigation1;
    }

    public String getPreSurgery() {
        return preSurgery;
    }

    public void setPreSurgery(String preSurgery) {
        this.preSurgery = preSurgery;
    }

    public String getPostSurgery() {
        return postSurgery;
    }

    public void setPostSurgery(String postSurgery) {
        this.postSurgery = postSurgery;
    }

    public String getIntraSurgery() {
        return intraSurgery;
    }

    public void setIntraSurgery(String intraSurgery) {
        this.intraSurgery = intraSurgery;
    }

    public String getSpecimenRemoval() {
        return specimenRemoval;
    }

    public void setSpecimenRemoval(String specimenRemoval) {
        this.specimenRemoval = specimenRemoval;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

}