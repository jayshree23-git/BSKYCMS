package com.project.bsky.bean;

import java.util.List;

import lombok.Data;

@Data
public class Uidauthmodetagbean {

	private Long actionBy;
	private List<AuthmodeUIDBean> selectedlist;
	private String remark;
	private Long userId;
	private Integer action;
}
