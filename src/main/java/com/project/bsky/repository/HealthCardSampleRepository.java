package com.project.bsky.repository;

import com.project.bsky.model.HealthCardSample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HealthCardSampleRepository extends JpaRepository<HealthCardSample,Long> {

    List<HealthCardSample> findByUrn(String urn);

    List<HealthCardSample> findByAadharNumber(String aadharnumber);

    HealthCardSample findByFullNameEnglish(String name);

}
