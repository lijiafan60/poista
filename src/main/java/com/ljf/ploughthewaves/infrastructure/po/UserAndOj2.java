package com.ljf.ploughthewaves.infrastructure.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAndOj2 {
    public Integer id;
    public Integer ojType;
    public Integer uid;
    public String ojUsername;
    public Integer nowRating;
    public Integer maxRating;
    public Integer recentMaxRating;
    public Integer allSolvedNumber;
    public Integer recentSolvedNumber;
    public Integer allContestNumber;
    public Integer recentContestNumber;
    public Date updTime;
}
