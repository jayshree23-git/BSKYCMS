/**
 * 
 */
package com.project.bsky.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * @author priyanka.singh
 *
 */
@Data
@Entity
@Table(name = "BANK_MST")
public class BankMaster implements Serializable {
	
	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name = "BANK_ID")
	private Integer bankId;
	
	@Column(name = "BANK_NAME")
	private String bankName;
	
	@Column(name = "STATUS_FLAG")
	private Integer statusFlag;
	
	@ManyToOne
	@JoinColumn(name = "CREATED_BY")
	private UserDetails createdBy;
	
	@Column(name = "CREATED_ON")
	private Date createdOn;
	
	@ManyToOne
	@JoinColumn(name = "UPDATED_BY")
	private UserDetails updatedBy;
	
	@Column(name = "UPDATED_ON")
	private Date updatedOn;


	
	
	
	
	

}
