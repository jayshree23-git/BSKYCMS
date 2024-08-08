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
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.ToString;

/**
 * @author rajendra.sahoo
 *
 */
@Entity
@Data
@Table(name = "BSKY_MESSAGE_MASTER")
public class BskyMessageMaster implements Serializable {
	
	@Id
	@Column(name = "MESSAGE_ID")
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") //for oracle sequence generate
	private Long messageid;
	
	@Column(name = "MESSAGE_TYPE")
	private String messagetype;
	
	@Column(name = "MESSAGE_PURPOSE")
	private String messageprps;
	
	@Column(name = "MESSAGE_CONTENT")
	private String messagecontaint;
	
	@Column(name = "MESSAGE_CONTENT_FORMAT")
	private String messagecontaintformat;
	
	@Column(name = "CREATED_BY")
	private Long createdby;
	
	@Column(name = "CREATED_ON")
	private Date createdon;
	
	@Column(name = "UPDATED_BY")
	private Long updatedby;
	
	@Column(name = "UPDATED_ON")
	private Date updatedon;
	
	@Column(name = "STATUS_FLAG")
	private Integer statusflag;
	
	@Column(name = "TEMPLATE_ID")
	private String tempid;
	
	@Column(name = "SEND_SMS_STATUS")
	private String smsstatus;
	
	@Column(name = "REMARKS")
	private String remarks;

}
