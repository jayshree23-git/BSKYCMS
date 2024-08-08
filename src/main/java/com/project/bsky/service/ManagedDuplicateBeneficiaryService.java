package com.project.bsky.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.ManageDuplicateBeneficiaryBean;
import com.project.bsky.bean.Response;

public interface ManagedDuplicateBeneficiaryService {

	List<Object> manageduplicatebeneficiarylist(Long searchtype, String searchvalue);

	List<Object> beneficiarylist(Long searchtype, String searchvalue);

	Response inactivebeneficiary(ManageDuplicateBeneficiaryBean manageduplicatebeneficiarybean);

	List<Object> ongoingtreatmentlist(Long searchtype, String searchvalue);

	List<Object> manageduplicatebeneficiaryviewlist(Long searchtype, String searchvalue) throws Exception;

}
