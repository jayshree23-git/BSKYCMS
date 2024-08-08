package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.Response;
import com.project.bsky.bean.UnbundlingSubmitBean;

public interface UnbundlingPackageMasterService {

	List<Object> getpackageidandpackagename();

	Response getsubmitunbundlingpackage(UnbundlingSubmitBean resbean);

}
