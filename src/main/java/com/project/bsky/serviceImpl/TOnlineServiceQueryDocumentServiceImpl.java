package com.project.bsky.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.entity.TOnlineServiceQueryDocument;
import com.project.bsky.repository.TOnlineServiceQueryDocumentRepository;
import com.project.bsky.service.TOnlineServiceQueryDocumentService;


@Service
public class TOnlineServiceQueryDocumentServiceImpl implements TOnlineServiceQueryDocumentService {
	@Autowired
	private TOnlineServiceQueryDocumentRepository tOnlineServiceQueryDocumentRepository;
	@Override
	public void save(TOnlineServiceQueryDocument tOnlineServiceQueryDocument) {
		tOnlineServiceQueryDocumentRepository.save(tOnlineServiceQueryDocument);
	}

}
