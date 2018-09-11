package com.ruubypay.mock.variable;

import java.util.Map;

/**
 * Mock的一些条件指标,例如，开关，延迟，抛异常开关等
 * @author chenhaiyang
 */
public interface Condition {

    /**
     * 必配
     * 全局mock开关，只有mock开关在打开状态，才进行Mock
     * @return 返回 mock开关(全局设定，如果isOpen是true，则每个接口的mock状态使用getMethodIsOpen获取，没配置采用全局配置。如果isOpen是false，getMethodIsOpen配置不生效)
     */
    Boolean isOpen();

    /**
     * 具体到要拦截的每一个方法的Mock开关，需要按照指定的格式
     * @return 返回每一个方法的mock开关。例如:getAccessToken(true),getAliPay(false),,多个方法用逗号间隔
     */
    Map<String,Boolean> getMethodIsOpen();

    /**
     * 睡眠时间。为了尽可能的模拟真实环境的接口产生的延迟，这里可以配置延迟，单位ms
     * @return 返回延迟时间，如果设置为0，则默认不延迟。
     */
    Long getSleepTime();

    /**
     * 具体到要拦截的每一个方法的睡眠时间，如果设置了，则方法级别生效
     * @return 返回每一个方法的mock开关。例如:getAccessToken(true),getAliPay(false),,多个方法用逗号间隔,单位ms
     * 如果配置了0,则默认不延迟。如果不配置属性，则使用sleepTime的配置。
     */
    Map<String,Long> getMethodSleepTime();

    /**
     * 是否是测试异常状态，如果是true,则mock接口会抛出getException获取的异常
     * 必传，如果不测试异常时逻辑，直接配置为false;
     * @return 是否在测试异常状态，全局配置，如果是false,则getMethodIsException不生效。
     * 如果是true，并且配置了方法级别的异常测试，则会使用getMethodIsException的配置，如无，则使用isException的配置
     */
    Boolean isException();

    /**
     * 具体到要拦截的具体方法,是否是开异常模式。如果是，则开启抛异常模式。否则，该方法为关闭异常
     * @return 返回每一个方法的异常开关，例如:getAccessToken(true),getAliPay(false),多个方法用逗号间隔
     */
    Map<String,Boolean> getMethodIsException();
}