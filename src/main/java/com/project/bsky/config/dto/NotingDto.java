package com.project.bsky.config.dto;

import java.util.ArrayList;

import com.project.bsky.entity.QueryDocEntity;

import lombok.Data;

@Data
public class NotingDto {
	private String intfromauthority;
	private Integer intonlineserviceid;
	private Integer intnotingsid;
	private Integer intprocessid;
	private Integer intprofileid;
	private Integer intstatus;
	private String dtactiontaken;
	private String inttoauthority;
	private String txtnoting;
	private String txtrevertremark;
	private Integer tinresubmitstatus;
	private Integer tinstagectr;
	private String jsnotherdetails;

	private String jsnomediadetails;
	private String priority;
	
	private ArrayList<QueryDocEntity> queryDoc;
}
