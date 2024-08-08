package com.project.bsky.service;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public interface CpdRegistrationPreviewService {
	String previewDetails(Integer cpdUserId) throws Exception;

	void commonDownloadMethod(String fileName, String prifix, String userid, HttpServletResponse response) throws IOException;


}
