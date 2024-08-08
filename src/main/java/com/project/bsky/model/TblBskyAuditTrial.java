/**
 * 
 */
package com.project.bsky.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

/**
 * @author rajendra.sahoo
 *
 */

@Entity
@Data
@Table(name = "TBL_BSKY_AUDIT_TRAIL")
public class TblBskyAuditTrial implements Serializable {

	@Id
	@Column(name = "AUDIT_ID")
	private Long auditId;
	
	@Column(name = "URN")
	private String urn;
	
	@Column(name = "CLAIM_NO")
	private String claimno;
	
	@Column(name = "SR_NO")
	private Integer srno;
	
	@Column(name = "STATUS_DESCRIPTION")
	private String statusdescription;
	
	@Column(name = "STATUS_DT")
	private Date statusdate;
	
//	@Column(name = "STATUS_BY")
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name ="STATUS_BY")
	private UserDetails statusby;
	
	
	@Column(name = "MODULE_NAME")
	private String modulename;
	
	@Transient
	private String sstatusdate;
	
}
