/**
 * 
 */
package com.project.bsky.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author santanu.barad
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PACKAGE_SCHEME_MAPPING")
public class PackageSchemeMapping {
	@Id
	@Column(name = "MAPPINGID")
	private Long mappingId;
	@Column(name = "PACKAGEHEADERCODE")
	private String packageHeaderCode;
	@Column(name = "PROCEDURECODE")
	private String procedureCode;
	@Column(name = "SCHEMEID")
	private Long schemeId;
	@Column(name = "SCHEMECATEGORYID")
	private Long schemeCategoryId;
	@Column(name = "STATUSFLAG")
	private Long statusFlag;
}
