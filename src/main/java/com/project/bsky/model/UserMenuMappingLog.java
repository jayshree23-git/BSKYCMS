/**
 * 
 */
package com.project.bsky.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * @author rajendra.sahoo
 *
 */
@Entity
@Table(name = "USER_MENU_MAPPING_LOG")
@Data
public class UserMenuMappingLog implements Serializable {
	
	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") // for oracle sequence generate
	@Column(name = "MENU_MAPPING_LOGID")
	private Long userMappinglogId;
	
	@Column(name = "MAPPING_ID")
	private Integer mappingid;
	
	@Column(name = "MAPPING_STATUS")
	private Integer mappingstatus;
	
	@Column(name = "PRIMARY_LINK_ID")
	private Long primarylink;
	
	@Column(name = "GLOBAL_LINK_ID")
	private Integer globallink;
	
	@Column(name = "STATUSFLAG")
	private Integer status;
	
	@Column(name = "UPDATEDBY")
	private Long updatedby;
	
	@Column(name = "UPDATEDON")
	private Date updatedon;
	
	@Column(name = "USER_ID")
	private Long  Userid;
}
