/**
 * 
 */
package com.project.bsky.bean;

import java.util.List;

import lombok.Data;

/**
 * 
 */
@Data
public class Surveygroupmapping {
	
	private Long surveyid;
	private Long createdby;
	private List<mappingsurveygroupbean> selectlist;
	private String procedure;
	
	
	

}
