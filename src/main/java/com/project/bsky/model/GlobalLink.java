package com.project.bsky.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import lombok.ToString;

@Entity
@ToString
@Table(name = "GLOBAL_LINK")
public class GlobalLink implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "GLOBAL_LINK_ID")
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") //for oracle sequence generate
	private Long globalLinkId;

	@NotEmpty
	@NotBlank
	@NotNull
	@Column(name = "GLOBAL_LINK_NAME")
	private String globalLinkName;
	
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
	
	@Column(name = "GL_DESCRIPTION")
	private String description;
	
	@Column(name = "DISPLAY_ORDER")
	private Integer order;
	//@OneToMany(mappedBy = "globalLinkId", fetch = FetchType.EAGER)
	//Set<PrimaryLink> primarylinks = new HashSet<>();

	

	public Long getGlobalLinkId() {
		return globalLinkId;
	}

	public void setGlobalLinkId(Long globalLinkId) {
		this.globalLinkId = globalLinkId;
	}

	public String getGlobalLinkName() {
		return globalLinkName;
	}

	public void setGlobalLinkName(String globalLinkName) {
		this.globalLinkName = globalLinkName;
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

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}
	
	
	

}
