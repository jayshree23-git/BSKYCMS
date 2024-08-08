package com.project.bsky.service;

import java.util.List;

import com.project.bsky.model.MobilenoActiveStatus;

public interface MobileNoActiveStatuservice {

	List<MobilenoActiveStatus> getmobileNoActiveStatus();

	List<MobilenoActiveStatus> getMobileNoActiveStatusNotConnected();

}
