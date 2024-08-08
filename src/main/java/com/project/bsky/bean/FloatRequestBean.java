/**
 * 
 */
package com.project.bsky.bean;

import java.sql.Date;

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
public class FloatRequestBean {
	private Long userId;
	private Date fromDate;
	private Date toDate;
	private Object stateCodeList;
	private Object distCodeList;
	private Object hospitalCodeList;
	private String flag;
	private Long snoUserId;
	private String floatNo;
	private Double snaAmount;
	private Integer schemecategoryid;
	private Integer searchtype;
}
