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

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.ToString;

/**
 * @author rajendra.sahoo
 *
 */

@Entity
@ToString
@Table(name = "TBL_CONFIG_DYNAMIC_REPORT")
@Data
public class DynamicReportConfiguration {
	
	@Id
	@Column(name = "SLNO")
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") //for oracle sequence generate
	private Long slno;
	
	@Column(name = "SPECIALITYNAME")
	private String packagename;
	
	@Column(name = "PACKAGE_CODE")
	private String packagecode;
	
	@Column(name = "NO_OF_OCCUARANE")
	private Integer occuarance;
	
	@Column(name = "AGE")
	private Integer age;
	
	@Column(name = "AGE_CONDITION")
	private String agecondition;
	
	@Column(name = "REPORT_NAME")
	private String reportname;
	
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
	
	@Column(name = "SPECIALITYCODE")
	private String specilitycode;
	
	@Transient
	private String totalnumber;
	
	@Transient
	private String totalnumberwithocc;

}
