package com.project.bsky.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Data
@Table(name = "BANK_DETAILS")
public class BankDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") // for oracle sequence generate
	@Column(name = "BANKID")
	private Integer bankId;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "USER_ID")
	private UserDetails userid;

	@Column(name = "PAYEENAME")
	private String payeeName;

	@Column(name = "CREATEDBY")
	private Integer createdBY;

	@Column(name = "CREATEDON")
	private Date createdON;

	@Column(name = "BANKACCNO")
	private String bankAccNo;

	@Column(name = "IFSCCODE")
	private String ifscCode;

	@Column(name = "BANKNAME")
	private String bankName;

	@Column(name = "BRANCHNAME")
	private String branchName;

	@Column(name = "UPLOADPASSBOOK")
	private String uploadPassbook;

	@Column(name = "UPDATEDBY")
	private Integer UpdatedBy;

	@Column(name = "UPDATEDON")
	private Date UpdatedON;

	@Column(name = "STATUS_FLAG")
	private Integer IsActive;

}
