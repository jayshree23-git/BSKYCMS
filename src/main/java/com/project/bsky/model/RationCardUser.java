/**
 * 
 */
package com.project.bsky.model;

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
@Table(name = "T_RATIONCARDACCESSUSER")
public class RationCardUser {
	
	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name = "ACCESS_ID")
	private Long accessid;
	
	@Column(name = "ACCESS_USER_NAME")
	private String accessusername;
	
	@Column(name = "STATUS_FLAG")
	private Integer statusflag;
	
	@Column(name = "MOBILE_NO")
	private String mobileno;
}
