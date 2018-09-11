package com.ruubypay.mock.variable.impl;

import com.ruubypay.mock.variable.ExceptionWapper;

import java.util.Map;

/**
 * 默认的ExceptionWapper实现，什么也不返回，如果不测试异常，可以默认使用此类
 * @author chenhaiyang
 */
public class DefaultExceptionWapper implements ExceptionWapper {
    @Override
    public Map exceptionWithMethodName() {
        return null;
    }

    @Override
    public Exception getGlobalException() {
        return null;
    }
}
