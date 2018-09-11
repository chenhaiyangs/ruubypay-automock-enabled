package com.ruubypay.mock.variable;

import java.util.Map;

/**
 * 获取每一个接口的异常，针对异常的包装
 * @author chenhaiyang
 */
public interface ExceptionWapper<String,T extends Throwable> {

    /**
     * 获取每个方法对应的抛出异常类型
     * @return 返回一个map集合体
     */
    Map<String,T> exceptionWithMethodName();

    /**
     * 获取全局Exception
     * @return 返回全局异常（如果方法级别的异常没有生效，就返回全局异常）
     */
    Exception getGlobalException();
}
