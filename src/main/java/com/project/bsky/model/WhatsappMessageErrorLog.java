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
@Table(name = "WHATSAPP_MESSAGE_ERROR_LOG")
public class WhatsappMessageErrorLog {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WHATSAPP_MESSAGE_ERROR_LOG_id_gen")
	@SequenceGenerator(name = "WHATSAPP_MESSAGE_ERROR_LOG_id_gen", sequenceName = "SEQ_WHATSAPP_MESSAGE_ERROR_LOG", allocationSize = 1)
	@Column(name = "ERROR_LOG_ID", nullable = false)
	private Integer id;

	@Column(name = "ERROR_CODE", nullable = false, length = 100)
	private String errorCode;

	@Column(name = "ERROR_MESSAGE", nullable = false, length = 500)
	private String errorMessage;

	@Lob
	@Column(name = "ERROR_STACK")
	private String errorStack;

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
}
