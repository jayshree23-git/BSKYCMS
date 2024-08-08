/**
 * 
 */
package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.ReferralHospital;

/**
 * @author rajendra.sahoo
 *
 */
@Repository
public interface ReferralHospitalRepository extends JpaRepository<ReferralHospital, Long> {

	@Query(value = "select h.HOSPITALID,h.HOSPITALNAME,\r\n"
			+ "        'ODISHA',d.DISTRICTNAME,\r\n"
			+ "        b.BLOCKNAME,ht.HOSPITALTYPENAME,\r\n"
			+ "        h.CONTACTPERSONNAME,h.MOBILENO,\r\n"
			+ "        h.ADDRESS,h.STATUSFLAG\r\n"
			+ "from HOSPITAL_INFO_REFERRAL h\r\n"
			+ "left join district d on d.DISTRICTCODE=h.DISTCODE and d.STATECODE=h.STATECODE\r\n"
			+ "left join block b on b.BLOCKCODE=h.BLOCKCODE and b.DISTRICTCODE=h.DISTCODE and b.STATECODE=h.STATECODE\r\n"
			+ "left join hospital_type_master ht on ht.HOSPITALTYPEID=h.HOSPITALTYPE\r\n"
			+ "order by h.HOSPITALID desc",nativeQuery = true)
	List<Object[]> getreferalhospitallist();

	@Query("from ReferralHospital where status=0 and statecode=21 and distcode=:distid")
	List<ReferralHospital> getbydistid(String distid);

	@Query("from ReferralHospital where status=0 and statecode=21 and distcode=:distid and blockcode=:block")
	List<ReferralHospital> getrefHospitalbyDistrictIdblockid(String distid, String block);

	@Query("from ReferralHospital where status=0 and statecode=21 and distcode=:distid and hospitaltype=:hosptypeid")
	List<ReferralHospital> getrefHospitalbyDistrictIdhospitaltype(String distid, Integer hosptypeid);

}
