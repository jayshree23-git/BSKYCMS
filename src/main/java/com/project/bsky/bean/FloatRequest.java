/**
 * 
 */
package com.project.bsky.bean;

import java.util.List;

/**
 * @author santanu.barad
 *
 */
public class FloatRequest {
	private List<Long> floatList;
	private Integer userId;
	private String remarks;
	private Integer pendingAt;
	private Long updatedBy;

	public List<Long> getFloatList() {
		return floatList;
	}

	public void setFloatList(List<Long> floatList) {
		this.floatList = floatList;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getPendingAt() {
		return pendingAt;
	}

	public void setPendingAt(Integer pendingAt) {
		this.pendingAt = pendingAt;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Override
	public String toString() {
		return "FloatRequest [floatList=" + floatList + ", userId=" + userId + ", remarks=" + remarks + ", pendingAt="
				+ pendingAt + ", updatedBy=" + updatedBy + "]";
	}

}
