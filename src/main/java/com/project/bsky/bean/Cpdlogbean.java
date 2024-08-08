package com.project.bsky.bean;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cpdlogbean {
	private Long claimId;
	private Long userId;
	private String amount;
	private String remarks;
	private Long actionRemarksId;
	private String actionRemark;
	private String urnNo;
	private Long pendingAt;
	private Long claimStatus;
	private Long claimAmount;
	private String presurgeryphoto;
	private String postsurgeryphoto;
	private String additionaldocs;
	private String dischargeslip;
	private String investigationdocs;
	private Long updatedby;
	private String additionaldoc1;
	private String additionaldoc2;
	private Long statusflag;
	private String intrasurgery;
	private String patientpic;
	private String specimenpic;
	private String snamortality;

	private Long icdFlag;
	private List<ICDDetailsBean> icdFinalData;
}
