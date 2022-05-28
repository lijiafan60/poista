package com.ljf.ploughthewaves.domain.admin.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public Date updTime;
}
