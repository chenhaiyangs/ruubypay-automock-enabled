package com.ruubypay.mock.variable;

/**
 * MockService,mock的具体实现需要实现这个方法
 * @param <R> 出参
 * @author chenhaiyang
 */
public interface MockService<R>{
    /**
     * mock的实现
     * @param args 入参
     * @return 出参
     */
     R mock(Object[] args);
}
