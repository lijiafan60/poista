package com.ljf.ploughthewaves.infrastructure.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAndOj1 {
    public Integer uid;
    public String name;
    public Integer ojType;
    public String ojUsername;
    public Integer allSolvedNumber;
    public Date updTime;
}
