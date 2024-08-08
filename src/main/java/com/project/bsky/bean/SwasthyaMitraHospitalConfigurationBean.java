/**
 * 
 */
package com.project.bsky.bean;

import com.project.bsky.model.DistrictMaster;
import com.project.bsky.model.HospitalInformation;
import com.project.bsky.model.State;
import com.project.bsky.model.UserDetails;

import lombok.Data;

/**
 * @author priyanka.singh
 *
 */
@Data
public class SwasthyaMitraHospitalConfigurationBean {
	
	private Long mappingId;
	private Long swasthyaId;
	private String userName;
	private String hospitalCode;
	private String stateFlg;
	
	private Long counthospital;
	private String hospitalName;
	private String districtName;
	private  String distCode;
	private String statCode;
	private String useId;
	
	private String stateName;
	private String fullName;
	private String createdBy;
	private String updatedBy;
	private String groupId;
	
	

	

}
