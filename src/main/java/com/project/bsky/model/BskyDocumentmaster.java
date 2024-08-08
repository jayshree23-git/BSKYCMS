package com.project.bsky.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

/**
 * @author rajendra.sahoo
 *
 */
@Entity
@Data
@Table(name = "TBL_MDR_DOCUMENT_MASTER")
public class BskyDocumentmaster {

	@Id
	@Column(name = "DOCUMENT_ID",unique = true,nullable = false)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long documentId;
	
	@Column(name = "DOCUMENT_NAME")
	private String documentname;
	
	@Column(name = "CREATEDBY")
	private Long createdby;
	
	@Column(name = "CREATEDON")
	private Date createdon;
	
	@Column(name = "UPDATEDBY")
	private Long updatedby;
	
	@Column(name = "UPDATEDON")
	private Date updatedon;
	
	@Column(name = "STATUSFLAG")
	private Integer statusflag;
	
	@Transient
	private String createbyname;
	
	@Transient
	private String createtime;
}
