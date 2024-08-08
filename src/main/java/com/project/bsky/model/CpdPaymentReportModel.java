package com.project.bsky.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name ="TBL_CPD_PAYMENT_AMOUNT")

public class CpdPaymentReportModel implements Serializable{
	@Id	
	@Column(name = "ID")
	private Long id;
	
	
	@Column(name = "CREATED_BY")
	private Long createdby;
	
	@Column(name = "CREATED_ON")
	private Date createdon;
	
	
	@Column(name = "DISHONOUR_AMOUNT")
	private Double dishonouramount;
	
	
	@Column(name = "SETTLEMENT_AMOUNT")
	private Double settelment;

	@Column(name = "STATUS_FLAG")
	private Integer statusFlag;
}
