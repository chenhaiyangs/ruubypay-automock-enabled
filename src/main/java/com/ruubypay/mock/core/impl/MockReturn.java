package com.ruubypay.mock.core.impl;

import com.ruubypay.mock.annotation.MockEnabled;
import com.ruubypay.mock.aop.proxy.MockEnabledProxyChain;
import com.ruubypay.mock.exception.WrongReturnTypeException;
import com.ruubypay.mock.variable.MockService;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 封装返回Mock结果集的实现
 * @author chenhaiyang
 */
public class MockReturn {

    private static ConcurrentHashMap<String,MockService<?>> mockServices = new ConcurrentHashMap<>();

    private MockEnabledProxyChain proxyChain;
    private MockEnabled mockEnabled;

    public MockReturn(MockEnabledProxyChain proxyChain, MockEnabled mockEnabled) {
        this.proxyChain = proxyChain;
        this.mockEnabled = mockEnabled;
    }

    /**
     * 返回Mock结果集
     * @return 返回Mock结果集
     * @throws Exception 异常
     */
    public Object getMockResult() throws Exception {

        Class<?> clazz = mockEnabled.clazz();
        String className =clazz.getName();
        MockService<?> mockService =mockServices.get(className);
        if(mockService==null){
            mockService = (MockService) clazz.newInstance();
            mockServices.put(className,mockService);
        }
        Object result = mockService.mock(proxyChain.getArgs());
        Class<?> returnClass = proxyChain.getMethod().getReturnType();
        if(result.getClass()!=returnClass &&
                !returnClass.isAssignableFrom(result.getClass())){
            String msg = String.format("wrong return type of method. methodName: [%s] required: [%s], input: [%s]",proxyChain.getMethod().getName(),returnClass,result.getClass());
            throw new WrongReturnTypeException(msg);
        }
        return result;
    }
}
