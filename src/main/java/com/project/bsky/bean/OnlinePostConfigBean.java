package com.project.bsky.bean;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class OnlinePostConfigBean {
	
   private Long postid;
	
	private String currentjobdescription;
	
	private String onlineapplyfrom;
	
	private String onlineapplyto;
	
	private String advertisementnumb;
	
	private String advertisementdate;
	private Long onlinepublish;
	
	private Long noofvaccancy;
	
	private Long createdBy;
	private Long  bitStatus;
	
	private MultipartFile filename;
	private String filename1;
	
	private Long configid;
	
	

}
