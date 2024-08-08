package com.project.bsky.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.model.HospitalProfile;
import com.project.bsky.model.UserDetails;
import com.project.bsky.repository.HospitalProfileRepository;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.UserLoggingsuccessService;

@Service
public class UserLoggingsucessServiceImpl implements UserLoggingsuccessService {

	@Autowired
	private UserDetailsRepository userdetailsrepo;

	@Autowired
	private HospitalProfileRepository hospitalProfileRepository;



	@Override
	public HospitalProfile getHospitalCode(Integer userId) {

		return hospitalProfileRepository.getHospitalProfileRepository(userId);
	}
}
