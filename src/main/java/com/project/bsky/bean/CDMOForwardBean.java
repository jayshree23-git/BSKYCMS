package com.project.bsky.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author arabinda.guin
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CDMOForwardBean {
	
	private Long processId;
	private Long serviceId;
	private Integer action;
	private String remark;
	private Long updatedBy;
	private Integer updatedByRoleId;
	private String priority;
	private Long dcUserId;

}
