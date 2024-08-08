package com.project.bsky.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;



@Entity
@Data
@Table(name = "PRIMARY_LINK")
public class PrimaryLink implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PRIMARY_LINK_ID")
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") // for oracle sequence generate
	private Long primaryLinkId;

	@NotEmpty
	@NotBlank
	@NotNull
	@Column(name = "PRIMARY_LINK_NAME")
	private String primaryLinkName;

	//@ManyToOne(cascade = CascadeType.ALL)
	//@JoinColumn(name = "GLOBAL_LINK_ID")
//	@Column(name = "GLOBAL_LINK_ID")
	//private GlobalLink globalLinkId;

	//@ManyToOne(cascade = CascadeType.ALL)
	//@JoinColumn(name = "FUNCTION_ID")
//	@Column(name = "FUNCTION_ID")
	//private FunctionMaster functionId;

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

	@Column(name = "PL_DESCRIPTION")
	private String description;

	@OneToOne
	@JoinColumn(name = "FUNCTION_ID")
	private FunctionMaster functionMaster;

	@ManyToOne
	@JoinColumn(name = "GLOBAL_LINK_ID")
	private GlobalLink globalLink;
	
	@Column(name = "DISPLAY_ORDER")
	private Integer order;

	public Long getPrimaryLinkId() {
		return primaryLinkId;
	}

	public void setPrimaryLinkId(Long primaryLinkId) {
		this.primaryLinkId = primaryLinkId;
	}

	public String getPrimaryLinkName() {
		return primaryLinkName;
	}

	public void setPrimaryLinkName(String primaryLinkName) {
		this.primaryLinkName = primaryLinkName;
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

	public FunctionMaster getFunctionMaster() {
		return functionMaster;
	}

	public void setFunctionMaster(FunctionMaster functionMaster) {
		this.functionMaster = functionMaster;
	}

	public GlobalLink getGlobalLink() {
		return globalLink;
	}

	public void setGlobalLink(GlobalLink globalLink) {
		this.globalLink = globalLink;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	
	

}
