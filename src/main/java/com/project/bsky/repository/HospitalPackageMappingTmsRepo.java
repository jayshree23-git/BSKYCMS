package com.project.bsky.repository;

import com.project.bsky.model.HospitalPackageMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalPackageMappingTmsRepo extends JpaRepository<HospitalPackageMapping,Long> {

}
