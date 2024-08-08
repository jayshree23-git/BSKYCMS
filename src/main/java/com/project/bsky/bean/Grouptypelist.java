package com.project.bsky.bean;

import lombok.Data;

@Data
public class Grouptypelist {
    public Long typrid;
    public String grouptypename;
    public Long groupid;
    
    
    ///details list
    public Long user_manual_id;
    public Long user_type_id;
    public String user_type_name;
    public Long primary_link_id;
    public String primary_link_name;
    public String user_manual_document;
    public String remarks;
    public String full_name;
    public String created_on;
    public String updated_on;
}
