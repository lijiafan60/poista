package com.ljf.ploughthewaves.infrastructure.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagsStatistics {
    Integer id;
    Integer uid;
    Integer math;
    Integer greedy;
    Integer graphs;
    Integer dataStructure;
    Integer bruteforce;
    Integer dp;
    String cfProblem;
    Date updTime;
}
