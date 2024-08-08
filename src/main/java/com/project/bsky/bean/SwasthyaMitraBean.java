/**
 * 
 */
package com.project.bsky.bean;

import java.util.List;

import lombok.Data;

/**
 * @author priyanka.singh
 *
 */
@Data
public class SwasthyaMitraBean {
	
	
	
	private Long mappingId;
	private Long swasthyaId;
	private String userName;
	private String hospitalCode;
	private Long stateFlg;
	
	private Long counthospital;
	private String hospitalName;
	private String districtName;
	private String distCode;
	private String statCode;
	private String useId;
	
	private String stateName;
	private String fullName;
	private String createdBy;
	private String updatedBy;
	private String groupId;
	private List<Object> hosplist;
	private String gropName;
	private Long mobileNo;
	private String emailId;
	private Long bskyUserId;
	
}
