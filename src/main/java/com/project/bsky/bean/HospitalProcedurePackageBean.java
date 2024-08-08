package com.project.bsky.bean;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class HospitalProcedurePackageBean {

	private Integer actionCode;
	private Integer userId;
	private Integer procedureId;
	private Integer headerId;
	private String procedureCode;
	private String packageHeaderCode;
	private String viewStatus;

}
