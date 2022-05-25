package com.ljf.ploughthewaves.domain.admin.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StuInfoExcel {
    @ExcelProperty(value = {"序号"},index = 0)
    public Integer idx;
    @ExcelProperty(value = {"姓名"},index = 1)
    public String name;
    @ExcelProperty(value = {"Codeforces","用户名"},index = 2)
    public String cfName;
    @ExcelProperty(value = {"Codeforces","Rating"},index = 3)
    public Integer cfRating;
    @ExcelProperty(value = {"Codeforces","历史最高Rating"},index = 4)
    public Integer cfMaxRating;
    @ExcelProperty(value = {"Codeforces","30天内最高Rating"},index = 5)
    public Integer cfRecentMaxRating;
    @ExcelProperty(value = {"Codeforces","参赛场次"},index = 6)
    public Integer cfContestNumber;
    @ExcelProperty(value = {"Codeforces","30天内参赛场次"},index = 7)
    public Integer cfRecentContestNumber;
    @ExcelProperty(value = {"Atcoder","用户名"},index = 8)
    public String acName;
    @ExcelProperty(value = {"Atcoder","Rating"},index = 9)
    public Integer acRating;
    @ExcelProperty(value = {"Atcoder","历史最高Rating"},index = 10)
    public Integer acMaxRating;
    @ExcelProperty(value = {"Atcoder","参赛场次"},index = 11)
    public Integer acContestNumber;
    @ExcelProperty(value = {"总刷题数"},index = 12)
    public Integer allSolvedNumber;
    @ExcelProperty(value = {"PT"},index = 13)
    public Double pt;
}
