/**
 * 
 */
package com.project.bsky.bean;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author santanu.barad
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CPDApproveRequestBean {

	private Long userId;
	private String flag;
	private Date fromDate;
	private Date toDate;
	private String stateCode;
	private String distCode;
	private String hospitalCode;
	private Long cpdFlag;
	private String mortality;
	private String floatNo;
	private Long amountFlag;
	private String description;
	private String action;
	private Long authMode;
	private String[] hospitalCodeArr;
	private Long snoUserId;
	private Integer pageIn;
	private Integer pageEnd;
	private String procedure;
	private String packages;
	private String implant;
	private String highend;
	private String ward;
	private String filter;
	private Double snaAmount;
	private Integer searchtype;
	private Integer trigger;
	private String claimStatus;
	private Object stateCodeList;
	private Object distCodeList;
	private Object hospitalCodeList;
	private String cpdName;
	private String mobile;
	private Long status;
	private Integer schemeid;
	private String schemecategoryid;
	private Integer cpdUserId;
	private Integer schemesubcategoryid;
}
