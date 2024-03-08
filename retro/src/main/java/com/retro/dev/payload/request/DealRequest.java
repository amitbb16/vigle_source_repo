package com.retro.dev.payload.request;


import lombok.Data;

import java.util.Date;

@Data
public class DealRequest {

    private Long uid;
    private String name;
    private String description;
    private Long categoryid;
    private Long serviceid;
    private Date fromDate;
    private Date todate;
    private String status;
}