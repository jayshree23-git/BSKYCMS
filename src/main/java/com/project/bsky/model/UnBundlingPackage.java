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
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.ToString;

/**
 * @author rajendra.sahoo
 *	DT- 06-11-2023
 */
@Entity
@ToString
@Table(name = "MST_UNBUNDLING_PACKAGE")
@Data
public class UnBundlingPackage {

	@Id
	@Column(name = "UNBUNDLING_ID")
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") //for oracle sequence generate
	private Long unboundlingid;
	
	@Column(name = "UNBUNDLING_PKG_NAME")
	private String packagename;
	
	@Column(name = "UNBUNDLING_PKG_CODE")
	private String packagecode;
	
	@Column(name = "UNBUNDLING_SPECIALITY_CODE")
	private String specialitycode;
	
	@Column(name = "CREATED_BY")
	private Integer createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "UPDATED_BY")
	private Integer updatedBy;

	@Column(name = "UPDATED_ON")
	private Date updatedOn;

	@Column(name = "STATUS_FLAG")
	private Integer status;
	
	@Transient
	private String screatedOn;
}
