package com.project.bsky.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
@Data
@Entity
@Table(name = "BES_ACTIONREMARK")
public class EnrollmentRemarks {
	@Id
	@Column(name = "REMARK_ID")
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	private Long id;
	@Column(name = "REMARK")
	private String remarks;
	
	@Column(name = "CREATEDBY")
	private Long createdby;
	
	@Column(name = "CREATEDON")
	private Date createdon;
	
	@Column(name = "UPDATEDBY")
	private Long updatedby;
	
	@Column(name = "UPDATEDON")
	private Date updatedon;
	
	@Column(name = "STATUSFLAG")
	private Long statusflag;
}
