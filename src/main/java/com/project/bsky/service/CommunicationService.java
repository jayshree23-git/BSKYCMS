package com.project.bsky.service;

import java.util.List;

import com.project.bsky.config.dto.MessagePassingDto;
import com.project.bsky.entity.MessagePassingEntity;

public interface CommunicationService {
		
	MessagePassingEntity saveData(String messagePassing) throws Exception;

	List<MessagePassingDto> getMessageDetails(String viewMessage);

	void delete(String[] arrOfStr);

	void publish(String[] arrOfStr);

	void archive(String[] arrOfStr);

	void unPublish(String[] arrOfStr);
}
