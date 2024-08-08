/**
 * 
 */
package com.project.bsky.bean;

import com.project.bsky.model.HospitalInformation;

import lombok.Data;

/**
 * @author rajendra.sahoo
 *
 */
@Data
public class Cpdconfigbean {
	
	private HospitalInformation hospital;
	
	private String status;
	
	private Integer bskyuserid;
	
	private String name;
	
	private String mobileno;
	
	private String username;
	
	private String taggeddate;
	
	private String applieddate;

}
