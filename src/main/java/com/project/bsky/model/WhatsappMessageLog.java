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
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * @author santanu.barad
 *
 */
@Getter
@Setter
@Entity
@Table(name = "WHATSAPP_MESSAGE_LOG")
public class WhatsappMessageLog {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WHATSAPP_MESSAGE_LOG_id_gen")
	@SequenceGenerator(name = "WHATSAPP_MESSAGE_LOG_id_gen", sequenceName = "SEQ_WHATSAPP_MESSAGE_LOG", allocationSize = 1)
	@Column(name = "LOG_ID", nullable = false)
	private Integer id;

	@Column(name = "TEMPLATE_ID", nullable = false)
	private Integer templateId;

	@Column(name = "USER_ID")
	private Long userId;

	@Column(name = "PHONE_NO", nullable = false, length = 20)
	private String phoneNo;

	@Column(name = "CLAIMS_PENDING")
	private Long claimsPending;

	@Column(name = "API_ID", nullable = false)
	private Integer apiId;

	@Column(name = "MESSAGE_STATUS")
	private Long messageStatus;

	@Column(name = "RESPONSE_MESSAGE")
	private String responseMessage;

	@Column(name = "ERROR_LOG_ID")
	private Integer errorLogId;

	@Column(name = "CREATED_BY", nullable = false)
	private Integer createdBy;

	@Column(name = "CREATED_ON", nullable = false)
	private Date createdOn;

	@Column(name = "UPDATED_BY")
	private Integer updatedBy;

	@Column(name = "UPDATED_ON")
	private Date updatedOn;

	@Column(name = "STATUS_FLAG", nullable = false)
	private Integer statusFlag;

	@Lob
	@Column(name = "REQUEST_DATA")
	private String requestData;
}
