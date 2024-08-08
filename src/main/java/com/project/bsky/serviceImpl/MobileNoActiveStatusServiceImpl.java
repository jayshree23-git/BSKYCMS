package com.project.bsky.serviceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.model.MobilenoActiveStatus;
import com.project.bsky.repository.MobileNoActiveStatusRepository;
import com.project.bsky.service.MobileNoActiveStatuservice;

@Service

public class MobileNoActiveStatusServiceImpl implements MobileNoActiveStatuservice {
	@Autowired
	private MobileNoActiveStatusRepository mobileNoActiveStatusRepository;
	
	Calendar cal = Calendar.getInstance();
	Date date = cal.getTime();

	@Override
	public List<MobilenoActiveStatus> getmobileNoActiveStatus() {
		List<MobilenoActiveStatus> mobileNoActiveStatusResponse=new ArrayList<>();
		List<MobilenoActiveStatus> findAll = mobileNoActiveStatusRepository.findAll();
		if(findAll!=null   ) {
			for (MobilenoActiveStatus mobilenoActiveStatus : findAll) {
				if(mobilenoActiveStatus!=null && mobilenoActiveStatus.getDeletedFlag()==0) {
					mobileNoActiveStatusResponse.add(mobilenoActiveStatus);
				}
			}
		}
		return mobileNoActiveStatusResponse;
	}

	@Override
	public List<MobilenoActiveStatus> getMobileNoActiveStatusNotConnected() {
		List<MobilenoActiveStatus> mobileNoActiveStatusResponse=new ArrayList<>();
		List<MobilenoActiveStatus> activeStatus = mobileNoActiveStatusRepository.findAll();
		for (MobilenoActiveStatus mobilenoActiveStatus : activeStatus) {
			if(mobilenoActiveStatus.equals("Not Connected")) {
				mobileNoActiveStatusResponse.add(mobilenoActiveStatus);
				
			}
		}
		return mobileNoActiveStatusResponse;
	}
	}
	
	

