package com.project.bsky.bean;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class Cpdspecialitydocbean {
	private Long packageid;
	private String packagename;
	private MultipartFile file;
	private Integer status;
	private String docname;
}
