package com.project.bsky.bean;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class HospitalProcedureTagging {

	private Integer headerId;
	private Integer hospitalId;
	private Integer procedureId;
	private String procedureCode;
	private String procedureDescription;
	private String subPackageCode;
	private String subPackageName;
	private String packageHeaderCode;
	private String packageHeaderName;
	private String packageAmount;
	private String isSurgical;
	private String status;
	private Integer masterStatus;
	private Integer insertStatus;
	private boolean viewStatus;
	private String errorMessage;
}
