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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author jayshree.moharana
 *
 */

@Entity
@Table(name="emp_mst_typeof_expertise")
@Getter
@Setter
@ToString
public class TypeOfExpertiseModel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") //for oracle sequence generate
	@Column(name="TYPEOF_EXPERTISE_ID")
	private Long typeofexpertiseid;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="MED_EXPERTISE_ID")
	private MedicalExpertiseModel medexpertiseid;
	
	@NotEmpty(message = "typeofexpertisename cannot be empty")
	@NotNull(message = "typeofexpertisename cannot be null")
	@Column(name="TYPEOF_EXPERTIESE_NAME")
	private String typeofexpertisename;
//	
//	@Column(name="CREATEDBY")
//	private String createdby;
	@ManyToOne
	@JoinColumn(name = "CREATEDBY")
	private UserDetails userDetails;
	
	@Column(name="CREATEDON")
	private Date createdate;
	
	@Column(name="UPDATEDBY")
	private String updateby;
	
	@Column(name="UPDATEDON")
	private Date updatedate;
	 
	@Column(name="STATUSFLAG")
	private Integer status;
	
	@Transient
	private Long createdby;
}
