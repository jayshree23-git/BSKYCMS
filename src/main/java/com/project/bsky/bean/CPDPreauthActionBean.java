package com.project.bsky.bean;

import lombok.Data;

@Data
public class CPDPreauthActionBean {
	private Long userId;
	private String fromDate;
	private String toDate;
	private String stateCode1;
	private String distCode1;
	private String hospitalCode;
	private String flag;
	private String action;
	private Long txnPackageDetailId;
	private Integer schemeid;
	private String schemecategoryid;

}
