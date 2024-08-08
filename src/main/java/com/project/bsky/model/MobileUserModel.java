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
 * @author preetam.mishra
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USERDETAILS")
public class MobileUserModel {
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
	
	@Column(name = "COMPANYCODE")
	private String companyCode;
	
	@Column(name = "EMAIL")
	private String email;
}
