package com.project.bsky.bean;

import lombok.Data;

@Data
public class PrimaryLinkBean {

	private Integer primaryLinkId;
	private Long functionId;
	private Long globalLinkId;
	private String primaryLinkName;
	private String description;
	private boolean status;
	private Integer isActive;
	private Long id;
	private Integer createdBy;
	private Integer updatedby;
	private Integer order;

	
}
