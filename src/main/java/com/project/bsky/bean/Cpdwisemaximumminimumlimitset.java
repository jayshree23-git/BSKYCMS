/**
 * 
 */
package com.project.bsky.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hrusikesh.mohanty
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cpdwisemaximumminimumlimitset {
	private Long maxminconfigid;
	private Long cpduserid;
	private Long maxlimt;
	private Long minlimit;
	private String createdon;
	private Long createdby;
	private String updatedon;
	private Long updatedby;
	private String Fullname;
	private String Assignedupto;
	private Long bskyuserid;
}
