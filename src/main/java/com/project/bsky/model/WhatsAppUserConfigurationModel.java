package com.project.bsky.model;

import java.io.Serializable;
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
import lombok.ToString;

@Entity
@Data
@Table(name = "whatsapp_user_configuration ")
public class WhatsAppUserConfigurationModel implements Serializable{
	
	@Id
	@Column(name = "WHATSAPPCONFIGURATION_ID",unique = true,nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "whatsapp_user_configuration_whatsappconfiguration_id_seq")
	@SequenceGenerator(name = "whatsapp_user_configuration_whatsappconfiguration_id_seq", sequenceName = "whatsapp_user_configuration_whatsappconfiguration_id_seq", allocationSize = 1)
	private Long whatsappconfigurationId;
	
	@Column(name = "TEMPLATE_ID")
	private Long  templateid;
	
	@Column(name = "WHATSAPP_TEMPLATE_NAME")
	private String  whatsapptemplatename;
	
	@Column(name = "GROUPID")
	private Integer  groupid;
	
	@Column(name = "USERID")
	private Long  userid;
	
	@Column(name = "CREATEDBY")
	private Long createdby;
	
	@Column(name = "CREATEDON")
	private Date createdon;
	
	@Column(name = "UPDATEDBY")
	private Long updatedby;
	
	@Column(name = "UPDATEDON")
	private Date updatedon;
	
	@Column(name = "STATUSFLAG")
	private Integer statusflag;
	
	@Transient
	private String createbyname;
	
	@Transient
	private String createtime;
	

}
