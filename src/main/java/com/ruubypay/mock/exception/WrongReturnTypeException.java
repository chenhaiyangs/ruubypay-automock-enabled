package com.ruubypay.mock.exception;

/**
 * 错误的类型异常。类型不匹配。mock返回的类型和接口本身的返回类型不匹配会抛出此异常
 * @author chenhaiyang
 */
public class WrongReturnTypeException extends Exception {

    public WrongReturnTypeException(String msg){
        super(msg);
    }
}
