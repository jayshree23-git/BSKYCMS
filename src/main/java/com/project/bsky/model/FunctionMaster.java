package com.project.bsky.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import lombok.ToString;

@Entity
@ToString
@Table(name = "FUNCTION_MASTER")
public class FunctionMaster implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "FUNCTION_ID")
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") //for oracle sequence generate
	private Long functionId;

	@NotEmpty
	@NotBlank
	@NotNull
	@Column(name = "FN_NAME")
	private String functionName;
	
	@NotEmpty
	@NotBlank
	@NotNull
	@Column(name = "FN_URL")
	private String fileName;
	
	@Column(name = "CREATED_BY")
	private Integer createdBy;

	@CreationTimestamp
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "UPDATED_BY")
	private Integer updatedBy;

	@Column(name = "UPDATED_ON")
	private Date updatedOn;

	@Column(name = "STATUS_FLAG")
	private Integer bitStatus;
	
	@Column(name = "FN_DESCRIPTION")
	private String description;

	public Long getFunctionId() {
		return functionId;
	}

	public void setFunctionId(Long functionId) {
		this.functionId = functionId;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
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

	public Integer getBitStatus() {
		return bitStatus;
	}

	public void setBitStatus(Integer bitStatus) {
		this.bitStatus = bitStatus;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "FunctionMaster [functionId=" + functionId + ", functionName=" + functionName + ", fileName=" + fileName
				+ ", createdBy=" + createdBy + ", createdOn=" + createdOn + ", updatedBy=" + updatedBy + ", updatedOn="
				+ updatedOn + ", bitStatus=" + bitStatus + ", description=" + description + "]";
	}
	
	
	
	
	

}
