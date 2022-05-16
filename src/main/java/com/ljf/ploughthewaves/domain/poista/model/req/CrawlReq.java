package com.ljf.ploughthewaves.domain.poista.model.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrawlReq {
    public Integer uid;
    public Integer ojType;
    public String ojUsername;
    public Integer allSolvedNumber;
    public Integer allContestNumber;
    public Date updTime;
}
