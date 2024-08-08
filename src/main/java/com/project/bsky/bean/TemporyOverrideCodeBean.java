/**
 * 
 */
package com.project.bsky.bean;

import java.sql.Timestamp;
import java.util.List;

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
public class TemporyOverrideCodeBean {
	private String stateCode;
	private String distCode;
//	private String hospitalCode;
	private String overrideCode;
	private Timestamp fromDate;
	private Timestamp toDate;
	private List<AuthmodeUIDBean> hospitalList;
}
