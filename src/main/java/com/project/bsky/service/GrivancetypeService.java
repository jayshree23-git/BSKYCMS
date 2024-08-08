package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.Response;
import com.project.bsky.model.Grivancetype;

public interface GrivancetypeService {

	Response saveGrivancetypeData(Grivancetype grivancetype);

	List<Grivancetype> getGrivancetypeData();

	Response updateGrivancetypeData(Grivancetype grivancetype);
	
	Grivancetype getgrievancetypeId(Long userid);
}
