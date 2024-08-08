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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.ToString;

	
@Entity
@Data
@Table(name = "M_POSTMASTER")
public class PostMasterModel  {
				
		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_POSTMASTER_POST_ID_SEQ")
		@SequenceGenerator(name = "M_POSTMASTER_POST_ID_SEQ", sequenceName = "M_POSTMASTER_POST_ID_SEQ", allocationSize = 1)

		@Column(name = "POST_ID")
		private Long postid;

		@NotEmpty
		@NotBlank
		@NotNull
		@Column(name = "POST_NAME")
		private String postname;
		
		@Column(name = "POST_DESCRIPTION")
		private String postdescription;
		
		
		@Column(name = "CREATEDBY")
		private Long createdBy;

		@CreationTimestamp
		@Column(name = "CREATEDON")
		private Date createdOn;
		
		@Column(name = "UPDATEDBY")
		private Long updatedBy;

		@Column(name = "UPDATEDON")
		private Date updatedOn;

		@Column(name = "STATUSFLAG")
		private Integer bitStatus;
		
				
}
