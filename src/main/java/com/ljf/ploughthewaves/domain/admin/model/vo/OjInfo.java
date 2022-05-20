package com.ljf.ploughthewaves.domain.admin.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OjInfo {
    public Integer ojType;
    public String ojName;
    public String ojUsername;
    public Integer nowRating;
    public Integer maxRating;
    public Integer recentMaxRating;
    public Integer allSolvedNumber;
    public Integer recentSolvedNumber;
    public Integer allContestNumber;
    public Integer recentContestNumber;
}
