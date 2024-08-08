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
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * @author suprava.parida
 *
 */

@Data
@Entity
@NotNull
@Table(name = "EMP_MST_MEDICAL_INFRA_SUB_CATEGORY")
public class MedicalInfraSubCategory implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name = "MEDINFRA_SUB_CAT_ID")
	private Integer medInfraSubCatId;
	
	
	@Column(name = "MEDICAL_SUB_CAT_NAME")
	private String medInfraSubCatName;
	
	//@Column(name = "MEDINFRA_CAT_ID")
	//private Integer medInfracatId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="MEDINFRA_CAT_ID")
	private MedicalInfraCategory medInfracatId;
	
	@Column(name = "STATUSFLAG")
	private Integer statusFlag;
	
//	@Column(name = "CREATEDBY")
//	private Integer createdBy;
	
	@ManyToOne
	@JoinColumn(name = "CREATEDBY")
	private UserDetails userId;
	
	@Column(name = "CREATEDON")
	private Date createdOn;
	
	@Column(name = "UPDATEDBY")
	private Integer updatedBy;
	
	@Column(name = "UPDATEDON")
	private Date updatedOn;
	
	@Transient
	private String screatedate;
}
