/**
 * 
 */
package com.project.bsky.bean;

import lombok.Data;

/**
 * @author hrusikesh.mohanty
 *
 */
@Data
public class Cpdwisehospitalwise {
	private String fromDate;
	private String toDate;
	private String stateCode;
	private String districtCode;
	private String hospitalCode;
	private Long groupid;
	private Long cpduserId;
	private String hospitalcodesearch;
	private String actionId;
}
