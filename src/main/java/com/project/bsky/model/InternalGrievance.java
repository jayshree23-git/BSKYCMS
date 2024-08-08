/**
 * 
 */
package com.project.bsky.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;


/**
 * @author priyanka.singh
 *
 */

/**
 * Modify on : 15-May-2023 
 * @author Rajendra.Sahoo
 * 
 * statusFlag = 0 = Open
 * statusFlag = 1 = Inprogress
 * statusFlag = 2 = Close
 *  * statusFlag = 3 =All
 * 
 * priority = 1 = High
 * priority = 2 = Medium
 * priority = 3 = Low
 * 
 * Category = 1 = Complaint
 * Category = 2 = Issue
 * Category = 3 = Request
 * Category = 4 = Suggestion
 *
 */


@Entity
@Data
@Table(name = "MST_INTERNAL_GRIEVANCE")
public class InternalGrievance implements Serializable {
	
	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name="GRIEVANCE_ID")
	private Long grievanceId;
	
//	@ManyToOne
//	@JoinColumn(name = "GROUPID", referencedColumnName = "TYPE_ID")
//	private GroupTypeDetails groupId;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "GRIEVANCE_BY", referencedColumnName = "TYPE_ID")
	private GroupTypeDetails groupId;
	
	@Column(name = "CATEGORY_TYPE")
	private Integer  categoryType;
	
	@Column(name = "PRIORITY")
	private Integer priority;
	
	@Column(name = "MOBILE_NO")
	private Long phoneno;
	
	@Column(name = "EMAIL_ID")
	private String email;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "CREATED_BY")
	private UserDetails createdBy;
	
	@Column(name = "UPDATED_BY")
	private Long updatedBy;
	
	@Column(name = "CREATED_ON")
	private Date createdOn;
	
	@Column(name = "UPDATED_ON")
	private Date updateOn;
	
	@Column(name = "DOCUMENT_NAME")
	private String documentName;
	
	@Column(name = "STATUS_FLAG")
	private Integer statusFlag;
	
	@Column(name = "FULL_NAME")
	private String fullname;
	
	@Column(name = "GRIEVANCE_SOURCE")
	private String grievanceSource;
	
	@Column(name = "MODULE_NAME")
	private String moduleName;
	
	@Transient
	private String screate;
	
	@Transient
	private String userId;
	
	@Transient
	private Integer sgroup;
	
	@Column(name = "ASSIGNED_TO")
	private String assignedName;
	
	@Column(name = "CLOSING_DATE")
	private Date closingDate;
	
	@Transient
	private String closeDate;
	
	@Column(name = "ACTUAL_DATE")
	private Date actualDate;

	@Transient
	private String actualDate1;
	
	@Column(name = "EXPECTED_DATE")
	private Date expectedDate;
	
	@Transient
	private String expectedDate1;
	
	@Column(name = "CLOSING_DESCRIPTION")
	private String closingDescription;
	
	@Column(name = "TOKEN_NO")
	private String tokenNumber;
	
	
	
}
