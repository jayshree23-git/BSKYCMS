/**
 * 
 */
package com.project.bsky.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author rajendra.sahoo
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Specilistbean {
	private Integer packageid;
	private String packagecode;
	private Integer privyear;
	private Integer bfrlastyear;
	private Integer status;
	private Long packageSubCatagoryId;
	private String packageSubCode;
	private Long procedureId;
	private String procedureCode;
	private Long hospitalTypeId;
	private Integer flag;
}
