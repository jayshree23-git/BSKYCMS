package com.project.bsky.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.InteenalCommunicationBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.InternalCommunication_user;

public interface InternalCommunicationSercice {

	List<InternalCommunication_user> getintcommuserlist();

	Response saveintcomm(InteenalCommunicationBean form);

	List<InteenalCommunicationBean> getintcommlist(Long userid);

	List<InteenalCommunicationBean> getintcommtasklist(Long userid);

	void downLoadPassbook(String fileName, String year, HttpServletResponse response, String month);

	Response updateintcomm(MultipartFile form, String remarks, Long userid, Long commid);

}
