package com.ruubypay.mock.support.spring.parser;

import com.ruubypay.mock.core.MockHandler;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * 处理handler标签的spring语义解析器
 * @author chenhaiyang
 */
public class MockHandlerParser extends AbstractSingleBeanDefinitionParser {

    /**
     * mock条件引用
     */
    private static final String MOCK_CONDITION_REF="mock-condition-ref";
    /**
     * 异常类的引用
     */
    private static final String EXCEPTION_REF="exceptions-ref";

    @Override
    protected Class<?> getBeanClass(Element element) {
        return MockHandler.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        String mockConditions = element.getAttribute(MOCK_CONDITION_REF);
        builder.addConstructorArgReference(mockConditions);
        String exceptions = element.getAttribute(EXCEPTION_REF);
        builder.addConstructorArgReference(exceptions);
    }
}
