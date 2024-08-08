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
@Entity
@Table(name = "TBL_ME_ACTION")
@Data
public class TblMeAction {
	
	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") // for oracle sequence generate
	@Column(name = "ME_REMARKID")
	private Long actionlogid;
	
	@Column(name = "REMARKS")
	private String remark;
	
	@Column(name = "ACTIONBY")
	private Long actionby;
	
	@Column(name = "ACTIONON")
	private Date actionon;
	
	@Column(name = "URN")
	private String urn;
	
	@Column(name = "TRANSACTIONDETAILSID")
	private Long transactionid;
	
	@Column(name = "CREATEDON")
	private Date createon;	
	
	@Column(name = "CREATEDBY")
	private Long createby;
	
	@Column(name = "UPDATEDON")
	private Date updateon;
	
	@Column(name = "UPDATEDBY")
	private Long updateby;
	
	@Column(name = "STATUSFLAG")
	private Integer status;
	
	@Column(name = "CLAIMSUBMITTEDORNOT")
	private Integer claimraisestatus;
	
	@Column(name = "CLAIMID")
	private Long claimid;
	
	@Column(name = "claim_conversion_count")
	private Integer claimremarkcount;
	
	@Column(name = "ME_TRIGGER_ID")
	private Integer meTriggerId;
	
	@Column(name = "AGE_REMARK")
	private String ageremark;

}
