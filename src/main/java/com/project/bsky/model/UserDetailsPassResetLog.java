package com.project.bsky.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "USERDETAILS_PASS_RESET_LOG")
public class UserDetailsPassResetLog implements Serializable {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERDETAILS_PASS_RESET_LOG_ID_SEQ")
	@SequenceGenerator(name = "USERDETAILS_PASS_RESET_LOG_ID_SEQ", sequenceName = "USERDETAILS_PASS_RESET_LOG_ID_SEQ", allocationSize = 1)
	@Column(name = "LOG_ID")
	private Long logId;
	
	//@Column(name = "USER_ID")
	//private Long userId;
	
	
	
	//@Column(name = "CREATED_BY")
	//private Long createdBy;
	
	@ManyToOne
	@JoinColumn(name = "CREATED_BY")
	private UserDetails createdBy;
	
	@Column(name = "CREATED_ON")
	private Date createdOn;
	
	
	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private UserDetails userId;


	public Long getLogId() {
		return logId;
	}


	public void setLogId(Long logId) {
		this.logId = logId;
	}


	public UserDetails getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(UserDetails createdBy) {
		this.createdBy = createdBy;
	}


	public Date getCreatedOn() {
		return createdOn;
	}


	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}


	public UserDetails getUserId() {
		return userId;
	}


	public void setUserId(UserDetails userId) {
		this.userId = userId;
	}


	@Override
	public String toString() {
		return "UserDetailsPassResetLog [logId=" + logId + ", createdBy=" + createdBy + ", createdOn=" + createdOn
				+ ", userId=" + userId + "]";
	}


	
	
	
	
	
	
	
	

}
