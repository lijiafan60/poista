package com.ljf.ploughthewaves.domain.admin.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StuInfo {
    public String name;
    public String cfName;
    public Integer cfRating;
    public Integer cfMaxRating;
    public Integer cfRecentMaxRating;
    public Integer cfContestNumber;
    public Integer cfRecentContestNumber;
    public String acName;
    public Integer acRating;
    public Integer acMaxRating;
    public Integer acContestNumber;
    public Integer allSolvedNumber;
    public Double pt;
}
