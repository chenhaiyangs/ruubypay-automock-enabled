package com.ruubypay.mock.aop;

import com.ruubypay.mock.annotation.MockEnabled;
import com.ruubypay.mock.aop.proxy.aspectj.AspectjMockEnabledProxyChain;
import com.ruubypay.mock.core.MockHandler;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 使用aspectj 实现AOP拦截
 * 注意，要拦截的类的要拦截的方法不能有重名方法
 * @author chenhaiyang
 */
public class AspectjAopInterceptor {

    /**
     * 处理Mock逻辑的核心实现
     */
    private final MockHandler mockHandler;

    public AspectjAopInterceptor(MockHandler mockHandler) {
        this.mockHandler = mockHandler;
    }

    /**
     * 处理方法
     * @param aopProxyChain 切点
     * @param mockEnabled 拦截到的注解
     * @return 返回执行结果
     * @throws Throwable 抛出异常
     */
    public Object proceed(ProceedingJoinPoint aopProxyChain, MockEnabled mockEnabled) throws Throwable {
        return mockHandler.proceed(new AspectjMockEnabledProxyChain(aopProxyChain),mockEnabled);
    }

}
