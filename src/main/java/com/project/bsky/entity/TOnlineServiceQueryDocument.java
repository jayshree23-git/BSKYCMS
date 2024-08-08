package com.project.bsky.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
@Data
@Entity
@Table(name="t_online_service_query_doc")
public class TOnlineServiceQueryDocument implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="t_online_service_query_doc_seq")
	@SequenceGenerator(name="t_online_service_query_doc_seq", sequenceName="t_online_service_query_doc_seq", allocationSize=1)
	@Column(name="INTAPPDOCID")
	private Integer intAppDocId;
	@Column(name="INTNOTINGID")
	private Integer intNotingId;
	@Column(name="INTONLINESERVICEID")
	private Integer intOnlineServiceId;
	@Column(name="VCHDOCUMENTNAME")
	private String vchDocumentName;
	@Column(name="VCHDOCUMENTFILE")
	private String vchDocumentFile;
	@Column(name="INTPROFILEID")
	private Integer intProfileId;
	@Column(name="INTCREATEDBY")
	private Integer intCreatedBy;
	@Column(name="STMCREATEDON")
	private Date stmCreatedOn;
	@Column(name="INTUPDATEDBY")
	private Integer intUpdatedBy;
	@Column(name="DTMUPDATEDON")
	private Date dtmUpdatedOn;
	@Column(name="BITDELETEDFLAG")
	private Integer bitDeletedFlag;
}
