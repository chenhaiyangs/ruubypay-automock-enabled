# 一，开发自己的Mock实现程序

## 一，启动一个pom工程
## 二，在pom中添加如下依赖：

    <!-- 自动化mock-->
    <dependency>
        <groupId>com.github.chenhaiyangs</groupId>
        <artifactId>ruubypay-automock-enabled</artifactId>
        <version>1.1-RELEASE</version>
    </dependency>   
## 三，编写com.ruubypay.mock.variable.Condition 的实现类，用于给各项mock指标赋值。
例如以下实现类：

    public class MyMockCondition implements Condition{
    
        @Override
        public Boolean isOpen() {
            return true;
        }
    
        @Override
        public Long getSleepTime() {
            return 0L;
        }
    
    
        @Override
        public Boolean isException() {
            return false;
        }
    
    
        @Override
        public Map<String, Boolean> getMethodIsOpen() {
            Map<String,Boolean> methodisOpens = new HashMap<>();
            methodisOpens.put("function1",true);
            methodisOpens.put("function2",false);
            return methodisOpens;
        }
    
        @Override
        public Map<String, Long> getMethodSleepTime() {
            Map<String,Long> methodsleeps = new HashMap<>();
            methodsleeps.put("function1",100L);
            methodsleeps.put("function2",null);
            return methodsleeps;
        }
    
        @Override
        public Map<String, Boolean> getMethodIsException() {
            Map<String,Boolean> methodThrowExceptions = new HashMap<>();
            methodThrowExceptions.put("function1",true);
            methodThrowExceptions.put("function2",null);
            return methodThrowExceptions;
        }
    }
现在逐步解释每个实现的方法的作用。按照一组一组的函数解释
###  isOpen()和getMethodIsOpen()
    这一组参数用于设置mock的开关
    逻辑是这样的：
    isOpen是全局开关。（必须设置非null的值）
        当isOpen是false时，整个mock不生效
        当isOpen是true,全局mock开关打开
    getMethodIsOpen是方法级别的开关（可以不设置<return null>）
        当isOpen是false时，整个mock不生效，全局开关是关闭状态
        当isOpen是true时，方法级开关生效
        getMethodIsOpen 需要返回一个线程安全的Map
        key为mock框架拦截到的方法名，value是true/false/null 开关
        例如：
        methodisOpens.put("function1",true);
        表示function1的mock开关是打开着的。
        methodisOpens.put("function1",false);
        表示function1的mock开关是关闭着的。
        methodisOpens.put("function1",null);
        表示function1的mock开关沿用全局开关设置。
### getSleepTime()和getMethodSleepTime()
    
    这一组参数是用于设置mock方法的延迟时间的。
    getSleepTime是全局设置的超时时间，可以是0，大于0，或者null
    getMethodSleepTime是方法级别的超时时间设置。<可以不设置，返回null>
        getMethodSleepTime需要返回一个线程安全的Map
        key为mock框架拦截到的方法名，value是 Long/null 超时时间
        例如：
        methodsleeps.put("function1",1000);
        表示function1的mock延时是1000ms
        methodsleeps.put("function1",0);
        表示function1的mock延时是0ms,就是不配置延迟
        methodsleeps.put("function1",null);
        表示function1的mock延迟采用getSleepTime全局配置的延迟时间。
        注意，0和null是有区别的。
        
### isException()和getMethodIsException()
    
    这一组参数是用于设置mock方法的抛异常逻辑的。
    isException是全局配置（必须设置非null的值，true/false。不开异常测试逻辑就设置为false）
    当isException设置为false,getMethodIsException不会生效！
    当isException设置为true,才会进一步判断getMethodIsException的配置
    getMethodIsException是方法级别的配置<可以不设置，返回null>
        getMethodIsException需要返回一个线程安全的Map
        key为mock框架拦截到的方法名，value是 true/false/null 是否打开异常开关
        例如：
        methodThrowExceptions.put("function1",true);
        表示function1的mock打开了抛异常逻辑
        methodThrowExceptions.put("function1",false);
        表示function1的mock关闭了抛异常的逻辑
        methodThrowExceptions.put("function1",null);
        表示function1的mock异常开关沿用isException的配置。
        
## 四，编写com.ruubypay.mock.variable.ExceptionWapper 的实现类，用于当isException()和getMethodIsException()配置测试异常逻辑时，返回具体的异常类
例如以下实现类：

    public class MyException implements ExceptionWapper<String,Throwable>{
        @Override
        public Map<String,Throwable> exceptionWithMethodName() {
            Map<String,Throwable> results = new HashMap<>();
            results.put("function1",new RuntimeException("function1的mock异常！"));
            return results;
    
        }
    
        @Override
        public Exception getGlobalException() {
            return new RuntimeException("mock异常！");
        }
    }

### exceptionWithMethodName和getGlobalException    
    
    getGlobalException返回一个全局异常类。
    exceptionWithMethodName 是每一个要mock的方法对应的返回的异常类
    exceptionWithMethodName需要返回一个线程安全的Map
    key为mock框架拦截到的方法名，value是 exception/null (表示要抛出的异常)
    results.put("function1",new RuntimeException("function1的mock异常！")) 表示当打开function1的mock异常测试时，该接口会抛出指定的异常。
    results.put("function1",null) 则function1要抛出的异常沿用全局配置getGlobalException。
    
    当程序压根不需要做接口异常mock时，也不需要编写com.ruubypay.mock.variable.ExceptionWapper的实现类
    框架默认提供了一个实现类com.ruubypay.mock.variable.impl.DefaultExceptionWapper。其实就是不设置任务异常类。
 
## 五，编写com.ruubypay.mock.variable.MockService的实现类，用于实现Mock逻辑
例如：
    
    public class MessageMockImpl implements MockService<ApiReturn>{
    
    
        @Override
        public ApiReturn mock(Object[] args) {
            MessageRequest request = (MessageRequest) args[0];
            System.out.println("打印一下入参："+request);
            ApiReturn<String> ApiReturn = new ApiReturn<>();
            ApiReturn.setCode("0001");
            ApiReturn.setData(null);
            ApiReturn.setMessage("mock成功");
            return ApiReturn;
        }
    }
    
    
     在业务的注解上有一个属性clazz，就是用来配置实现了MockService的实现类的类class
     @MockEnabled(clazz=MessageMockImpl.class)
     @Override
     public ApiReturn message(MessageRequest messageRequest) {
        
        调用第三方代码......
     }
     则,当message方法需要被mock时，会找MessageMockImpl的mock函数并执行，然后返回Mock对象
     需要注意的是，Mock的返回要和第三方函数的返回对象类型保持一致。
     
     mock接口的入参是object[]数组，目的是为了适配所有入参类型。
     在mock方法里可以强转成对应的具体类，进行更加复杂的mock逻辑。比如需要入参才能构造的mock返回对象。
     
# 二，编写spring-mock.xml配置文件，并被spring容器加载
如下配置文件：

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
    
        <!-- 配置你写的mockCondition实现类-->
        <bean id="mockCondition" class="com.ruubypay.mock.variable.impl.DefaultCondition"/>
        <!-- 配置你写的 ExceptionWapper实现类 -->
        <bean id="exception" class="com.ruubypay.mock.variable.impl.DefaultExceptionWapper"/>
    
        <!-- 构造mock核心控制器-->
        <mock:handler id="mockHandler" mock-condition-ref="mockCondition" exceptions-ref="exception" />
    
        <!-- 构造aop拦截其，并将mock核心控制器传入-->
        <bean id="mockInterceptor" class="com.ruubypay.mock.aop.AspectjAopInterceptor">
            <constructor-arg ref="mockHandler" />
        </bean>
    
        <!-- aspectj织入 aop动态代理-->
        <aop:aspectj-autoproxy proxy-target-class="true"/>
        <aop:config proxy-target-class="true">
            <!-- 处理 @MockEnabeled AOP-->
            <aop:aspect ref="mockInterceptor">
                <aop:pointcut id="mockPointCut" expression="execution(* com.*.*..*.*(..)) &amp;&amp; @annotation(mockEnabled)" />
                <aop:around pointcut-ref="mockPointCut" method="proceed" />
            </aop:aspect>
        </aop:config>
    
    </beans>
     
# 三，在spring-mock.xml配置的拦截的类的要mock的每个方法上添加注解

     @MockEnabled(clazz=MessageMockImpl.class)
     @Override
     public ApiReturn message(MessageRequest messageRequest) {
        
        调用第三方代码......
     }