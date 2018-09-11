# automock-enabled
    
### 这是一款什么框架？
    用于在压力测试环境下压测进行时mock掉第三方接口返回的一个工具（jar包）
### 为什么要使用本工具来帮助压力测试
    在我们的接口中，经常有需要调用第三方接口才能完成的功能。
    我们压力测试自己的服务时，并不想真实的调用第三方。我们需要的是只压测自己的业务逻辑和自己的数据库资源，将第三方的接口调用mock掉，进行虚假的预期返回。
    本工具可以帮助我们轻松构建压力测试的mock。
### 相比传统的mock方式有什么优势和缺点？
    在压力测试时mock第三方。有以下三种方式：
    （1）第三方直接提供压力测试环境。我们在压测时直接配置他们的压测环境。
    优点：不侵入业务，不用自己开发mockServer。
    缺点：打开mock和关闭mock往往需要修改很多与第三方相关的配置，繁琐，且极其容易出错。且不是所有的第三方都提供mock环境
    （2）修改第三方依赖lib库,开发mock jar包代替正常jar包
    优点：不侵入业务，不用自己开发mockServer。
    缺点：正式环境和压测环境jar包需要来回替换，繁琐且容易出错。
    （3）自己开发mockServer。
    优点：不侵入业务，只需更改配置。
    缺点：自己开发，额外开发量。
         有可能MockServer相较于业务服务，率先成为了瓶颈。（MockServer部署的服务器的各种原因，极有可能导致性能不佳）
         自己开发的MockServer由于本身服务器环境等问题，很难模拟出真实下第三方的情况。这个时候的mock的结果也不准确
         假设我们服务线上指标10000并发，业务服务需要部署10台机器，那么，我们的mockServer也可能需要部署5-10台才能撑得住并发。
         我们有更多的精力部署10份儿mock服务吗？毕竟服务器资源有限？而真实的业务场景下，第三方接口的性能一般不会率先成为瓶颈。
         笔者在真实的开发实践中没有遇到过第三方撑不住自己的流量的情形。
         
    automock-enabled相较于上面的压测时mock方式优缺点：
    优点：不侵入业务。只需更改配置
         有扩展功能：模拟第三方抛异常，模拟第三方时延等功能case。
         模拟第三方时延：例如，第三方A接口在真实测试时一般都是100ms返回。
         那么,我们mock这个A接口，就可以配置延迟策略，比如配置100ms延迟，这样，就能尽可能真实的模拟出第三方接口的吞吐量和响应。
         模拟第三方抛异常：此功能可以模拟第三方抛出异常的情形。用于测试第三方异常情况下的业务接口的性能。
         配合当当网的config-toolkit可以实现在线一键热切（mock/unmock）
    缺点：线上代码中有一个永远不会走进去的分支:真实线上环境，是永远不会跑mock分支的。虽然此种方式不侵入业务，但线上代码有一个永远不会走的分支逻辑有时不太可靠。
         此框架不侵入业务，但也需要寄生在Spring之上。这就需要将业务方的代码进行调整：
         将调用第三方的代码单独包成一个个方法，并受spring容器管理，automock-enabled是基于Spring的AOP原理实现的。
         值得高兴的是：即使没有automock框架， 将调用第三方的代码单独包成一个个方法，并受spring容器管理也是一个代码优化的方式。（面向接口编程）
         借用一次次功能需求迭代过程中，顺水推舟的将调用第三方的代码单独包成一个个方法，并受spring容器管理。
         这样，就可以使用本框架了。在基于代码的改动中，也并未有业务逻辑的改动，因此，此框架可以算做是不侵入业务。

### 接入方式以及如何做到不侵入业务
    
    （一）编写单独的mock实现程序。
         例如：你需要开发一个推送服务的mock实现。
    
        在pom种添加依赖：      
        <!-- 自动化mock-->
        <dependency>
            <groupId>com.github.chenhaiyangs</groupId>
            <artifactId>ruubypay-automock-enabled</artifactId>
            <version>1.1-RELEASE</version>
        </dependency>    
        然后依赖ruubypay-automock-enabled的API完成自己的mock实现开发。
    
        在主业务代码程序中，可能只需要在pom中添加你的mock实现依赖，例如：
        你依赖ruubypay-automock-enabled开发完了你的推送服务的Mock实现。
        
        在业务程序中添加pom。下面只是举个例子：
         <!-- 用户中心自动化mock实现-->
        <dependency>
            <groupId>com.ruubypay.userservice</groupId>     
            <artifactId>ruubypay-userservice-mock</artifactId> 
            <version>1.0-RELEASE</version>
        </dependency>  
    
        整个依赖过程为：
        业务程序pom.xml -> 依赖你的mock实现程序
        你的mock实现程序 -> 依赖ruubypay-automock-enabled
    
    （二）在一线业务程序中添加spring配置文件如spring-mock.xml。进行简单配置，
        如：
    
        <?xml version="1.0" encoding="UTF-8"?>
        <!--suppress ALL -->
        <beans xmlns="http://www.springframework.org/schema/beans"
               xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
               xmlns:aop="http://www.springframework.org/schema/aop"
               xmlns:mock="http://www.ruubypay.com/schema/automock"
               xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
                                   http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd
                                   http://www.ruubypay.com/schema/automock http://www.ruubypay.com/schema/automock/config.xsd
                                   ">
        
        
            <bean id="mockCondition" class="com.ruubypay.mock.variable.impl.DefaultCondition"/>
            <bean id="exception" class="com.ruubypay.mock.variable.impl.DefaultExceptionWapper"/>
        
            <mock:handler id="mockHandler" mock-condition-ref="mockCondition" exceptions-ref="exception" />
        
            <bean id="mockInterceptor" class="com.ruubypay.mock.aop.AspectjAopInterceptor">
                <constructor-arg ref="mockHandler" />
            </bean>
        
            <!-- aspectj织入 配置-->
            <aop:aspectj-autoproxy proxy-target-class="true"/>
            <aop:config proxy-target-class="true">
                <!-- 处理 @MockEnabeled AOP-->
                <aop:aspect ref="mockInterceptor">
                    <aop:pointcut id="mockPointCut" expression="execution(* com.*.*..*.*(..)) &amp;&amp; @annotation(mockEnabled)" />
                    <aop:around pointcut-ref="mockPointCut" method="proceed" />
                </aop:aspect>
            </aop:config>
        
        </beans>
    
    （三）在需要mock的第三方的函数上添加mock注解，实现自动化mock，前提要求：函数所在的类受spring管理
        
         例如：
         @MockEnabled(clazz=MessageMockImpl.class)
         @Override
         public ApiReturn message(ApiRequest apiRequest) {
            
            调用第三方代码......
         }
    
    
    
     （四）在程序启动时必须添加参数指定当前环境为mock环境 java -Dmock=true -jar application.jar
     
     
     以上几个步骤，就可以实现自动化mock
     
## [具体使用方法详解](./doc/detail.md)
## [内置了一个和配置中心集成的condition实现](doc/config-cloud.md)
## [注意事项](./doc/needattention.md)