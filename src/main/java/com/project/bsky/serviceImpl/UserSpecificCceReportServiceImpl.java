package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.UserspecificReportBean;
import com.project.bsky.model.UserDetails;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.UserSpecificCceReportService;

@Service
public class UserSpecificCceReportServiceImpl implements UserSpecificCceReportService {
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private UserDetailsRepository userdetailsrepository;

	@Autowired
	private Logger logger;

	@Override
	public List<UserDetails> getCceUserlist() {
		return userdetailsrepository.findDetails();
	}

}
