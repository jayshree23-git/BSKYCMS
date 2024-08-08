package com.project.bsky.bean;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class InteenalCommunicationBean {
	
	private Long userid;
	private Long towhom;
	private String reqfor;
	private String description;
	private Date date;
	private String priority;
	private MultipartFile file2;
	
	private String taken;
	private Long intcommid;
	private String reqbyname;
	private String towhomename;
	private String reqbydate;
	private String reqatch;
	private String resolveatch;
	private String progstatus;
	private String createon;
	private String remarks;

}
