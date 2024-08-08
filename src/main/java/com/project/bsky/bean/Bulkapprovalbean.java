/**
 * 
 */
package com.project.bsky.bean;

import java.util.Date;

import lombok.Data;

/**
 * @author hrusikesh.mohanty
 *
 */
@Data
public class Bulkapprovalbean {
	private Long userId;
	private String flag;
	private Date fromDate;
	private Date toDate;
	private String stateCode1;
	private String distCode1;
	private String hospitalCode;
	private Long cpdFlag;
	private String mortality;
	private String floatNo;
	private Integer searchtype;
	
}
