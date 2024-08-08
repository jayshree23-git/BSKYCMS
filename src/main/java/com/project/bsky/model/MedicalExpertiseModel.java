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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.ToString;


/**
 * @author jayshree.moharana
 *
 */
@Data
@Entity
@NotNull
@Table(name = "EMP_MST_MEDICAL_EXPERTISE")
public class MedicalExpertiseModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") //for oracle sequence generate	
	@Column(name = "MED_EXPERTISE_ID")
	private Long id;
	
	@Column(name = "MEDICAL_EXPERTISE_NAME")
	private String medexpertisename;
	
//	@Column(name = "CREATEDBY")
//   private Long createdby;
	@ManyToOne
	@JoinColumn(name = "CREATEDBY")
	private UserDetails userdetails;
	
	@Column(name = "UPDATEDBY")
	private Long updatedby;
	
	@Column(name = "UPDATEDON")
	private Date updatedon;
	
	@Column(name = "CREATEDON")
	private Date createdon;
	
	@Column(name = "STATUSFLAG")
	private Integer statusFlag;
	
	@Transient
	private Long createdby;
	

}
