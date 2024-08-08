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
public class Cpdwiseunprocessedbean {
	private String fromDate;
	private String toDate;
	private String stateCode;
	private String districtCode;
	private String hospitalCode;
	private String actionId;
	private Long userId;
}
