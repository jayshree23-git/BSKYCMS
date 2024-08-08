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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author santanu.barad
 *
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "MAIL_SERVICE_ERROR_LOG")
public class MailServiceErrorLog {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MAIL_SERVICE_ERROR_LOG_id_gen")
	@SequenceGenerator(name = "MAIL_SERVICE_ERROR_LOG_id_gen", sequenceName = "MAIL_SERVICE_ERROR_LOG_SEQ", allocationSize = 1)
	@Column(name = "ERROR_LOG_ID", nullable = false)
	private Integer id;

	@Column(name = "MAIL_SERVICE_ID", nullable = false)
	private Integer mailServiceId;

	@Column(name = "ERROR_LOG_MESSAGE", nullable = false, length = 4000)
	private String errorLogMessage;

	@Column(name = "ERROR_LOG_DATE", nullable = false)
	private Date errorLogDate;

	@Column(name = "CREATED_ON", nullable = false)
	private Date createdOn;

	@Column(name = "CREATED_BY", nullable = false, length = 10)
	private String createdBy;

	@Column(name = "UPDATED_ON")
	private Date updatedOn;

	@Column(name = "UPDATED_BY", length = 10)
	private String updatedBy;

	@Column(name = "STATUS_FLAG", nullable = false)
	private Integer statusFlag;
}
