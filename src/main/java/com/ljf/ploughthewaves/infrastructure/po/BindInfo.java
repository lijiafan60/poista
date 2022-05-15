package com.ljf.ploughthewaves.infrastructure.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BindInfo {
    public Integer ojType;
    public String ojName;
    public String ojUsername;
    public Integer solvedNumber;
}
