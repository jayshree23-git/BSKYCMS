/**
 * 
 */
package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.Response;
import com.project.bsky.bean.SnaRemarkBean;
import com.project.bsky.model.ActionRemark;

/**
 * @author priyanka.singh
 *
 */
public interface SnaRemarkMasterService {

	Response savesnaRemark(SnaRemarkBean snaRemarkBean);

	List<ActionRemark> getAllsnaRemarks();

	ActionRemark getActionById(Long id);

	Response updatesnaremarkMaster(SnaRemarkBean snaRemarkBean);

}
