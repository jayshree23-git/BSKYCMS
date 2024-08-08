package com.project.bsky.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name="T_ONLINE_SERVICE_QUERY_DOC")
@Entity
public class QueryDocEntity {

	@Id
	@Column(name = "INTAPPDOCID")
	private Integer docId;
	
	@Column(name = "INTNOTINGID")
	private Integer notId;
	
	@Column(name = "INTONLINESERVICEID")
	private Integer servId;
	
	@Column(name = "VCHDOCUMENTNAME")
	private String docName;
	
	@Column(name = "VCHDOCUMENTFILE")
	private String doc;
	
	@Column(name = "INTPROFILEID")
	private Integer profileId;
	
	@Column(name = "INTCREATEDBY")
	private Integer createdBy;
	
	@Column(name = "STMCREATEDON")
	private Date createdOn;
	
	@Column(name = "INTUPDATEDBY")
	private Integer updatedBy;
	
	@Column(name = "DTMUPDATEDON")
	private Date updatedOn;
	
	@Column(name = "BITDELETEDFLAG")
	private Integer bitDeletedFlag;
	
}
