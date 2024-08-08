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
@Table(name = "MAIL_SERVICE_LOG")
public class MailServiceLog {
	@Id
	@Column(name = "LOG_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MAIL_SERVICE_LOG_id_gen")
	@SequenceGenerator(name = "MAIL_SERVICE_LOG_id_gen", sequenceName = "MAIL_SERVICE_LOG_SEQ", allocationSize = 1)
	private Integer id;

	@Column(name = "MAIL_SERVICE_ID", nullable = false)
	private Integer mailServiceId;

	@Column(name = "MAIL_SERVICE_NAME", nullable = false, length = 100)
	private String mailServiceName;

	@Column(name = "USER_ID", nullable = false)
	private Integer userId;

	@Column(name = "MAIL_DATE", nullable = false)
	private Date mailDate;

	@Column(name = "MAIL_STATUS", nullable = false)
	private Integer mailStatus;

	@Column(name = "MAIL_ATTACHED_STATUS")
	private Integer mailAttachedStatus;

	@Column(name = "CRATED_BY", nullable = false)
	private String cratedBy;

	@Column(name = "CREATED_ON", nullable = false)
	private Date createdOn;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "UPDATED_ON")
	private Date updatedOn;

	@Column(name = "ERROR_LOG_ID")
	private Integer errorLogId;

	@Column(name = "STATUS_FLAG", nullable = false)
	private Boolean statusFlag = false;

}
