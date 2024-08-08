package com.project.bsky.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name ="BSKYGROUP")
public class GroupDetails {

	
	
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "GROUPID")
	private Integer GroupId;
	
	@Column(name = "GROUPNAME")
	private String GroupName;
	
	@Column(name = "CREATEDDATE")
	private Timestamp CreatedDate;
	
	@Column(name = "CREATEDBY")
	private Integer CreatedBy;
	
	@Column(name = "LASTUPDATEDATE")
	private Date LastUpdateDate;
	
	@Column(name = "LASTUPDATEDBY")
	private Integer LastUpdatedBy;
	
	@Column(name = "ISACTIVE")
	private Number IsActive;
	
	@Column(name = "ISSUBGROUPED")
	private Integer IsSubgrouped;
	

	@Column(name = "PARENTGROUPID")
	private String ParentGroupId;
	
//	public int getGroupId() {
//		return GroupId;
//	}
//
//
//	public void setGroupId(int groupId) {
//		GroupId = groupId;
//	}


	public String getGroupName() {
		return GroupName;
	}


	public Integer getGroupId() {
		return GroupId;
	}


	public void setGroupId(Integer groupId) {
		GroupId = groupId;
	}


	public void setGroupName(String groupName) {
		GroupName = groupName;
	}

	public Timestamp getCreatedDate() {
		return CreatedDate;
	}


	public void setCreatedDate(Timestamp createdDate) {
		CreatedDate = createdDate;
	}


	


	public Date getLastUpdateDate() {
		return LastUpdateDate;
	}


	public void setLastUpdateDate(Date lastUpdateDate) {
		LastUpdateDate = lastUpdateDate;
	}


	

	public Integer getCreatedBy() {
		return CreatedBy;
	}


	public void setCreatedBy(Integer createdBy) {
		CreatedBy = createdBy;
	}


	


	

	public Integer getLastUpdatedBy() {
		return LastUpdatedBy;
	}


	public void setLastUpdatedBy(Integer lastUpdatedBy) {
		LastUpdatedBy = lastUpdatedBy;
	}


	public Number getIsActive() {
		return IsActive;
	}


	public void setIsActive(Number isActive) {
		IsActive = isActive;
	}


	public Integer getIsSubgrouped() {
		return IsSubgrouped;
	}


	public void setIsSubgrouped(Integer isSubgrouped) {
		IsSubgrouped = isSubgrouped;
	}


	

	public String getParentGroupId() {
		return ParentGroupId;
	}


	public void setParentGroupId(String parentGroupId) {
		ParentGroupId = parentGroupId;
	}


	@Override
	public String toString() {
		return "GroupDetails [GroupId=" + GroupId + ", GroupName=" + GroupName + ", CreatedDate=" + CreatedDate
				+ ", CreatedBy=" + CreatedBy + ", LastUpdateDate=" + LastUpdateDate + ", LastUpdatedBy=" + LastUpdatedBy
				+ ", IsActive=" + IsActive + ", IsSubgrouped=" + IsSubgrouped + ", ParentGroupId=" + ParentGroupId
				+ "]";
	}


	



}
