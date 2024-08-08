package com.project.bsky.repository;

import com.project.bsky.model.MstResponseMessage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Project : BSKY Backend
 * @Author : Sambit Kumar Pradhan
 * @Created On : 21/07/2023 - 2:57 PM
 */
public interface MstResponseMessageRepository extends JpaRepository<MstResponseMessage, Integer> {
}
