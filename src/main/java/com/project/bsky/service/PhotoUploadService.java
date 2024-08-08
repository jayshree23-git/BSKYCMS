/**
 * 
 */
package com.project.bsky.service;

import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.Response;
import com.project.bsky.model.UserDetails;

/**
 * @author priyanka.singh
 *
 */
public interface PhotoUploadService {

	Response saveProfilePhto(UserDetails userDetails, MultipartFile photo);


}
