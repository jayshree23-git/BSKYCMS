package com.project.bsky.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateExtensionBean {

	private Integer[] transactionDetailsId;
	private String claimBy;
	private String actionId;
	private Integer createdBy;
}
