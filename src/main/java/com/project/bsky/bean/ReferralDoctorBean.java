/**
 * 
 */
package com.project.bsky.bean;

import java.util.List;

import lombok.Data;

/**
 * @author rajendra.sahoo
 *
 */
@Data
public class ReferralDoctorBean {
	
	private List<HospObj> hospList;
	
	private Integer createdBy;

	private Integer updatedBy;
	
	private Long cpdId;
	
	private String hospitaltype; 

}
