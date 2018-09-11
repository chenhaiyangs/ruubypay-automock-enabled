package com.ruubypay.mock.core.impl;

import com.ruubypay.mock.aop.proxy.MockEnabledProxyChain;
import com.ruubypay.mock.util.ParseString;
import com.ruubypay.mock.variable.Condition;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

/**
 * 封装对条件的判断
 * @author chenhaiyang
 */
public class MockCondition {
    /**
     * mock条件
     */
    private Condition condition;

    public MockCondition(Condition condition) {
        this.condition = condition;
    }

    /**
     *  判断某个方法是否开启了Mock
     *  优先级是这样的:
     *          如果应用没有在启动时指定 java -Dmock=true 则，永远不会真正的mock
     *          全局mock开关
     *          关：所有拦截的方法都不mock
     *          开：看具体方法配置的开关
     *              未配置，采用全局配置（开）
     *              配置了，采用具体方法的配置（开或者关）
     *
     * @param proxyChain 代理类
     * @return 返回是否可以Mock
     */
    public boolean isMockEnabled(MockEnabledProxyChain proxyChain) {


        String isMock= System.getProperty("mock");
        boolean mockCondition = ParseString.parseStringToBoolean(isMock);
        if(!mockCondition){
            return false;
        }

        Objects.requireNonNull(condition.isOpen());

        if(condition.isOpen()){
            String name = proxyChain.getMethod().getName();
            Boolean isOpen = Optional.ofNullable(condition.getMethodIsOpen())
                    .orElseGet(HashMap::new)
                    .get(name);

            if(isOpen==null){
                return true;
            }
            return isOpen;
        }
        return false;
    }

    /**
     * 获取某个要拦截的方法的阻塞时间（模拟真实环境下的接口时延）
     *                      先获取方法级别配置的睡眠时间,0,则代表不睡眠。
     *                      如果方法级别的为null,取默认全局配置的睡眠时间，
     *                      如果默认配置的睡眠时间为null或者0，则代表mock时不进行阻塞
     * @param proxyChain 代理类
     * @return 返回某个方法是否配置了睡眠时间，单位 毫秒
     */
    public Long getSleepTime(MockEnabledProxyChain proxyChain){
        String name = proxyChain.getMethod().getName();
        Long methodSleepTime =
                Optional.ofNullable(condition.getMethodSleepTime())
                        .orElseGet(HashMap::new)
                        .get(name);

        if(methodSleepTime==null){
            return condition.getSleepTime();
        }
        return methodSleepTime;
    }

    /**
     * 判断某个mock方法是否是开启了抛异常逻辑（模拟正式环境第三方异常）
     *                  如果全局配置isException为false,则不抛异常(全局生效)
     *                  如果全局配置isException配置为true。则会取具体getMethodIsException()的值
     *                  每个拦截的方法的异常开关，如果为null，沿用全局配置，如果不为null，则使用getMethodIsException的配置
     *
     * @param proxyChain proxyChain 代理
     * @return 返回 结果
     */
    public Boolean isException(MockEnabledProxyChain proxyChain){

        Objects.requireNonNull(condition.isException());

        if (!condition.isException()){
            return false;
        }
        String name = proxyChain.getMethod().getName();
        Boolean isException = Optional.ofNullable(condition.getMethodIsException())
                .orElseGet(HashMap::new)
                .get(name);

        if(isException!=null){
            return isException;
        }
        return condition.isException();
    }
}
