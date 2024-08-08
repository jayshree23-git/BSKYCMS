/**
 * 
 */
package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.BankMasterBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.BankMaster;

/**
 * @author priyanka.singh
 *
 */

public interface BankMasterService {

	Response saveBankMasterDetails(BankMasterBean bankMasterBean);

	List<BankMaster> getBankDetails();

	BankMaster getBankById(Integer bankId);

	Response updateBankMaster(BankMasterBean bankMasterBean);

}
