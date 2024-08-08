package com.project.bsky.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USERDETAILS")
public class User {
	@Id
	@Column(name = "USERID")
	private int userId;

	@Column(name = "USERNAME")
	private String userName;
	@JsonIgnore
	@Column(name = "PASSWORD")
	private String passWord;

	@Column(name = "PHONE")
	private String phone;

	@Column(name = "FULL_NAME")
	private String fullName;

	@Column(name = "GROUPID")
	private Long groupId;

	@Column(name = "STATUS_FLAG")
	private Long statusFlag;

	@JsonIgnore
	@Column(name = "ATTEMPTED_STATUS")
	private int attemptedStatus;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(Long statusFlag) {
		this.statusFlag = statusFlag;
	}

	public int getAttemptedStatus() {
		return attemptedStatus;
	}

	public void setAttemptedStatus(int attemptedStatus) {
		this.attemptedStatus = attemptedStatus;
	}

}
