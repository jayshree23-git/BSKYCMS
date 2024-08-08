package com.project.bsky.serviceImpl;

import java.util.Date;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.repository.CommunicationRepository;
import com.project.bsky.service.ReminderService;
@Service
public class ReminderServiceImpl implements ReminderService {

	@Autowired
	private CommunicationRepository communicationRepository;
	@Override
	public void startExecution(String data) throws Exception {
		JSONObject jsonObject=new JSONObject(data);
		Integer MessageConfigId=jsonObject.getInt("MessageConfigId");
		Integer processId=jsonObject.getInt("processId");
		communicationRepository.updateDataForExecute(MessageConfigId,processId);
	}
	@Override
	public void stopExecution(String data) throws Exception {
		JSONObject jsonObject=new JSONObject(data);
		Integer MessageConfigId=jsonObject.getInt("MessageConfigId");
		Integer processId=jsonObject.getInt("processId");
		Date date=new Date();
		communicationRepository.updateDataForStop(MessageConfigId,processId,date);
	}
	@Override
	public void getRemindercron(String data) throws Exception {
		JSONObject jsonObject=new JSONObject(data);
		Integer MessageConfigId=jsonObject.getInt("MessageConfigId");
		Integer processId=jsonObject.getInt("processId");
		Integer messageConfigType=jsonObject.getInt("MessageConfigType");
		String startDate=jsonObject.getString("startDate");
		String endDate=jsonObject.getString("endDate");
		String frequencyType=jsonObject.getString("frequencyType");
		String frequencyDuration=jsonObject.getString("frequencyDuration");
		String schedularUrl=jsonObject.getString("schedularUrl");
//		Integer schedularStatus=jsonObject.getInt("schedularStatus");
		communicationRepository.updateDataForCreate(MessageConfigId,processId,messageConfigType,startDate,endDate,frequencyType,frequencyDuration,schedularUrl);
	}
}
