/**
 * 
 */
package com.project.bsky.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="DYN_JSOPTTEDTL_DATA")
public class DYNJSONOptionTextDetailsData {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="DYN_JSOPTTEDTL_DATA_SEQ")
	@SequenceGenerator(name="DYN_JSOPTTEDTL_DATA_SEQ", sequenceName="DYN_JSOPTTEDTL_DATA_SEQ", allocationSize=1)
	private Integer detialsId;
	private Integer intId;
	private Integer onlineServiceId;
	private String tableName;
	@Column(length=10485760)
	private String jsonOptionTextDetails;
}
