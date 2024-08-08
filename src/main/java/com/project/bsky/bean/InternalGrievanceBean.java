/**
 * 
 */
package com.project.bsky.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import com.project.bsky.model.UserDetails;

import lombok.Data;

/**
 * @author priyanka.singh
 *
 */
@Data
public class InternalGrievanceBean {
	
	
	private Long grievanceId;
	private String grievanceBy;
	private String  castType;
	private String priority;
	private String mobileNo;
	private String emailId;
	private String assignTo;
	private String description;
	private String createdBy;
	private String updatedBy;
	private String createdOn;
	private String updateOn;
	private String documentName;
	private String statusFlag;
	private String typeid;
	private String  categoryType;
	private String fullname;
	private String tokenNumber;
	private String moduleName;
	private String assignedName;
	private String closeDate;
	private Date fromDate;
	private String expectedDate1;
	private Date toDate;
	private String closingDescription;
	

}
