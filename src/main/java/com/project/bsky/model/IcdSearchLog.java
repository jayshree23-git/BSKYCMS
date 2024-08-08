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
 * 
 */
@Data
@Entity
@Table(name ="tbl_icd_search_log")
public class IcdSearchLog {

	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name="ICD_SEARCH_LOGID")
	private Long logid;	
	
	@Column(name="icd_code")
	private String icdCode;	
	
	@Column(name="icd_name")
	private String icdName;
	
	@Column(name="icd_mode")
	private Integer icdMode;
	
	@Column(name="CREATEDBY")
	private Long createdBy;
	
	@Column(name="CREATEDON")
	private Date createdOn;
	
	@Column(name="statusflag")
	private Integer statusflag;
	
	@Column(name="SEARCH_INPUT")
	private String searchkey;
	
}
