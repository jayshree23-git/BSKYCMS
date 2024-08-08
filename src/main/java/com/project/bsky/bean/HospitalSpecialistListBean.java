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
public class HospitalSpecialistListBean {

	private Integer hospitalid;

	private String packagecode;

	private Integer packageid;

	private Integer addmissionprvyear;

	private Integer admissionlastyear;

	private Integer status;

	private Integer showstatus;

	private String packagename;

	private String hospitalcode;

	private String hospitalname;

	private String stateName;

	private String distName;

	private String actionby;

	private String actionon;

	private String appliedon;

	private String actiontype;

	private Long hospitalTypeId;

	private String hospitalTypeName;
	
	private String preauth;
}
