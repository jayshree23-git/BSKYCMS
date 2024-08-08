/**
 * 
 */
package com.project.bsky.bean;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

/**
 * @author rajendra.sahoo
 *
 */
@Data
public class Mskgrivfilebean {
	
	private String count;
	private MultipartFile docfile;
	private String docname;
	private MultipartFile vdofile;
	private String vdoname;

}
