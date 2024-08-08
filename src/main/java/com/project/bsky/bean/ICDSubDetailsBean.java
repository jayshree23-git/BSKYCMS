/**
 * 
 */
package com.project.bsky.bean;

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
public class ICDSubDetailsBean {
	private String icdCode;
	private String icdSubCode;
	private String icdSubName;
}
