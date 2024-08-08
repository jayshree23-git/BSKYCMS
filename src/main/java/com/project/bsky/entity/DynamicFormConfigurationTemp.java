package com.project.bsky.entity;

import java.io.Serializable;
import java.sql.Date;

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
@Table(name="m_dyn_temp_form_configuration")
@TypeDef(name = "json", typeClass = JSONString.class)
public class DynamicFormConfigurationTemp implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="M_DYN_TEMP_FORM_CONFIG_SEQ")
	@SequenceGenerator(name="M_DYN_TEMP_FORM_CONFIG_SEQ", sequenceName="M_DYN_TEMP_FORM_CONFIG_SEQ", allocationSize=1)
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
	private Integer deletedFlag;	
	private Integer tinPublishStatus;
	private String otherStatus;
	private String vchSectionWiseTableName;
}
