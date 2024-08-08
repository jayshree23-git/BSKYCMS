/**
 * 
 */
package com.project.bsky.bean;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author santanu.barad
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ICDDetailsBean {
	private String icdCode;
	private String icdName;
	private String icdMode;
	private List<ICDSubDetailsBean> subList;
	private Long byGroupId;
}
