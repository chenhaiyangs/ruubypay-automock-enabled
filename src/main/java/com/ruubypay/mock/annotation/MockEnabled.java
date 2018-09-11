package com.ruubypay.mock.annotation;

import java.lang.annotation.*;

/**
 * 针对需要Mock的方法的注解。方法级别生效
 * @author chenhaiyang
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface MockEnabled {


    /**
     * 获取接口返回对象的一个类,指定该类，从该类中获取mock返回对象
     * @return 返回对象
     */
    Class clazz();
}
