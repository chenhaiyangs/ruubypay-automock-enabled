package com.ruubypay.mock.exception;

/**
 * Mock异常找不到时抛出此异常
 *          当某个拦截的方法，打开了mock异常测试。需要返回其对应的异常类时
 *          却找不到该方法对应的mock的Exception。则抛出此异常
 *          此时需要检查以下配置：isException,getMethodIsException,exceptionWithMethodName,getGlobalException
 * @author chenhaiyang
 */
public class NoMockExceptionFoundException extends Exception{

    public NoMockExceptionFoundException(String msg) {
        super(msg);
    }
}
