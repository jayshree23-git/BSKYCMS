/**
 * 
 */
package com.project.bsky.bean;

import javax.persistence.Id;

import lombok.Data;

/**
 * @author priyanka.singh
 *
 */
@Data
public class FacilityDetailBean {
	
	@Id
	private Integer facilityDetailId;
	private String facilityName;
	private String statusFlag;
	private String createdBy;
	private String updatedBy;
	private String createdOn;
	private String updatedOn;

}
