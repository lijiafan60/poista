package com.ljf.ploughthewaves.infrastructure.util.ratelimit;

public enum LimitType {
    /**
     * 默认方式,滑动窗口限流
     */
    DEFAULT,
    /**
     * 根据ip限流
     */
    IP
}
