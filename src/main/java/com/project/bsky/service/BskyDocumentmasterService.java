package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.DocProcedureMstdocbean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.BskyDocumentmaster;

public interface BskyDocumentmasterService {

	Response savedocumentmst(BskyDocumentmaster bsydocumentmaster) throws Exception;

	List<Object> getdocumentmst() throws Exception;
	
	List<Object> getdocumentmstname(String procedurecode ) throws Exception;

	Response updatedocumentmst(BskyDocumentmaster bsydocumentmaster) throws Exception;

	List<Object> getprocdurethroughheadercode(String headerCode) throws Exception;

	Response savedocproceduremapping(DocProcedureMstdocbean bsydocumentmapping) throws Exception;

	List<Object> getdocproctaggedlist(String headerCode) throws Exception;

	List<Object> getproceduretagggeddoclist(String procedureCode) throws Exception;

	List<Object> getdocproctaggedlistforexcel(String headerCode) throws Exception;

}
