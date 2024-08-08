package com.project.bsky.repository;

import com.project.bsky.model.Referral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferralRepository extends JpaRepository<Referral, Long> {

    Referral findByMemberIdAndIsActiveAndDeletedFlag(Long memberId, int i, int i1);

}
