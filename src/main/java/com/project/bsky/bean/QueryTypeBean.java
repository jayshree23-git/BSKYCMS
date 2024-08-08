package com.project.bsky.bean;

public class QueryTypeBean {
	private String typeId;
	private String typeName;
	private String remark;
	private String createdBy;
	private String updatedBy;
	private Long statusflag;
	
	
	
	public Long getStatusflag() {
		return statusflag;
	}
	public void setStatusflag(Long statusflag) {
		this.statusflag = statusflag;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	@Override
	public String toString() {
		return "QueryTypeBean [typeId=" + typeId + ", typeName=" + typeName + ", remark=" + remark + ", createdBy="
				+ createdBy + ", updatedBy=" + updatedBy + ", statusflag=" + statusflag + "]";
	}
	
	
	

}
