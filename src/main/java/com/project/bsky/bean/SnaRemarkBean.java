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
public class SnaRemarkBean {
	
	@Id
	private Long id;
	private String remarks;
	private String statusFlag;

}
