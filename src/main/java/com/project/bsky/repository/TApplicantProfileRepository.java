package com.project.bsky.repository;
import com.project.bsky.entity.TApplicantProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TApplicantProfileRepository extends JpaRepository<TApplicantProfile, Integer> {
    @Query(value = "FROM TApplicantProfile tap WHERE tap.VCHMOBILENO=:mobile")
    TApplicantProfile getTApplicantProfileByMobile(@Param("mobile") String mobile);
    
    @Query(value = "FROM TApplicantProfile tap WHERE tap.INTPROFILEID!=:profileId and tap.VCHMOBILENO=:mobile")
    TApplicantProfile getTApplicantProfileByProfileIdAndMobile(@Param("mobile") String mobile,@Param("profileId") Integer profileId);
    
}