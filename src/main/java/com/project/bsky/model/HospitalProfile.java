package com.project.bsky.model;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "HOSPITALPROFILE")
public class HospitalProfile {
	
	@Id
	@Column(name = "HOSPITALID")
    private int HospitalID;
	
    @Column(name = "STATE")
    private String state;

    @Column(name = "DISTRICT")
    private String district;

    @Column(name = "HOSPITALCODE")
    private String hospitalCode;

    @Column(name = "HOSPITALAUTHORITYID")
    private String hospitalAuthorityID;

    @Lob
    @Column(name = "HOSPITALNAME")
    private String hospitalName;

    @Column(name = "HOSPITALTYPE")
    private String hospitalType;

    @Lob
    @Column(name = "HOSPITALADDRESS")
    private String hospitalAddress;

    @Column(name = "HOSPITALPINCODE")
    private Integer hospitalPincode;

    @Lob
    @Column(name = "CONTACTPERSON")
    private String contactPerson;

    @Lob
    @Column(name = "PHONENO")
    private String phoneNo;

    @Lob
    @Column(name = "EMAILID")
    private String emailID;

    @Column(name = "COMPUTER")
    private String computer;

    @Column(name = "OS")
    private String os;

    @Column(name = "INTERNET")
    private String internet;

    @Lob
    @Column(name = "OPERATOR")
    private String operator;

    @Lob
    @Column(name = "INSURANCECOMPANYNAME")
    private String insurancecompanyName;

    @Column(name = "EMPANELMENTDATE")
    private String empanelmentDate;

    @Column(name = "EMPANELMENTSTATUS")
    private String empanelmentStatus;

    @Lob
    @Column(name = "LONGITUDE")
    private String longitude;

    @Lob
    @Column(name = "LATITUDE")
    private String latitude;

    @Column(name = "SERVICETAXREGISTRATION_NO")
    private String serviceTaxRegistrationNo;

    @Column(name = "PANCARDNO")
    private String pancardNo;

    @Column(name = "PANCARDHOLDER")
    private String pANCardHolder;

    @Column(name = "BANKNAME")
    private String bankName;

    @Lob
    @Column(name = "BRANCHADDRESS")
    private String branchAddress;

    @Column(name = "BANKACCOUN_NO")
    private String bankAccounNo;

    @Column(name = "IFSCCODE")
    private String iFSCCode;

    @Column(name = "PAYEENAME")
    private String payeeName;

    @Column(name = "NOOF_FULL_TIME_PHYSICIANS")
    private Integer noOfFullTimePhysicians;

    @Column(name = "PHARMACY")
    private Integer pharmacy;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "GENERAL")
    private Integer general;

    @Column(name = "DAYCARE")
    private Integer dayCare;

    @Column(name = "ICU")
    private Integer icu;

    @Column(name = "SECONDARY")
    private String secondary;

    @Column(name = "SECONDARYSINGLE_SPECIALITY")
    private String secondarySingleSpeciality;

    @Column(name = "SECONDARYMULTI_SPECIALITY")
    private String secondaryMultiSpeciality;

    @Column(name = "TERTIARYSINGLE_SPECIALITY")
    private String tertiarySingleSpeciality;

    @Column(name = "TERTIARYMULTI_SPECIALITY")
    private String tertiaryMultiSpeciality;

    @Column(name = "INTERNALMEDICINE")
    private String internalMedicine;

    @Column(name = "CARDIOLOGY")
    private String cardiology;

    @Column(name = "NEPHROLOGY")
    private String nephrology;

    @Column(name = "NEUROLOGY")
    private String neurology;

    @Column(name = "PEADIATRICS")
    private String peadiatrics;

    @Column(name = "PULMONOLOGY")
    private String pulmonology;

    @Column(name = "GASTROENTEROLOGY")
    private String gastroEnterology;

    @Column(name = "GENERALSURGERY")
    private String generalSurgery;

    @Column(name = "ORTHOPAEDICS")
    private String orthopaedics;

    @Column(name = "GYNAECOLOGY")
    private String gynaecology;

    @Column(name = "OBSTETRICS")
    private String obstetrics;

    @Column(name = "ONCOLOGY")
    private String oncology;

    @Column(name = "UROLOGY")
    private String urology;

    @Column(name = "EMERGENCYROOMORMINOR_OT")
    private String emergencyRoomOrMinorOT;

    @Column(name = "BURNSUNIT")
    private String burnsUnit;

    @Column(name = "TRAUMACENTRE")
    private String traumaCentre;

    @Column(name = "CATHLABFACILITY")
    private String cathLabFacility;

    @Column(name = "NOOF_MAJOR_OTS")
    private String noOfMajorOTs;

    @Column(name = "NOOF_MINOR_OTS")
    private String noOfMinorOTs;

    @Column(name = "WASTEDISPOSALSYSTEM")
    private String wasteDisposalSystem;

    @Column(name = "KITCHENSERVICE")
    private String kitchenService;

    @Column(name = "POWERBACKUP")
    private String powerBackup;

    @Column(name = "F62")
    private String f62;

    @Column(name = "UPLOADDATE")
    private Instant uploadDate;

    @Column(name = "UPLOADUSERID")
    private Integer uploadUserID;

	public int getHospitalID() {
		return HospitalID;
	}

	public void setHospitalID(int hospitalID) {
		HospitalID = hospitalID;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public String getHospitalAuthorityID() {
		return hospitalAuthorityID;
	}

	public void setHospitalAuthorityID(String hospitalAuthorityID) {
		this.hospitalAuthorityID = hospitalAuthorityID;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}

	public String getHospitalAddress() {
		return hospitalAddress;
	}

	public void setHospitalAddress(String hospitalAddress) {
		this.hospitalAddress = hospitalAddress;
	}

	public Integer getHospitalPincode() {
		return hospitalPincode;
	}

	public void setHospitalPincode(Integer hospitalPincode) {
		this.hospitalPincode = hospitalPincode;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public String getComputer() {
		return computer;
	}

	public void setComputer(String computer) {
		this.computer = computer;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getInternet() {
		return internet;
	}

	public void setInternet(String internet) {
		this.internet = internet;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getInsurancecompanyName() {
		return insurancecompanyName;
	}

	public void setInsurancecompanyName(String insurancecompanyName) {
		this.insurancecompanyName = insurancecompanyName;
	}

	public String getEmpanelmentDate() {
		return empanelmentDate;
	}

	public void setEmpanelmentDate(String empanelmentDate) {
		this.empanelmentDate = empanelmentDate;
	}

	public String getEmpanelmentStatus() {
		return empanelmentStatus;
	}

	public void setEmpanelmentStatus(String empanelmentStatus) {
		this.empanelmentStatus = empanelmentStatus;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getServiceTaxRegistrationNo() {
		return serviceTaxRegistrationNo;
	}

	public void setServiceTaxRegistrationNo(String serviceTaxRegistrationNo) {
		this.serviceTaxRegistrationNo = serviceTaxRegistrationNo;
	}

	public String getPancardNo() {
		return pancardNo;
	}

	public void setPancardNo(String pancardNo) {
		this.pancardNo = pancardNo;
	}

	public String getpANCardHolder() {
		return pANCardHolder;
	}

	public void setpANCardHolder(String pANCardHolder) {
		this.pANCardHolder = pANCardHolder;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBranchAddress() {
		return branchAddress;
	}

	public void setBranchAddress(String branchAddress) {
		this.branchAddress = branchAddress;
	}

	public String getBankAccounNo() {
		return bankAccounNo;
	}

	public void setBankAccounNo(String bankAccounNo) {
		this.bankAccounNo = bankAccounNo;
	}

	public String getiFSCCode() {
		return iFSCCode;
	}

	public void setiFSCCode(String iFSCCode) {
		this.iFSCCode = iFSCCode;
	}

	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public Integer getNoOfFullTimePhysicians() {
		return noOfFullTimePhysicians;
	}

	public void setNoOfFullTimePhysicians(Integer noOfFullTimePhysicians) {
		this.noOfFullTimePhysicians = noOfFullTimePhysicians;
	}

	public Integer getPharmacy() {
		return pharmacy;
	}

	public void setPharmacy(Integer pharmacy) {
		this.pharmacy = pharmacy;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getGeneral() {
		return general;
	}

	public void setGeneral(Integer general) {
		this.general = general;
	}

	public Integer getDayCare() {
		return dayCare;
	}

	public void setDayCare(Integer dayCare) {
		this.dayCare = dayCare;
	}

	public Integer getIcu() {
		return icu;
	}

	public void setIcu(Integer icu) {
		this.icu = icu;
	}

	public String getSecondary() {
		return secondary;
	}

	public void setSecondary(String secondary) {
		this.secondary = secondary;
	}

	public String getSecondarySingleSpeciality() {
		return secondarySingleSpeciality;
	}

	public void setSecondarySingleSpeciality(String secondarySingleSpeciality) {
		this.secondarySingleSpeciality = secondarySingleSpeciality;
	}

	public String getSecondaryMultiSpeciality() {
		return secondaryMultiSpeciality;
	}

	public void setSecondaryMultiSpeciality(String secondaryMultiSpeciality) {
		this.secondaryMultiSpeciality = secondaryMultiSpeciality;
	}

	public String getTertiarySingleSpeciality() {
		return tertiarySingleSpeciality;
	}

	public void setTertiarySingleSpeciality(String tertiarySingleSpeciality) {
		this.tertiarySingleSpeciality = tertiarySingleSpeciality;
	}

	public String getTertiaryMultiSpeciality() {
		return tertiaryMultiSpeciality;
	}

	public void setTertiaryMultiSpeciality(String tertiaryMultiSpeciality) {
		this.tertiaryMultiSpeciality = tertiaryMultiSpeciality;
	}

	public String getInternalMedicine() {
		return internalMedicine;
	}

	public void setInternalMedicine(String internalMedicine) {
		this.internalMedicine = internalMedicine;
	}

	public String getCardiology() {
		return cardiology;
	}

	public void setCardiology(String cardiology) {
		this.cardiology = cardiology;
	}

	public String getNephrology() {
		return nephrology;
	}

	public void setNephrology(String nephrology) {
		this.nephrology = nephrology;
	}

	public String getNeurology() {
		return neurology;
	}

	public void setNeurology(String neurology) {
		this.neurology = neurology;
	}

	public String getPeadiatrics() {
		return peadiatrics;
	}

	public void setPeadiatrics(String peadiatrics) {
		this.peadiatrics = peadiatrics;
	}

	public String getPulmonology() {
		return pulmonology;
	}

	public void setPulmonology(String pulmonology) {
		this.pulmonology = pulmonology;
	}

	public String getGastroEnterology() {
		return gastroEnterology;
	}

	public void setGastroEnterology(String gastroEnterology) {
		this.gastroEnterology = gastroEnterology;
	}

	public String getGeneralSurgery() {
		return generalSurgery;
	}

	public void setGeneralSurgery(String generalSurgery) {
		this.generalSurgery = generalSurgery;
	}

	public String getOrthopaedics() {
		return orthopaedics;
	}

	public void setOrthopaedics(String orthopaedics) {
		this.orthopaedics = orthopaedics;
	}

	public String getGynaecology() {
		return gynaecology;
	}

	public void setGynaecology(String gynaecology) {
		this.gynaecology = gynaecology;
	}

	public String getObstetrics() {
		return obstetrics;
	}

	public void setObstetrics(String obstetrics) {
		this.obstetrics = obstetrics;
	}

	public String getOncology() {
		return oncology;
	}

	public void setOncology(String oncology) {
		this.oncology = oncology;
	}

	public String getUrology() {
		return urology;
	}

	public void setUrology(String urology) {
		this.urology = urology;
	}

	public String getEmergencyRoomOrMinorOT() {
		return emergencyRoomOrMinorOT;
	}

	public void setEmergencyRoomOrMinorOT(String emergencyRoomOrMinorOT) {
		this.emergencyRoomOrMinorOT = emergencyRoomOrMinorOT;
	}

	public String getBurnsUnit() {
		return burnsUnit;
	}

	public void setBurnsUnit(String burnsUnit) {
		this.burnsUnit = burnsUnit;
	}

	public String getTraumaCentre() {
		return traumaCentre;
	}

	public void setTraumaCentre(String traumaCentre) {
		this.traumaCentre = traumaCentre;
	}

	public String getCathLabFacility() {
		return cathLabFacility;
	}

	public void setCathLabFacility(String cathLabFacility) {
		this.cathLabFacility = cathLabFacility;
	}

	public String getNoOfMajorOTs() {
		return noOfMajorOTs;
	}

	public void setNoOfMajorOTs(String noOfMajorOTs) {
		this.noOfMajorOTs = noOfMajorOTs;
	}

	public String getNoOfMinorOTs() {
		return noOfMinorOTs;
	}

	public void setNoOfMinorOTs(String noOfMinorOTs) {
		this.noOfMinorOTs = noOfMinorOTs;
	}

	public String getWasteDisposalSystem() {
		return wasteDisposalSystem;
	}

	public void setWasteDisposalSystem(String wasteDisposalSystem) {
		this.wasteDisposalSystem = wasteDisposalSystem;
	}

	public String getKitchenService() {
		return kitchenService;
	}

	public void setKitchenService(String kitchenService) {
		this.kitchenService = kitchenService;
	}

	public String getPowerBackup() {
		return powerBackup;
	}

	public void setPowerBackup(String powerBackup) {
		this.powerBackup = powerBackup;
	}

	public String getF62() {
		return f62;
	}

	public void setF62(String f62) {
		this.f62 = f62;
	}

	public Instant getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Instant uploadDate) {
		this.uploadDate = uploadDate;
	}

	public Integer getUploadUserID() {
		return uploadUserID;
	}

	public void setUploadUserID(Integer uploadUserID) {
		this.uploadUserID = uploadUserID;
	}

	@Override
	public String toString() {
		return "HospitalDetailsfHospitalName [HospitalID=" + HospitalID + ", state=" + state + ", district=" + district
				+ ", hospitalCode=" + hospitalCode + ", hospitalAuthorityID=" + hospitalAuthorityID + ", hospitalName="
				+ hospitalName + ", hospitalType=" + hospitalType + ", hospitalAddress=" + hospitalAddress
				+ ", hospitalPincode=" + hospitalPincode + ", contactPerson=" + contactPerson + ", phoneNo=" + phoneNo
				+ ", emailID=" + emailID + ", computer=" + computer + ", os=" + os + ", internet=" + internet
				+ ", operator=" + operator + ", insurancecompanyName=" + insurancecompanyName + ", empanelmentDate="
				+ empanelmentDate + ", empanelmentStatus=" + empanelmentStatus + ", longitude=" + longitude
				+ ", latitude=" + latitude + ", serviceTaxRegistrationNo=" + serviceTaxRegistrationNo + ", pancardNo="
				+ pancardNo + ", pANCardHolder=" + pANCardHolder + ", bankName=" + bankName + ", branchAddress="
				+ branchAddress + ", bankAccounNo=" + bankAccounNo + ", iFSCCode=" + iFSCCode + ", payeeName="
				+ payeeName + ", noOfFullTimePhysicians=" + noOfFullTimePhysicians + ", pharmacy=" + pharmacy
				+ ", remark=" + remark + ", general=" + general + ", dayCare=" + dayCare + ", icu=" + icu
				+ ", secondary=" + secondary + ", secondarySingleSpeciality=" + secondarySingleSpeciality
				+ ", secondaryMultiSpeciality=" + secondaryMultiSpeciality + ", tertiarySingleSpeciality="
				+ tertiarySingleSpeciality + ", tertiaryMultiSpeciality=" + tertiaryMultiSpeciality
				+ ", internalMedicine=" + internalMedicine + ", cardiology=" + cardiology + ", nephrology=" + nephrology
				+ ", neurology=" + neurology + ", peadiatrics=" + peadiatrics + ", pulmonology=" + pulmonology
				+ ", gastroEnterology=" + gastroEnterology + ", generalSurgery=" + generalSurgery + ", orthopaedics="
				+ orthopaedics + ", gynaecology=" + gynaecology + ", obstetrics=" + obstetrics + ", oncology="
				+ oncology + ", urology=" + urology + ", emergencyRoomOrMinorOT=" + emergencyRoomOrMinorOT
				+ ", burnsUnit=" + burnsUnit + ", traumaCentre=" + traumaCentre + ", cathLabFacility=" + cathLabFacility
				+ ", noOfMajorOTs=" + noOfMajorOTs + ", noOfMinorOTs=" + noOfMinorOTs + ", wasteDisposalSystem="
				+ wasteDisposalSystem + ", kitchenService=" + kitchenService + ", powerBackup=" + powerBackup + ", f62="
				+ f62 + ", uploadDate=" + uploadDate + ", uploadUserID=" + uploadUserID + "]";
	}

   
}
	
	
	