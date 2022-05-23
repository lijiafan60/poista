package com.ljf.ploughthewaves.domain.poista.model.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrawlRes {
    public Integer uid;
    public Integer ojType;
    public Integer allSolvedNumber;
    public String ojUsername;
    public Integer nowRating;
    public Integer maxRating;
    public Integer recentMaxRating;
    public Integer recentSolvedNumber;
    public Integer allContestNumber;
    public Integer recentContestNumber;
    public Date updTime;
}
