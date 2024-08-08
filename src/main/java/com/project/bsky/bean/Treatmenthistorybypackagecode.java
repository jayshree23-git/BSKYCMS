/**
 * 
 */
package com.project.bsky.bean;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author rajendra.sahoo
 *
 */
@Getter
@Setter
@ToString
public class Treatmenthistorybypackagecode implements Serializable {
	
	private String urnno;
	private String packagename;
	private String packagecode;
	private String hospitalname;
	private String totalamount;
	private String dateofadmission;
	private String dateofdischarge;
	private String patentname;
	private String status;

}
