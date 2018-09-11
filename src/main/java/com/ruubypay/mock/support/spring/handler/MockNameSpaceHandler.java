package com.ruubypay.mock.support.spring.handler;

import com.ruubypay.mock.support.spring.parser.MockHandlerParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * mockHander的Spring命名空间处理器
 * @author chenhaiyang
 */
public class MockNameSpaceHandler extends NamespaceHandlerSupport {

    /**
     * spring配置文件的标签
     */
    private static final String TAG="handler";
    @Override
    public void init() {
        registerBeanDefinitionParser(TAG, new MockHandlerParser());
    }
}
