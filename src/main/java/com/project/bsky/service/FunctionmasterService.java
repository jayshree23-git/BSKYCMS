/**
 * 
 */
package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.Response;
import com.project.bsky.model.FunctionMaster;
import com.project.bsky.model.UnlinkedFunctionMaster;

/**
 * @author rajendra.sahoo
 *
 */
public interface FunctionmasterService {

	Response savefunctionmaster(FunctionMaster functionmaster);

	List<FunctionMaster> getfunctionmaster();

	Response deletefunctionmaster(Long userid, Long fnid);

	FunctionMaster getbyfunctionmaster(Long userid);

	Response update(FunctionMaster functionmaster);

	Response saveunlinkedfunctionmaster(UnlinkedFunctionMaster unlinkedFunctionMaster);

	List<UnlinkedFunctionMaster> getUnlinkedFunctionMaster();

	Response removeUnlinkedFunctionMaster(Long userid, Long fnid);

	UnlinkedFunctionMaster getUnlinkedFunctionById(Long functionId);

	Response updateUnlinkedFunctionMaster(UnlinkedFunctionMaster unlinkedFunctionMaster);

}
