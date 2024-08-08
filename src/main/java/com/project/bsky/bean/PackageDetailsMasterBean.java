package com.project.bsky.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PackageDetailsMasterBean {

	private String packageHeaderCode;

	private String packageSubcatagoryId;

	private String packageSubcode;

	private String procedureCode;

	private String procedureDescription;

	private Long hospitalCategoryId;

	private Long amount;

	private Long createdBy;

	private Date createdOn;

	private Integer updatedBy;

	private Date updatedOn;

	private Integer deletedFlag;

	private String packageCatagoryType;

	private Long maximumDays;

	private String multiProcedure;

	private String mandatoryPreauth;

	private String dayCare;

	private Long femaleAmount;

	private String stayType;

	private String preauthDocs;

	private String claimProcessDocs;

	private String packageExtention;

	private String priceEditable;

	private String isPackageException;

	private List<Map<String, Object>> scheme;

	private String isSurgical;

}