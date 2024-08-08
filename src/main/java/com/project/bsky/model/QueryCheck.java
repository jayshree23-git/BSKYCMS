/**
 * 
 */
package com.project.bsky.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author santanu.barad
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TBL_QUERY_CHECK")
public class QueryCheck {
	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name = "QUERY_ID")
	private Integer queryId;

	@Column(name = "QUERY_BY")
	private Long queryBy;

	@Column(name = "QUERY_CONTENT")
	private String queryContent;

	@Column(name = "QUERY_ON")
	private Date createdOn;

	public Integer getQueryId() {
		return queryId;
	}

	public void setQueryId(Integer queryId) {
		this.queryId = queryId;
	}

	public Long getQueryBy() {
		return queryBy;
	}

	public void setQueryBy(Long queryBy) {
		this.queryBy = queryBy;
	}

	public String getQueryContent() {
		return queryContent;
	}

	public void setQueryContent(String queryContent) {
		this.queryContent = queryContent;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Override
	public String toString() {
		return "QueryCheck [queryId=" + queryId + ", queryBy=" + queryBy + ", queryContent=" + queryContent
				+ ", createdOn=" + createdOn + "]";
	}

}
