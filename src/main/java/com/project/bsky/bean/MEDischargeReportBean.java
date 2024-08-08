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
public class MEDischargeReportBean {
	@Id
	private Integer id;
	private String countData;

	

	
}
