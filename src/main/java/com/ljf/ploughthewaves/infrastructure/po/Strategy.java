package com.ljf.ploughthewaves.infrastructure.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Strategy {
    public String id;
    public String school;
    public Double cfRating;
    public Double cfMaxRating;
    public Double cfRecentMaxRating;
    public Double cfContestNumber;
    public Double cfRecentContestNumber;
    public Double acRating;
    public Double acMaxRating;
    public Double acContestNumber;
    public Double allSolvedNumber;
}
