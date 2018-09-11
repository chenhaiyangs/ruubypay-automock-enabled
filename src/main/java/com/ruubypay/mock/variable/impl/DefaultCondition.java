package com.ruubypay.mock.variable.impl;

import com.ruubypay.mock.util.ParseString;
import com.ruubypay.mock.variable.Condition;
import lombok.Data;

import java.util.Map;

/**
 * 默认的condition，可以和当当网的config-toolkit配合一用，一起实现热更新
 * @author chenhaiyang
 */
@Data
public class DefaultCondition implements Condition{

    /**
     * mock的全局开关
     */
    private String open;

    /**
     * mock的全局睡眠时间
     */
    private String sleepTime;

    /**
     * mock的全局抛异常开关
     */
    private String exception;

    /**
     * 具体到每一个要mock的方法的开关，是一个json key为方法名，value为true|false
     */
    private String opens;

    /**
     * 具体到每一个要mock的方法的睡眠时间，是一个json key为方法名，value为毫秒值
     */
    private String sleepTimes;

    /**
     * 具体到每一个是否要抛出异常的配置开关，是一个json key为方法名，value为true|false
     */
    private String exceptions;

    @Override
    public Boolean isOpen() {
        return ParseString.parseStringToBoolean(open);
    }

    @Override
    public Long getSleepTime() {
        return ParseString.parseStringToLang(sleepTime);
    }


    @Override
    public Boolean isException() {
        return ParseString.parseStringToBoolean(exception);
    }

    @Override
    public Map<String, Boolean> getMethodIsOpen() {
        return ParseString.parseJsonAsBooleanMap(opens);
    }

    @Override
    public Map<String, Long> getMethodSleepTime() {
        return ParseString.parseJsonAsLongMap(sleepTimes);
    }

    @Override
    public Map<String, Boolean> getMethodIsException() {
        return ParseString.parseJsonAsBooleanMap(exceptions);
    }
}
