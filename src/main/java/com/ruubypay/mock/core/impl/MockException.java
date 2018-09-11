package com.ruubypay.mock.core.impl;

import com.ruubypay.mock.aop.proxy.MockEnabledProxyChain;
import com.ruubypay.mock.exception.NoMockExceptionFoundException;
import com.ruubypay.mock.variable.ExceptionWapper;

import java.util.HashMap;
import java.util.Optional;

/**
 * MockException 模拟mock异常的类
 * @author chenhaiyang
 */
public class MockException {

    /**
     * 获取接口运行时的mock异常
     */
    private ExceptionWapper exceptionWapper;

    public MockException(ExceptionWapper exceptionWapper) {
        this.exceptionWapper = exceptionWapper;
    }

    /**
     * 获取要拦截的方法对应的异常时mock要抛出的Exception类
     * @param proxyChain 代理类
     * @return 返回具体方法要抛的异常。
     */
    public Throwable getMockException(MockEnabledProxyChain proxyChain) throws NoMockExceptionFoundException {

        String name = proxyChain.getMethod().getName();
        Throwable methodException = (Throwable) Optional.ofNullable(exceptionWapper.exceptionWithMethodName())
                .orElseGet(HashMap::new)
                .get(name);

        methodException = Optional.ofNullable(methodException).orElse(exceptionWapper.getGlobalException());
        if(methodException==null){
            String msg =String.format(" methodName:[%s] both globalException and methodException is null. one is required! ",name);
            throw new NoMockExceptionFoundException(msg);
        }
        return methodException;
    }
}
