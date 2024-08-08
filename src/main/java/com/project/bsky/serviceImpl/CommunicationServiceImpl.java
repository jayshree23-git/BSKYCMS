package com.project.bsky.serviceImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.config.dto.MessagePassingDto;
import com.project.bsky.entity.MessagePassingEntity;
import com.project.bsky.repository.CommunicationRepository;
import com.project.bsky.repository.ProcessRepository;
import com.project.bsky.service.CommunicationService;

@Service
public class CommunicationServiceImpl implements CommunicationService {
	
	@Autowired
	private CommunicationRepository communicationRepository;
	
	@Autowired
	private ProcessRepository processRepository;

	@Override
	public MessagePassingEntity saveData(String messagePassing) throws Exception {
		JSONObject json = new JSONObject(messagePassing);
		MessagePassingEntity messagePassingEntity = new MessagePassingEntity();
		messagePassingEntity.setIntMessageConfigId(json.getInt("intMessageConfigId"));
		
		return null;
	}

	@Override
	public List<MessagePassingDto> getMessageDetails(String viewMessage) {
		List<MessagePassingEntity> messagePassingEntity = communicationRepository.findAllData();
		List<MessagePassingDto> messagePassingList = new ArrayList<>();
		for(MessagePassingEntity messagePassingEntity2 : messagePassingEntity) {
			MessagePassingDto MessagePassingDto = new MessagePassingDto();
			MessagePassingDto.setIntMessageConfigId(messagePassingEntity2.getIntMessageConfigId());
			MessagePassingDto.setIntMessageConfigType(messagePassingEntity2.getIntMessageConfigType());
			MessagePassingDto.setIntmessageType(messagePassingEntity2.getIntmessageType());
			MessagePassingDto.setVchSmsTemplateId(messagePassingEntity2.getVchSmsTemplateId());
			MessagePassingDto.setBitDeletedFlag(messagePassingEntity2.getBitDeletedFlag());
			MessagePassingDto.setTinPublishStatus(messagePassingEntity2.getTinPublishStatus());
			MessagePassingDto.setIntProcessId(messagePassingEntity2.getIntProcessId());
			Date date = messagePassingEntity2.getDtmCreatedOn();
			SimpleDateFormat sm=new SimpleDateFormat("dd-MMM-yyyy");
			MessagePassingDto.setDtmCreatedOn(sm.format(date));
			////System.out.println(messagePassingEntity2.getIntProcessId());
			MessagePassingDto.setSchedularStatus(messagePassingEntity2.getSchedularStatus());
		//	Date date1 = messagePassingEntity2.getStopExecutionDate();
			MessagePassingDto.setSchedularStatus(messagePassingEntity2.getSchedularStatus());
			if(messagePassingEntity2.getStopExecutionDate()==null) {
				MessagePassingDto.setStopExecutionDate(null);
			}else {
				Date date1 = messagePassingEntity2.getStopExecutionDate();
				MessagePassingDto.setStopExecutionDate(sm.format(date1));

			}
			String mPn= processRepository.findProcessNameByIntProcessId(messagePassingEntity2.getIntProcessId());
			MessagePassingDto.setVchProcessName(mPn);
			messagePassingList.add(MessagePassingDto);
		}
		return messagePassingList;
	}

	@Override
	public void delete(String[] arrOfStr) {
		for(int i=0; i<arrOfStr.length; i++) {
			MessagePassingEntity messagePassingEntity = communicationRepository.getById(Integer.parseInt(arrOfStr[i]));
			MessagePassingEntity MessagePassingEntity1 = new MessagePassingEntity();
			MessagePassingEntity1.setIntMessageConfigType(messagePassingEntity.getIntMessageConfigType());
			MessagePassingEntity1.setIntProcessId(messagePassingEntity.getIntProcessId());
			MessagePassingEntity1.setVchSmsTemplateId(messagePassingEntity.getVchSmsTemplateId());
			MessagePassingEntity1.setIntmessageType(messagePassingEntity.getIntMessageConfigType());
			MessagePassingEntity1.setIntEventType(messagePassingEntity.getIntEventType());
			MessagePassingEntity1.setVchSubject(messagePassingEntity.getVchSubject());
			MessagePassingEntity1.setVchMessageContent(messagePassingEntity.getVchMessageContent());
			MessagePassingEntity1.setTinPublishStatus(messagePassingEntity.getTinPublishStatus());
			MessagePassingEntity1.setBitDeletedFlag(true);
			MessagePassingEntity1.setDtmCreatedOn(messagePassingEntity.getDtmCreatedOn());
			MessagePassingEntity1.setDtmUpdatedOn(messagePassingEntity.getDtmUpdatedOn());
			MessagePassingEntity1.setIntCreatedBy(messagePassingEntity.getIntCreatedBy());
			MessagePassingEntity1.setIntUpdatedBy(messagePassingEntity.getIntUpdatedBy());
			MessagePassingEntity1.setDtmStartDate(messagePassingEntity.getDtmStartDate());
			MessagePassingEntity1.setDtmEndDate(messagePassingEntity.getDtmEndDate());
			MessagePassingEntity1.setFrequencyDuration(messagePassingEntity.getFrequencyDuration());
			MessagePassingEntity1.setFrequencyType(messagePassingEntity.getFrequencyType());
			MessagePassingEntity1.setSchedularUrl(messagePassingEntity.getSchedularUrl());
			MessagePassingEntity1.setSchedularStatus(messagePassingEntity.getSchedularStatus());
			MessagePassingEntity1.setStopExecutionDate(messagePassingEntity.getStopExecutionDate());
			MessagePassingEntity1.setIntDocumentType(messagePassingEntity.getIntDocumentType());
			MessagePassingEntity1.setVchDocument(messagePassingEntity.getVchDocument());
			MessagePassingEntity1.setIntMailTemplate(messagePassingEntity.getIntMailTemplate());
			communicationRepository.save(MessagePassingEntity1);
		}
		
	}

	@Override
	public void publish(String[] arrOfStr) {
		for(int i=0; i<arrOfStr.length; i++) {
			MessagePassingEntity messagePassingEntity = communicationRepository.getById(Integer.parseInt(arrOfStr[i]));
			MessagePassingEntity MessagePassingEntity1 = new MessagePassingEntity();
			MessagePassingEntity1.setIntMessageConfigType(messagePassingEntity.getIntMessageConfigType());
			MessagePassingEntity1.setIntProcessId(messagePassingEntity.getIntProcessId());
			MessagePassingEntity1.setVchSmsTemplateId(messagePassingEntity.getVchSmsTemplateId());
			MessagePassingEntity1.setIntmessageType(messagePassingEntity.getIntMessageConfigType());
			MessagePassingEntity1.setIntEventType(messagePassingEntity.getIntEventType());
			MessagePassingEntity1.setVchSubject(messagePassingEntity.getVchSubject());
			MessagePassingEntity1.setVchMessageContent(messagePassingEntity.getVchMessageContent());
			MessagePassingEntity1.setTinPublishStatus(1);
			MessagePassingEntity1.setBitDeletedFlag(messagePassingEntity.getBitDeletedFlag());
			MessagePassingEntity1.setDtmCreatedOn(messagePassingEntity.getDtmCreatedOn());
			MessagePassingEntity1.setDtmUpdatedOn(messagePassingEntity.getDtmUpdatedOn());
			MessagePassingEntity1.setIntCreatedBy(messagePassingEntity.getIntCreatedBy());
			MessagePassingEntity1.setIntUpdatedBy(messagePassingEntity.getIntUpdatedBy());
			MessagePassingEntity1.setDtmStartDate(messagePassingEntity.getDtmStartDate());
			MessagePassingEntity1.setDtmEndDate(messagePassingEntity.getDtmEndDate());
			MessagePassingEntity1.setFrequencyDuration(messagePassingEntity.getFrequencyDuration());
			MessagePassingEntity1.setFrequencyType(messagePassingEntity.getFrequencyType());
			MessagePassingEntity1.setSchedularUrl(messagePassingEntity.getSchedularUrl());
			MessagePassingEntity1.setSchedularStatus(messagePassingEntity.getSchedularStatus());
			MessagePassingEntity1.setStopExecutionDate(messagePassingEntity.getStopExecutionDate());
			MessagePassingEntity1.setIntDocumentType(messagePassingEntity.getIntDocumentType());
			MessagePassingEntity1.setVchDocument(messagePassingEntity.getVchDocument());
			MessagePassingEntity1.setIntMailTemplate(messagePassingEntity.getIntMailTemplate());
			communicationRepository.save(MessagePassingEntity1);
		}
		
	}

	@Override
	public void archive(String[] arrOfStr) {
		
	}

	@Override
	public void unPublish(String[] arrOfStr) {
		for(int i=0; i<arrOfStr.length; i++) {
			MessagePassingEntity messagePassingEntity = communicationRepository.getById(Integer.parseInt(arrOfStr[i]));
			MessagePassingEntity MessagePassingEntity1 = new MessagePassingEntity();
			MessagePassingEntity1.setIntMessageConfigType(messagePassingEntity.getIntMessageConfigType());
			MessagePassingEntity1.setIntProcessId(messagePassingEntity.getIntProcessId());
			MessagePassingEntity1.setVchSmsTemplateId(messagePassingEntity.getVchSmsTemplateId());
			MessagePassingEntity1.setIntmessageType(messagePassingEntity.getIntMessageConfigType());
			MessagePassingEntity1.setIntEventType(messagePassingEntity.getIntEventType());
			MessagePassingEntity1.setVchSubject(messagePassingEntity.getVchSubject());
			MessagePassingEntity1.setVchMessageContent(messagePassingEntity.getVchMessageContent());
			MessagePassingEntity1.setTinPublishStatus(0);
			MessagePassingEntity1.setBitDeletedFlag(messagePassingEntity.getBitDeletedFlag());
			MessagePassingEntity1.setDtmCreatedOn(messagePassingEntity.getDtmCreatedOn());
			MessagePassingEntity1.setDtmUpdatedOn(messagePassingEntity.getDtmUpdatedOn());
			MessagePassingEntity1.setIntCreatedBy(messagePassingEntity.getIntCreatedBy());
			MessagePassingEntity1.setIntUpdatedBy(messagePassingEntity.getIntUpdatedBy());
			MessagePassingEntity1.setDtmStartDate(messagePassingEntity.getDtmStartDate());
			MessagePassingEntity1.setDtmEndDate(messagePassingEntity.getDtmEndDate());
			MessagePassingEntity1.setFrequencyDuration(messagePassingEntity.getFrequencyDuration());
			MessagePassingEntity1.setFrequencyType(messagePassingEntity.getFrequencyType());
			MessagePassingEntity1.setSchedularUrl(messagePassingEntity.getSchedularUrl());
			MessagePassingEntity1.setSchedularStatus(messagePassingEntity.getSchedularStatus());
			MessagePassingEntity1.setStopExecutionDate(messagePassingEntity.getStopExecutionDate());
			MessagePassingEntity1.setIntDocumentType(messagePassingEntity.getIntDocumentType());
			MessagePassingEntity1.setVchDocument(messagePassingEntity.getVchDocument());
			MessagePassingEntity1.setIntMailTemplate(messagePassingEntity.getIntMailTemplate());
			communicationRepository.save(MessagePassingEntity1);
		}
		
	}
	
}
