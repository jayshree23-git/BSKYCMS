/**
 * 
 */
package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.PaymentMode;

/**
 * @author priyanka.singh
 *
 */

@Repository
public interface GetPaymentModeRepository extends JpaRepository<PaymentMode, Long> {
	

//	@Query(value = "SELECT ID, PAYMENT_TYPE\n" +
//			"FROM PAYMENT_MODE\n", nativeQuery = true)
//	List<Object[]> getPaymentModeDetails();

	
	@Query("FROM PaymentMode order by  id desc")
	List<PaymentMode> getDetails();

//	@Query("FROM PaymentMode order by  id desc")
//	List<Object[]> getPaymentModeDetails();

	
	

}
