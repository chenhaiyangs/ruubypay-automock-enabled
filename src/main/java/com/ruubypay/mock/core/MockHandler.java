package com.ruubypay.mock.core;

import com.ruubypay.mock.annotation.MockEnabled;
import com.ruubypay.mock.aop.proxy.MockEnabledProxyChain;
import com.ruubypay.mock.core.impl.MockCondition;
import com.ruubypay.mock.core.impl.MockException;
import com.ruubypay.mock.core.impl.MockReturn;
import com.ruubypay.mock.variable.Condition;
import com.ruubypay.mock.variable.ExceptionWapper;

/**
 * 处理Mock的核心类
 * @author chenhaiyang
 */
public class MockHandler {

    /**
     * mock条件逻辑封装类
     */
    private MockCondition mockCondition;
    /**
     * mock异常类
     */
    private MockException mockException;

    /**
     * mock异常类
     * @param condition 条件
     * @param exceptionWapper 异常类
     */
    public MockHandler(Condition condition,ExceptionWapper exceptionWapper){
        this.mockCondition= new MockCondition(condition);
        this.mockException= new MockException(exceptionWapper);
    }


    /**
     * 处理Mock的核心类
     * @param proxyChain proxyChain
     * @param mockEnabled 注解
     * @return 返回程序执行结果
     * @throws Throwable 抛出异常
     */
    public Object proceed(MockEnabledProxyChain proxyChain, MockEnabled mockEnabled) throws Throwable {

        if(mockCondition.isMockEnabled(proxyChain)){

            //模拟接口阻塞逻辑
            Long sleepTime = mockCondition.getSleepTime(proxyChain);
            if(sleepTime!=null&&sleepTime>0L){
                Thread.sleep(sleepTime);
            }
            //模拟接口异常逻辑
            Boolean isTestException =mockCondition.isException(proxyChain);
            if(isTestException){
                Throwable exception = mockException.getMockException(proxyChain);
                if(exception!=null){
                    throw exception;
                }
            }
            return new MockReturn(proxyChain,mockEnabled).getMockResult();
        }
        return proxyChain.doProxyChain(proxyChain.getArgs());
    }
}
