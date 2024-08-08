package com.project.bsky.service;

import java.util.List;
import java.util.Map;

import com.project.bsky.bean.Response;
import com.project.bsky.bean.Surveygroupmapping;
import com.project.bsky.model.Implant;

public interface ImplantService {

	Response saveImplant(Implant implant);

	List<Implant> getImplant();

	Response deleteImplant(Long implantId);

	Implant getbyimplantId(Long implantId);

	Response update(Long implantId, Implant implant);

	List<Object> getpackageicddetails(String procedurecode) throws Exception;

	Map<String, Object> getpackageicddetails(Surveygroupmapping implantconfigdata) throws Exception;

	List<Object> implantproceduremappeddata(String procedurecode, String packageheadercode) throws Exception;

}
