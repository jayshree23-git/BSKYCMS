package com.project.bsky.repository;

import com.project.bsky.model.ReferralVitalParameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferralVitalParametersRepository extends JpaRepository<ReferralVitalParameters, Long> {
}
