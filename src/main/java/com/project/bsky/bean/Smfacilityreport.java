/**
 * 
 */
package com.project.bsky.bean;

import lombok.Data;

/**
 * @author rajendra.sahoo
 *
 */
@Data
public class Smfacilityreport {
	
	private String statename;
	
	private String distname;
	
	private String hospitalname;
		
	private String totalblocked;
	
	private String totalcceactiontaken;
	
	private String nonconnected;
	
	private String connected;

	private String smforyes;
	
	private String smforno;	

	private String pctsmforyes;
	
	private String pctsmforno;

}
