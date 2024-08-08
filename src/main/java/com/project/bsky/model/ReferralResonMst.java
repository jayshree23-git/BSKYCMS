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
@Table(name = "MSTREFERRALREASON")
@Entity
@Data
public class ReferralResonMst {

	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") // for oracle sequence generate
	@Column(name = "REFERRALID")	
	private Long referralid;
	
	@Column(name = "REFERRALDESCRIPTION")
	private String referaldesc;
	
	@Column(name = "CREATEDON")
	private Date createon;	
	
	@Column(name = "CREATEDBY")
	private Long createdby;
	
	@Column(name = "UPDATEDON")
	private Date updateon;
	
	@Column(name = "UPDATEDBY")
	private Long updateby;
	
	@Column(name = "DELETEDFLAG")
	private Integer status;
}
