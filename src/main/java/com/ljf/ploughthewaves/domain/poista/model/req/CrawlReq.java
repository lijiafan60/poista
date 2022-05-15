package com.ljf.ploughthewaves.domain.poista.model.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrawlReq {
    public String openid;
    public String drawType;
    public Integer ojType;
    public String ojUsername;
}
