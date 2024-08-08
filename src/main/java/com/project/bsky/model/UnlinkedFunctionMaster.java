package com.project.bsky.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.ToString;

@Entity
@ToString
@Table(name = "FUNCTION_MASTER_UNLINKED")
@Data
public class UnlinkedFunctionMaster implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "FUNCTION_ID")
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") // for oracle sequence generate
	private Long functionId;

	@NotEmpty
	@NotBlank
	@NotNull
	@Column(name = "FN_NAME")
	private String functionName;

	@NotEmpty
	@NotBlank
	@NotNull
	@Column(name = "FN_URL")
	private String fileName;

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

	@Column(name = "FN_DESCRIPTION")
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "USER_ID", referencedColumnName = "USERID")
	private UserDetails userId;

}
