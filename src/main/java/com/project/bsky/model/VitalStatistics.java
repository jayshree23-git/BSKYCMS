package com.project.bsky.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Entity
@Accessors(chain=true)
@ToString
@Data
@Table(name = "MSTVITALPARAMETERS")

public class VitalStatistics implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID")
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") //for oracle sequence generate
	private Long vitalStatisticsId;

	@NotEmpty
	@NotBlank
	@NotNull
	@Column(name = "VITALSIGN")
	private String vitalstatisticsname;
	
	@NotEmpty
	@NotBlank
	@NotNull
	@Column(name = "VITALCODE")
	private String vitalstatisticscode;

	@NotEmpty
	@NotBlank
	@NotNull
	@Column(name = "VITALDESCRIPTION")
	private String vitalstatisticsdescription;

	@Column(name = "CREATEDBY")
	private Integer createdBy;

	@CreationTimestamp
	@Column(name = "CREATEDON")
	private Date createdOn;

	@Column(name = "UPDATEDBY")
	private Integer updatedBy;

	@Column(name = "UPDATEDON")
	private Date updatedOn;
	
	@Column(name = "DELETEDFLAG")
	private Integer deletedFlag;

	public Long getVitalStatisticsId() {
		return vitalStatisticsId;
	}

	public void setVitalStatisticsId(Long vitalStatisticsId) {
		this.vitalStatisticsId = vitalStatisticsId;
	}

	public String getVitalstatisticsname() {
		return vitalstatisticsname;
	}

	public void setVitalstatisticsname(String vitalstatisticsname) {
		this.vitalstatisticsname = vitalstatisticsname;
	}

	public String getVitalstatisticscode() {
		return vitalstatisticscode;
	}

	public void setVitalstatisticscode(String vitalstatisticscode) {
		this.vitalstatisticscode = vitalstatisticscode;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	public Integer getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(Integer deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

//	public Integer getBitStatus() {
//		return bitStatus;
//	}
//
//	public void setBitStatus(Integer bitStatus) {
//		this.bitStatus = bitStatus;
//	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
