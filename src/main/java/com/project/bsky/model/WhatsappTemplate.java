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

/**
 * @author santanu.barad
 *
 */
@Getter
@Setter
@Entity
@Table(name = "WHATSAPP_TEMPLATE")
public class WhatsappTemplate {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WHATSAPP_TEMPLATE_id_gen")
	@SequenceGenerator(name = "WHATSAPP_TEMPLATE_id_gen", sequenceName = "SEQ_WHATSAPP_TEMPLATE", allocationSize = 1)
	@Column(name = "TEMPLATE_ID", nullable = false)
	private Integer id;

	@Column(name = "ODISHA_GOV_TEMPLATE_ID")
	private Long odishaGovTemplateId;

	@Column(name = "TEMPLATE_NAME", nullable = false, length = 100)
	private String templateName;

	@Column(name = "TEMPLATE_BODY", length = 500)
	private String templateBody;

	@Column(name = "TEMPLATE_DESC", length = 500)
	private String templateDesc;

	@Column(name = "CREATED_ON", nullable = false)
	private Date createdOn;

	@Column(name = "CREATED_BY", nullable = false)
	private Integer createdBy;

	@Column(name = "UPDATED_BY")
	private Integer updatedBy;

	@Column(name = "UPDATED_ON")
	private Date updatedOn;

	@Column(name = "STATUS_FLAG", nullable = false)
	private Integer statusFlag;
}
