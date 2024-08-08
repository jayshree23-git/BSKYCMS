package com.project.bsky.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;


@Entity
@Data
@Table(name = "WHATSAPP_TEMPLATE")
public class WhatsAppTemplateModel {
	
private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "TEMPLATE_ID")
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") //for oracle sequence generate
	private Long templateId ;

	@NotEmpty
	@NotBlank
	@NotNull
	@Column(name = "TEMPLATE_NAME")
	private String templateName;
	
	@Column(name = "CREATED_BY")
	private Integer createdBy;

	@CreationTimestamp
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "UPDATED_BY")
	private Integer updatedBy;

	@Column(name = "UPDATED_ON")
	private Date updatedOn;

	@Column(name = "STATUS_FLAG")
	private Integer bitStatus;
	
	@Column(name = "ODISHA_GOV_TEMPLATE_ID")
	private Long odishagovtemplateid;
	
	@Column(name = "TEMPLATE_BODY")
	private String templatebody;
	
	@Column(name = "TEMPLATE_DESC")
	private String templatedesc;	
	
	

}
