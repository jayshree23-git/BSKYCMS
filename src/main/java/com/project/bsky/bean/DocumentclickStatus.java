/**
 * 
 */
package com.project.bsky.bean;

import java.util.List;

import lombok.Data;

/**
 * @author hrusikesh.mohanty
 *
 */
@Data
public class DocumentclickStatus{
	private String urnumber;
	private List<String> documnetname;
	private Long claimid;
	private Long userid;
	private Long groupid;
	private Long documnetStatus;
	private String pagenameAction;
		
}
