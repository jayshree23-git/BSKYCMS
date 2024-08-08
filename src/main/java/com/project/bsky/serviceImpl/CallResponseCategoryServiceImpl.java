package com.project.bsky.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.model.CallResponsecategory;
import com.project.bsky.repository.CallResponseCategoryRepository;
import com.project.bsky.service.CallResponseCategoryService;

@Service

public class CallResponseCategoryServiceImpl implements CallResponseCategoryService{
	@Autowired
	private CallResponseCategoryRepository callResponseCategoryRepository;
//	@Override
//	public List<CallResponsecategory> getallCallResponseCategory() {
//		List<CallResponsecategory> callResponse=new ArrayList<>();
//		List<CallResponsecategory> findAll = callResponseCategoryRepository.findAll();
//		if(findAll!=null   ) {
//			for (CallResponsecategory callResponsecategory : findAll) {
//				if(callResponsecategory!=null && callResponsecategory.getDeletedFlag()==0) {
//					callResponse.add(callResponsecategory);
//				}
//			}
//		}
//		return callResponse;
//	}
	@Override
	public List<CallResponsecategory> getallCallResponseCategory(Integer statusId) {
		List<CallResponsecategory> res=new ArrayList<>();
		List<CallResponsecategory> findByName=callResponseCategoryRepository.findByCategoryName(statusId);
		//System.out.println(findByName);
		if(findByName!=null   ) {
			for (CallResponsecategory callResponsecategory : findByName) {
				if(callResponsecategory!=null && callResponsecategory.getDeletedFlag()==0) {
					res.add(callResponsecategory);
				}
			}
		}
		return res;
	}
	
}
	
