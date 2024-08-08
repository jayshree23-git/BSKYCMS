package com.project.bsky.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name ="PACKAGEDETAILSBSKY")
public class PackageDetails {
	
	@Id
	@Column(name = "ID")
	private Integer id;
	
	@Column(name = "PROCEDURECODE")
	private String procedureCode;
	
	@Column(name = "PACKAGEID")
	private String packageId;
	
	@Column(name = "PACKAGENAME")
	private String packageName;
	
	@Column(name = "COST")
	private Integer cost;
	
	@Column(name = "DAYS")
	private String days;

	@Column(name = "TYPE")
	private String type;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "UPDATEDDATE")
	private Timestamp updatedDate;
	
	@Column(name = "PREAUTH")
	private String preauth;
	
	@Column(name = "STRATIFICATIONCRITERIA")
	private String stratificationCriteria;
	
	@Column(name = "NOOFSTRATICRITERIAWHICANBEBOOK")
	private Integer noofStratiCriteriaWhicanbeBook;
	
	@Column(name = "IMPLANTSHIGHENDCONSUMABLES")
	private String implantShighendConsumables;
	
	@Column(name = "CANMORETHAN1TYPEOFIMPLANTBOOK")
	private String canMoreThan1TypeofImplantBook;
	
	@Column(name = "RESERVATIONPUBLICHOSPITALS")
	private String reservationPublicHospitals;
	
	@Column(name = "SPECIALCONDITIONPOPUP")
	private String specialConditioPopup;
	
	@Column(name = "SPECIALCONDITIONSRULE")
	private String specialConditionsRule;
	
	@Column(name = "AUTOAPPROVED")
	private String autoApproved;
	
	@Column(name = "MANDOCUMENTSPREAUTHORIZATION")
	private String manDocumentsPreauthorization;
	
	@Column(name = "MANDOCCLAIMPROCESSING")
	private String mandocclaimprocessing;
	
	@Column(name = "REMARK")
	private String remark;
	
	@Column(name = "PACKAGECATEGORYCODECARD")
	private String packageCategoryCodeCard;
	
	@Column(name = "PACKAGEIDCARD")
	private String packageIdCard;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProcedureCode() {
		return procedureCode;
	}

	public void setProcedureCode(String procedureCode) {
		this.procedureCode = procedureCode;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Integer getCost() {
		return cost;
	}

	public void setCost(Integer cost) {
		this.cost = cost;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getPreauth() {
		return preauth;
	}

	public void setPreauth(String preauth) {
		this.preauth = preauth;
	}

	public String getStratificationCriteria() {
		return stratificationCriteria;
	}

	public void setStratificationCriteria(String stratificationCriteria) {
		this.stratificationCriteria = stratificationCriteria;
	}

	public Integer getNoofStratiCriteriaWhicanbeBook() {
		return noofStratiCriteriaWhicanbeBook;
	}

	public void setNoofStratiCriteriaWhicanbeBook(Integer noofStratiCriteriaWhicanbeBook) {
		this.noofStratiCriteriaWhicanbeBook = noofStratiCriteriaWhicanbeBook;
	}

	public String getImplantShighendConsumables() {
		return implantShighendConsumables;
	}

	public void setImplantShighendConsumables(String implantShighendConsumables) {
		this.implantShighendConsumables = implantShighendConsumables;
	}

	public String getCanMoreThan1TypeofImplantBook() {
		return canMoreThan1TypeofImplantBook;
	}

	public void setCanMoreThan1TypeofImplantBook(String canMoreThan1TypeofImplantBook) {
		this.canMoreThan1TypeofImplantBook = canMoreThan1TypeofImplantBook;
	}

	public String getReservationPublicHospitals() {
		return reservationPublicHospitals;
	}

	public void setReservationPublicHospitals(String reservationPublicHospitals) {
		this.reservationPublicHospitals = reservationPublicHospitals;
	}

	public String getSpecialConditioPopup() {
		return specialConditioPopup;
	}

	public void setSpecialConditioPopup(String specialConditioPopup) {
		this.specialConditioPopup = specialConditioPopup;
	}

	public String getSpecialConditionsRule() {
		return specialConditionsRule;
	}

	public void setSpecialConditionsRule(String specialConditionsRule) {
		this.specialConditionsRule = specialConditionsRule;
	}

	public String getAutoApproved() {
		return autoApproved;
	}

	public void setAutoApproved(String autoApproved) {
		this.autoApproved = autoApproved;
	}

	public String getManDocumentsPreauthorization() {
		return manDocumentsPreauthorization;
	}

	public void setManDocumentsPreauthorization(String manDocumentsPreauthorization) {
		this.manDocumentsPreauthorization = manDocumentsPreauthorization;
	}

	public String getMandocclaimprocessing() {
		return mandocclaimprocessing;
	}

	public void setMandocclaimprocessing(String mandocclaimprocessing) {
		this.mandocclaimprocessing = mandocclaimprocessing;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPackageCategoryCodeCard() {
		return packageCategoryCodeCard;
	}

	public void setPackageCategoryCodeCard(String packageCategoryCodeCard) {
		this.packageCategoryCodeCard = packageCategoryCodeCard;
	}

	public String getPackageIdCard() {
		return packageIdCard;
	}

	public void setPackageIdCard(String packageIdCard) {
		this.packageIdCard = packageIdCard;
	}

	@Override
	public String toString() {
		return "PackageDetails [id=" + id + ", procedureCode=" + procedureCode + ", packageId=" + packageId
				+ ", packageName=" + packageName + ", cost=" + cost + ", days=" + days + ", type=" + type + ", status="
				+ status + ", updatedDate=" + updatedDate + ", preauth=" + preauth + ", stratificationCriteria="
				+ stratificationCriteria + ", noofStratiCriteriaWhicanbeBook=" + noofStratiCriteriaWhicanbeBook
				+ ", implantShighendConsumables=" + implantShighendConsumables + ", canMoreThan1TypeofImplantBook="
				+ canMoreThan1TypeofImplantBook + ", reservationPublicHospitals=" + reservationPublicHospitals
				+ ", specialConditioPopup=" + specialConditioPopup + ", specialConditionsRule=" + specialConditionsRule
				+ ", autoApproved=" + autoApproved + ", manDocumentsPreauthorization=" + manDocumentsPreauthorization
				+ ", mandocclaimprocessing=" + mandocclaimprocessing + ", remark=" + remark
				+ ", packageCategoryCodeCard=" + packageCategoryCodeCard + ", packageIdCard=" + packageIdCard + "]";
	}
	
	
	
	

}
