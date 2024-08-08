/**
 * 
 */
package com.project.bsky.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * @author rajendra.sahoo
 *
 */
@Data
@Entity
@Table(name = "HOSPITAL_TYPE_MASTER")
public class HospitalTypeMaster {
	
	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") // for oracle sequence generate
	@Column(name = "HOSPITALTYPEID")
	private Integer hospitaltypeid;
	
	@Column(name = "HOSPITALTYPENAME")
	private String hospitaltypename;
	
	@Column(name = "HOSPITALTYPEDESCRIPTION")
	private String hospitaltypedesc;

	@Column(name = "CREATEDBY")
	private Long createdby;
	
	@Column(name = "CREATEDON")
	private Date createdon; 
	
	@Column(name = "UPDATEDBY")
    private Long updatedby;
	
	@Column(name = "UPDATEDON")
	private Date updatedon; 
	
	@Column(name = "STATUSFLAG")
	private Integer status;
}
