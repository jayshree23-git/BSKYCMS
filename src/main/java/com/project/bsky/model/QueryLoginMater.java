/**
 * 
 */
package com.project.bsky.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "TBL_QUERY_LOGIN_MASTER")
public class QueryLoginMater {
	@Id
	@Column(name = "LOGIN_QUERY_ID")
	private int queryId;

	@Column(name = "USER_NAME")
	private String userName;

	@JsonIgnore
	@Column(name = "PASSWORD")
	private String passWord;

	@Column(name = "STATUS")
	private Long status;

	public int getQueryId() {
		return queryId;
	}

	public void setQueryId(int queryId) {
		this.queryId = queryId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "QueryLoginMater [queryId=" + queryId + ", userName=" + userName + ", passWord=" + passWord + ", status="
				+ status + "]";
	}

}
