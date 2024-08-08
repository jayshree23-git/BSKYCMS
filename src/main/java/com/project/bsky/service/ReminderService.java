package com.project.bsky.service;

public interface ReminderService {

	void startExecution(String data) throws Exception;

	void stopExecution(String data) throws Exception;

	void getRemindercron(String data) throws Exception;

}
