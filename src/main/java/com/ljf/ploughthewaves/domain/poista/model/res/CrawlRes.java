package com.ljf.ploughthewaves.domain.poista.model.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrawlRes extends CommonRes{
    public Integer uid;
    public Integer ojType;
    public Integer allSolvedNumber;
    public String ojUsername;
    public Date updTime;
}
