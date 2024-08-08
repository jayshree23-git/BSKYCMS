/**
 * 
 */
package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.Response;
import com.project.bsky.model.GlobalLink;

/**
 * @author rajendra.sahoo
 *
 */
public interface Globallinkservice {

	Response savegloballink(GlobalLink globallink);

	List<GlobalLink> getgloballink();

	Response deletefunctionmaster(Long userid);

	GlobalLink getbyId(Long userid);

	Response updategloballink(GlobalLink globallink);

}
