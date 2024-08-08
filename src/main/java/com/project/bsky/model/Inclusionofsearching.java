/**
 * 
 */
package com.project.bsky.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author hrusikesh.mohanty
 *
 */
@Entity
@Table(name = "PACKAGEDETAILSBSKY")
public class Inclusionofsearching implements Serializable{
	
	@Id
	@Column(name = "ID")
	private Long id;
	@Column(name = "PROCEDURECODE")
	private String procedure;
	@Column(name = "PACKAGEID")
	private String packageid;
	@Column(name = "PACKAGENAME")
    private String packagename;
	@Column(name = "COST")
    private Long cost;
	@Column(name = "DAYS")
	 private String days;
	@Column(name = "TYPE")
	 private String type;
	@Column(name = "STATUS")
	 private String status;
	@Column(name = "UPDATEDDATE")
	 private Date updateddate;
	@Column(name = "PREAUTH")
	 private String preauth;
	@Column(name = "STRATIFICATIONCRITERIA")
	 private String stratificationcriteria;
	@Column(name = "NOOFSTRATICRITERIAWHICANBEBOOK")
	 private long noofstraticriteriawhicanbebook;
	@Column(name = "IMPLANTSHIGHENDCONSUMABLES")
	 private String implantshighendconsumables;
	@Column(name = "CANMORETHAN1TYPEOFIMPLANTBOOK")
	 private String canmorethan1typeofimplantbook;
	@Column(name = "RESERVATIONPUBLICHOSPITALS")
	 private String reservationpublichospitals;
	@Column(name = "SPECIALCONDITIONPOPUP")
	 private String specialconditionpopup;
	@Column(name = "SPECIALCONDITIONSRULE")
	 private String specialconditionsrule;
	@Column(name = "AUTOAPPROVED")
	 private String autoapproved;
	@Column(name = "MANDOCUMENTSPREAUTHORIZATION")
	 private String mandocumentspreauthorizattion;
	@Column(name = "MANDOCCLAIMPROCESSING")
	 private String mandocclaimprocessing;
	@Column(name = "REMARK")
	 private String remark;
	@Column(name = "PACKAGECATEGORYCODECARD")
	 private String packagecategorycodecard;
	@Column(name = "PACKAGEIDCARD")
	 private String packagetdcard;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProcedure() {
		return procedure;
	}
	public void setProcedure(String procedure) {
		this.procedure = procedure;
	}
	public String getPackageid() {
		return packageid;
	}
	public void setPackageid(String packageid) {
		this.packageid = packageid;
	}
	public String getPackagename() {
		return packagename;
	}
	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}
	public Long getCost() {
		return cost;
	}
	public void setCost(Long cost) {
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
	public Date getUpdateddate() {
		return updateddate;
	}
	public void setUpdateddate(Date updateddate) {
		this.updateddate = updateddate;
	}
	public String getPreauth() {
		return preauth;
	}
	public void setPreauth(String preauth) {
		this.preauth = preauth;
	}
	public String getStratificationcriteria() {
		return stratificationcriteria;
	}
	public void setStratificationcriteria(String stratificationcriteria) {
		this.stratificationcriteria = stratificationcriteria;
	}
	public long getNoofstraticriteriawhicanbebook() {
		return noofstraticriteriawhicanbebook;
	}
	public void setNoofstraticriteriawhicanbebook(long noofstraticriteriawhicanbebook) {
		this.noofstraticriteriawhicanbebook = noofstraticriteriawhicanbebook;
	}
	public String getImplantshighendconsumables() {
		return implantshighendconsumables;
	}
	public void setImplantshighendconsumables(String implantshighendconsumables) {
		this.implantshighendconsumables = implantshighendconsumables;
	}
	public String getCanmorethan1typeofimplantbook() {
		return canmorethan1typeofimplantbook;
	}
	public void setCanmorethan1typeofimplantbook(String canmorethan1typeofimplantbook) {
		this.canmorethan1typeofimplantbook = canmorethan1typeofimplantbook;
	}
	public String getReservationpublichospitals() {
		return reservationpublichospitals;
	}
	public void setReservationpublichospitals(String reservationpublichospitals) {
		this.reservationpublichospitals = reservationpublichospitals;
	}
	public String getSpecialconditionpopup() {
		return specialconditionpopup;
	}
	public void setSpecialconditionpopup(String specialconditionpopup) {
		this.specialconditionpopup = specialconditionpopup;
	}
	public String getSpecialconditionsrule() {
		return specialconditionsrule;
	}
	public void setSpecialconditionsrule(String specialconditionsrule) {
		this.specialconditionsrule = specialconditionsrule;
	}
	public String getAutoapproved() {
		return autoapproved;
	}
	public void setAutoapproved(String autoapproved) {
		this.autoapproved = autoapproved;
	}
	public String getMandocumentspreauthorizattion() {
		return mandocumentspreauthorizattion;
	}
	public void setMandocumentspreauthorizattion(String mandocumentspreauthorizattion) {
		this.mandocumentspreauthorizattion = mandocumentspreauthorizattion;
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
	public String getPackagecategorycodecard() {
		return packagecategorycodecard;
	}
	public void setPackagecategorycodecard(String packagecategorycodecard) {
		this.packagecategorycodecard = packagecategorycodecard;
	}
	public String getPackagetdcard() {
		return packagetdcard;
	}
	public void setPackagetdcard(String packagetdcard) {
		this.packagetdcard = packagetdcard;
	}
	@Override
	public String toString() {
		return "Inclusionofsearching [id=" + id + ", procedure=" + procedure + ", packageid=" + packageid
				+ ", packagename=" + packagename + ", cost=" + cost + ", days=" + days + ", type=" + type + ", status="
				+ status + ", updateddate=" + updateddate + ", preauth=" + preauth + ", stratificationcriteria="
				+ stratificationcriteria + ", noofstraticriteriawhicanbebook=" + noofstraticriteriawhicanbebook
				+ ", implantshighendconsumables=" + implantshighendconsumables + ", canmorethan1typeofimplantbook="
				+ canmorethan1typeofimplantbook + ", reservationpublichospitals=" + reservationpublichospitals
				+ ", specialconditionpopup=" + specialconditionpopup + ", specialconditionsrule="
				+ specialconditionsrule + ", autoapproved=" + autoapproved + ", mandocumentspreauthorizattion="
				+ mandocumentspreauthorizattion + ", mandocclaimprocessing=" + mandocclaimprocessing + ", remark="
				+ remark + ", packagecategorycodecard=" + packagecategorycodecard + ", packagetdcard=" + packagetdcard
				+ "]";
	}
	

	
	
	
	
	
	
	
	
	
	

	

}
