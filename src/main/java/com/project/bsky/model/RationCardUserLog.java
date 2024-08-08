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

import lombok.Data;

/**
 * @author rajendra.sahoo
 *
 */
@Data
@Entity
@Table(name = "T_RATIONCARDACCESSUSER_LOG")
public class RationCardUserLog {
	
	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name = "ACCESS_LOG_ID")
	private Long accesslogid;

	@Column(name = "ACCESS_ID")
	private Long accessid;
	
	@Column(name = "ACCESS_USER_NAME")
	private String accessusername;
	
	@Column(name = "MOBILE_NO")
	private String mobileno;
	
	@Column(name = "CARD_NUMBER")
	private String cardno;
	
	@Column(name = "SEARCH_TYPE")
	private String searchtype;
	
	@Column(name = "CREATED_ON")
	private Date createdon;
}
