package com.project.bsky.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.Response;
import com.project.bsky.model.NotificationDetails;

public interface NotificationDetailsService {

	Response save(NotificationDetails notificationdetails, MultipartFile form);

	List<NotificationDetails> findall();

	Response update(NotificationDetails notificationdetails, MultipartFile form);

	List<NotificationDetails> getdataforNotificationDetails(String fromdate, String todate);

	List<NotificationDetails> getdataforNotificationDetailsOns(String fromdate, String todate, String groupId);

	List<NotificationDetails> getnotificationshow(Integer groupId);

	void downLoadPassbook(String fileName, String year, HttpServletResponse response, String month);

	List<Map<String, Object>> getHospitalListClaimsNotVerified(Long userId, int actionCode, int days);

	List<Map<String, Object>> getHospitalClaimsNotVerified(Long userId, String hospitalCode, int actionCode, int days);
}
