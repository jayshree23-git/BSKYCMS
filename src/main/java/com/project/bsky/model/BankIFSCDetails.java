package com.project.bsky.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "M_IFSC_DETAILS")
@Data
@ToString
public class BankIFSCDetails {

	@Id
	@Column(name = "INT_IFSC_ID")
	private Integer bankIFSCId;

	@Column(name = "VCH_BANK_NAME")
	private String bankName;

	@Column(name = "VCH_DISTRICT")
	private String districtName;

	@Column(name = "VCH_BRANCH_NAME")
	private String branchName;

	@Column(name = "VCH_IFSC_CODE")
	private String ifscCode;

	@Column(name = "INT_MAX_ACCOUNT_NO")
	private Integer maxAccountLength;

	@Column(name = "INT_MIN_ACCOUNT_NO")
	private Integer minAccountLength;

	@Column(name = "BIT_DELETED_FLAG")
	private Integer deletedFlag;
}
