package com.project.bsky.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "HOSPITALMASTER04042022")
public class HospitaDetails {

	    @Id
	    @Column(name = "INTID")
	    private Double intId;

	    @Column(name = "INTSTATEID")
	    private Double intStateId;

	    @Column(name = "VCHSTATENAME")
	    private String vchStateName;

	    @Column(name = "INTDISTRICTID")
	    private Double intDistrictId;

	    @Column(name = "VCHDISTRICTNAME")
	    private String vchDistrictName;

	    @Column(name = "INTBLOCKID")
	    private Double intBlockId;

	    @Column(name = "VCHBLOCKNAME")
	    private String vchBlockName;

	    @Column(name = "INTFACILITYCODE")
	    private Double intFacilityCode;

	    @Column(name = "NAME")
	    private String name;

	    @Column(name = "ADDRESS")
	    private String address;

	    @Column(name = "PIN")
	    private Double pin;

	    @Column(name = "CONTACT_PERSON")
	    private String contactPerson;

	    @Column(name = "CONTACT_NO")
	    private Double contactNo;

	    @Column(name = "ALTERNATIVE_CONTACT_NO")
	    private Double alternativeContactNo;

	    @Column(name = "EMAIL")
	    private String email;

	    @Column(name = "INTSTATUS")
	    private Double intStatus;

	    @Column(name = "LATITUDE")
	    private String latitude;

	    @Column(name = "LONGITUDE")
	    private String longitude;

	    @Column(name = "MOU_START_DATE")
	    private String mOUStartDate;

	    @Column(name = "MOU_END_DATE")
	    private String mOUEndDate;

	    @Column(name = "DATE_OF_REPORTING")
	    private String dateOfReporting;

		public Double getIntId() {
			return intId;
		}

		public void setIntId(Double intId) {
			this.intId = intId;
		}

		public Double getIntStateId() {
			return intStateId;
		}

		public void setIntStateId(Double intStateId) {
			this.intStateId = intStateId;
		}

		public String getVchStateName() {
			return vchStateName;
		}

		public void setVchStateName(String vchStateName) {
			this.vchStateName = vchStateName;
		}

		public Double getIntDistrictId() {
			return intDistrictId;
		}

		public void setIntDistrictId(Double intDistrictId) {
			this.intDistrictId = intDistrictId;
		}

		public String getVchDistrictName() {
			return vchDistrictName;
		}

		public void setVchDistrictName(String vchDistrictName) {
			this.vchDistrictName = vchDistrictName;
		}

		public Double getIntBlockId() {
			return intBlockId;
		}

		public void setIntBlockId(Double intBlockId) {
			this.intBlockId = intBlockId;
		}

		public String getVchBlockName() {
			return vchBlockName;
		}

		public void setVchBlockName(String vchBlockName) {
			this.vchBlockName = vchBlockName;
		}

		public Double getIntFacilityCode() {
			return intFacilityCode;
		}

		public void setIntFacilityCode(Double intFacilityCode) {
			this.intFacilityCode = intFacilityCode;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public Double getPin() {
			return pin;
		}

		public void setPin(Double pin) {
			this.pin = pin;
		}

		public String getContactPerson() {
			return contactPerson;
		}

		public void setContactPerson(String contactPerson) {
			this.contactPerson = contactPerson;
		}

		public Double getContactNo() {
			return contactNo;
		}

		public void setContactNo(Double contactNo) {
			this.contactNo = contactNo;
		}

		public Double getAlternativeContactNo() {
			return alternativeContactNo;
		}

		public void setAlternativeContactNo(Double alternativeContactNo) {
			this.alternativeContactNo = alternativeContactNo;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public Double getIntStatus() {
			return intStatus;
		}

		public void setIntStatus(Double intStatus) {
			this.intStatus = intStatus;
		}

		public String getLatitude() {
			return latitude;
		}

		public void setLatitude(String latitude) {
			this.latitude = latitude;
		}

		public String getLongitude() {
			return longitude;
		}

		public void setLongitude(String longitude) {
			this.longitude = longitude;
		}

		public String getmOUStartDate() {
			return mOUStartDate;
		}

		public void setmOUStartDate(String mOUStartDate) {
			this.mOUStartDate = mOUStartDate;
		}

		public String getmOUEndDate() {
			return mOUEndDate;
		}

		public void setmOUEndDate(String mOUEndDate) {
			this.mOUEndDate = mOUEndDate;
		}

		public String getDateOfReporting() {
			return dateOfReporting;
		}

		public void setDateOfReporting(String dateOfReporting) {
			this.dateOfReporting = dateOfReporting;
		}

		@Override
		public String toString() {
			return "HospitaDetails [intId=" + intId + ", intStateId=" + intStateId + ", vchStateName=" + vchStateName
					+ ", intDistrictId=" + intDistrictId + ", vchDistrictName=" + vchDistrictName + ", intBlockId="
					+ intBlockId + ", vchBlockName=" + vchBlockName + ", intFacilityCode=" + intFacilityCode + ", name="
					+ name + ", address=" + address + ", pin=" + pin + ", contactPerson=" + contactPerson
					+ ", contactNo=" + contactNo + ", alternativeContactNo=" + alternativeContactNo + ", email=" + email
					+ ", intStatus=" + intStatus + ", latitude=" + latitude + ", longitude=" + longitude
					+ ", mOUStartDate=" + mOUStartDate + ", mOUEndDate=" + mOUEndDate + ", dateOfReporting="
					+ dateOfReporting + "]";
		}
		

}