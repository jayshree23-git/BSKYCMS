package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.Response;
import com.project.bsky.model.ActionRemark;

public interface RemarksMasterService {

	Response saveRemarksMaster(ActionRemark actionRemark);

	List<ActionRemark> getAllRemarks();

	ActionRemark getbyremark(Long id);

	Response updateremark(Long id, ActionRemark actionRemark);

}
