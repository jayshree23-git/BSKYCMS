package com.project.bsky.response;


import java.util.List;

import lombok.Data;

@Data
public class Root{
    public String status;
    public List<Result> result;
}
