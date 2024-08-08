package com.project.bsky.entity;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.TypeDef;
import org.json.JSONString;

import lombok.Data;

@Data
@Entity
@Table(name="m_dyn_form_configuration")
@TypeDef(name = "json", typeClass = JSONString.class)
public class DynamicFormConfigurationApp implements Serializable {
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="M_DYN_FORM_CONFIGURATION_SEQ")
	@SequenceGenerator(name="M_DYN_FORM_CONFIGURATION_SEQ", sequenceName="M_DYN_FORM_CONFIGURATION_SEQ", allocationSize=1)
	private Integer configurationId;
	private Integer itemId;
	private Integer sectionId;
	@Column(length=10485760)
	private String formDetails;
	private Integer status;
	private Date createdOn;
	private Integer createdBy;
	private Date updatedOn;
	private Integer updatedBy;
	private Boolean deletedFlag;
	private Integer tinPublishStatus;
	@Column(name="other_status")
	private String otherStatus;
	private String vchSectionWiseTableName;
}

