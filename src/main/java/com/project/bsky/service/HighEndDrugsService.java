package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.HighEndDrugsBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.HighEndDrugs;
import com.project.bsky.model.Implant;

public interface HighEndDrugsService {
    List<Implant> getImplantName();

    Response saveHighEndDrugs(HighEndDrugsBean highEndDrugsBean);

    List<HighEndDrugs> getAllHighEndDrugs();

    HighEndDrugs getHighEndDrugsById(Long id);

    Response deleteHighEndDrugsById(Long id);

    Response updateHighEndDrugsById(Long id, HighEndDrugsBean highEndDrugsBean);

    List<Object> getWardName(String action);

    List<Object> getImplantCode(String action, Integer wardCategoryId);

    Long checkDrugCode(String drugCode);

}
