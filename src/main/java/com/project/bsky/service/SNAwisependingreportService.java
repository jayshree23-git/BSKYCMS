/**
 * 
 */
package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.Cpdwiseunprocessedbean;
import com.project.bsky.bean.NonComplianceBean;

/**
 * @author rajendra.sahoo
 *
 */
public interface SNAwisependingreportService {

	List<Object> getsnawisependingreport(NonComplianceBean requestBean);

	List<Object> getsnawisependingreportdetails(NonComplianceBean requestBean);

	List<Object> getcpdwiseunprocesseddata(Cpdwiseunprocessedbean requestData);

	List<Object> getcpdwisependingreportdetails(Cpdwiseunprocessedbean requestBean);

}
