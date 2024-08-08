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
public class Smattendancereportbean {

	private String discrict;
	
	private String hospital;
	
	private String smname;
	
	private String dateofjoin;
	
	private String noofdays;
	
	private String noofdutydays;
	
	private String absent;
	
	private String remark;
	
	private String other;
	
	private String date;
	
	private String state;
	
	private String punchin;
	
	private String punchout;
	
	private List<Object> hosplist;
	
	private String hospcount;
	
	private String code;
	
	private String punchindistance;
	
	private String punchoutdistance;
}
