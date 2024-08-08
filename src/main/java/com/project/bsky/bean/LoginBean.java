package com.project.bsky.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import oracle.dms.util.JSONUtils;

/**
 * @Project : BSKY Backend
 * @Auther : Sambit Kumar Pradhan
 * @Created On : 20/03/2023 - 11:26 AM
 */
@Data
public class LoginBean {
    private String username;
    private String dateoftoken;
}
