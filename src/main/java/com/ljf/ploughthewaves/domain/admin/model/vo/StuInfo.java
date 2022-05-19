package com.ljf.ploughthewaves.domain.admin.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StuInfo {
    public String name;
    public String ojName;
    public String ojUsername;
    public Integer solvedNumber;
}
