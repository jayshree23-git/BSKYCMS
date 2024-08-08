package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.Response;
import com.project.bsky.model.Ward;
import com.project.bsky.model.WardDetails;

public interface WardDetailsService {

	Response saveWardDetails(WardDetails wardDetails);

	List<WardDetails> getwarddetails();

	List<Ward> getAllward(Long wardMasterId);

}
