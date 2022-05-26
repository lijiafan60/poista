package com.ljf.ploughthewaves.domain.poista.service.util;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

public class OjFilter {
    public static Map<String,Integer> NameToType = new HashMap<String,Integer>() {
        {
            put("codeforces", 0);
            put("atcoder", 1);
            put("vjudge", 2);
        }
    };

    public static Map<Integer,String> TypeToName = new HashMap<Integer,String>() {
        {
            put(0,"codeforces");
            put(1,"atcoder");
            put(2,"vjudge");
        }
    };

}
