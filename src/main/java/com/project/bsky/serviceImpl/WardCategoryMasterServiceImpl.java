package com.project.bsky.serviceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.project.bsky.model.PackageDetailsHospital;
import com.project.bsky.model.WardCategoryMaster;
import com.project.bsky.repository.PackageDetailsHospitalRepository;
import com.project.bsky.repository.WardCategoryMasterRepository;
import com.project.bsky.service.WardCategoryMasterService;

@Service

public class WardCategoryMasterServiceImpl implements WardCategoryMasterService {
	@Autowired
	private WardCategoryMasterRepository wardCategoryMasterRepository;

	@Autowired
	private PackageDetailsHospitalRepository packageDetailsHospitalRepository;

	@Override
	public List<WardCategoryMaster> getwardCategory() {
		List<WardCategoryMaster> wardrResponse = new ArrayList<>();
		List<WardCategoryMaster> findAll = wardCategoryMasterRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

		if (findAll != null) {
			for (WardCategoryMaster wardCategoryMaster : findAll) {
				if (wardCategoryMaster != null && wardCategoryMaster.getDeletedflag() == '0') {
					wardrResponse.add(wardCategoryMaster);
				}
			}
			Set<Long> uniqueSet = new HashSet<>();
			wardrResponse = findAll.stream().filter(w -> uniqueSet.add(w.getWardlevel())).collect(Collectors.toList());
		}
		return wardrResponse;
	}

//	 @Override
//	    public List<PackageDetailsHospital> getpackageDetailsDescrition(String packageheadercode) {
//	        List<PackageDetailsHospital> headerResponse = new ArrayList<>();
//	        List<PackageDetailsHospital> findAll = packageDetailsHospitalRepository.findDetails(packageheadercode);
//	        if (findAll != null) {
//	            for (PackageDetailsHospital packageDetailsHospital : findAll) {
//	                if (packageDetailsHospital != null && packageDetailsHospital.getDeletedFlag() == 0) {
//	                    headerResponse.add(packageDetailsHospital);
//	                }
//	            }
//	        }
//	        return headerResponse;
//	    }
//

}
