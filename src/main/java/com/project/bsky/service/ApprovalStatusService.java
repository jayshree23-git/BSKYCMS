package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.HospitalSpecialistListBean;

public interface ApprovalStatusService {

	List<HospitalSpecialistListBean> getapprovalstatusllist(String hospitalcode);

}
