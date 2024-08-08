package com.project.bsky.bean;

import java.util.List;

import lombok.Data;

@Data
public class DocProcedureMstdocbean {

	private String procedureCode;
	private String headerCode;
	private Long createdby;
	private List<Documentmstbean> selectitemlist;
}
