package com.project.bsky.bean;

public class VerifyOverrideCodeBean {
    private String date;
    private String patientName;
    private String hospitalCode;
    private Integer memberId;
    private String adharNo;
    private String verifiedBy;
    private String verifiedThrough;
    private String status;
    private String urn;
    private String modeOfVerify;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getAdharNo() {
        return adharNo;
    }

    public void setAdharNo(String adharNo) {
        this.adharNo = adharNo;
    }

    public String getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(String verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public String getVerifiedThrough() {
        return verifiedThrough;
    }

    public void setVerifiedThrough(String verifiedThrough) {
        this.verifiedThrough = verifiedThrough;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public String getModeOfVerify() {
        return modeOfVerify;
    }

    public void setModeOfVerify(String modeOfVerify) {
        this.modeOfVerify = modeOfVerify;
    }
}
