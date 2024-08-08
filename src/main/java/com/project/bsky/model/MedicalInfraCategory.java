package com.project.bsky.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;


@Data
@Entity
@NotNull
@Table(name = "EMP_MST_MEDICAL_INFRA_CATEGORY")
public class MedicalInfraCategory implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name = "MEDINFRA_CAT_ID")
	private Integer medInfracatId;
	
	@Column(name = "MEDICAL_INFRA_CAT_NAME")
	private String medInfraCatName;
	
	@Column(name = "STATUSFLAG")
	private Integer statusFlag;
	
//	@Column(name = "CREATEDBY")
//	private Integer createdBy;
//	
	@ManyToOne
	@JoinColumn(name = "CREATEDBY")
	private UserDetails userId;
	
	@Column(name = "CREATEDON")
	private Date createdOn;
	
	@Column(name = "UPDATEDBY")
	private Integer updatedBy;
	
	@Column(name = "UPDATEDON")
	private Date updatedOn;
	
	@Column(name = "IS_MANDATORY")
	private Integer isMandatory;
	
	@Transient
	private String screatedate;
	
	@Transient
	private Integer createdBy;
	
	
	

}
