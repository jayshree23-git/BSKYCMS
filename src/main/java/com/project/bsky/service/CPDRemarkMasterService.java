/**
 * 
 */
package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.CPDRemarkBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.ActionRemark;

/**
 * @author priyanka.singh
 *
 */
public interface CPDRemarkMasterService {

	List<ActionRemark> getAllRemarks();

	Response saveCpdRemark(CPDRemarkBean cpdRemarkBean);

	ActionRemark getActionById(Long id);

	Response updateBankMaster(CPDRemarkBean cpdRemarkBean);

}
