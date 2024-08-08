/**
 * 
 */
package com.project.bsky.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
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
@Table(name = "USER_LOGIN_HISTORY")
public class LoginHistory {
	@Id
	
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_LOGIN_HISTORY_SEQ")
	@SequenceGenerator(name = "USER_LOGIN_HISTORY_SEQ", sequenceName = "USER_LOGIN_HISTORY_SEQ", allocationSize = 1)
	@Column(name = "LOGIN_ID")
	private Long loginId;

	@Column(name = "USERNAME")
	private String userName;

	@Column(name = "LOGIN_TIME")
	private Date createdOn;

	@Column(name = "STATUS_FLAG")
	private Character statusFlag;

	@Column(name = "USER_IP")
	private String userIP;

	public Long getLoginId() {
		return loginId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setLoginId(Long loginId) {
		this.loginId = loginId;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Character getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(Character statusFlag) {
		this.statusFlag = statusFlag;
	}

	public String getUserIP() {
		return userIP;
	}

	public void setUserIP(String userIP) {
		this.userIP = userIP;
	}

}
