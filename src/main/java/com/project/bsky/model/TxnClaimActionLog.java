/**
 * 
 */
package com.project.bsky.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author rajendra.sahoo
 *
 */
@Setter
@Getter
@ToString
@Entity
@Table(name ="TXNCLAIMACTION_LOG")
public class TxnClaimActionLog implements Serializable {
	
	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name="CLAIMACTIONLOGID")
	private Long claimactionloginid;
	
	@Column(name = "CLAIMID")
	private Long claimid;
	
	@Column(name = "ACTIONTYPE")
	private Integer actiontype;
	
	@Column(name = "ACTIONBY")
	private Long actionby;
	
	@Column(name = "STATUSFLAG")
	private Integer statusflag;
	
	@Column(name = "REMARKS")
	private String remarks;
	
	@Column(name = "REMARK_ID")
	private Long remarkid;
	

}
