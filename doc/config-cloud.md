# 框架已经内置了一个和config-toolkit热配置集成的Condition实现类

    框架已经内置了一个和config-toolkit热配置集成的Confition实现类
    全类路径为：
    com.ruubypay.mock.variable.impl.DefaultCondition
    
    因此，如果想使automock-enabled和config-toolkit一键集成。
    直接使用整个默认的实现即可。原理很简单。不妨贴出源代码：
    
        /**
         * mock的全局开关
         */
        private String open;
    
        /**
         * mock的全局睡眠时间
         */
        private String sleepTime;
    
        /**
         * mock的全局抛异常开关
         */
        private String exception;
    
        /**
         * 具体到每一个要mock的方法的开关，是一个json key为方法名，value为true|false
         */
        private String opens;
    
        /**
         * 具体到每一个要mock的方法的睡眠时间，是一个json key为方法名，value为毫秒值
         */
        private String sleepTimes;
    
        /**
         * 具体到每一个是否要抛出异常的配置开关，是一个json key为方法名，value为true|false
         */
        private String exceptions;
    
        @Override
        public Boolean isOpen() {
            return ParseString.parseStringToBoolean(open);
        }
    
        @Override
        public Long getSleepTime() {
            return ParseString.parseStringToLang(sleepTime);
        }
    
    
        @Override
        public Boolean isException() {
            return ParseString.parseStringToBoolean(exception);
        }
    
        @Override
        public Map<String, Boolean> getMethodIsOpen() {
            return ParseString.parseJsonAsBooleanMap(opens);
        }
    
        @Override
        public Map<String, Long> getMethodSleepTime() {
            return ParseString.parseJsonAsLongMap(sleepTimes);
        }
    
        @Override
        public Map<String, Boolean> getMethodIsException() {
            return ParseString.parseJsonAsBooleanMap(exceptions);
        }
        
    所有的配置都为String类型，在实现的函数中转为了Boolean,Long和map。原来的map参数在配置中心只需要配置成json形式的字符串即可。
    
    配置中心只需要有一个组的参数配置为该javaBean的属性名，即可。map形式的参数配置为json字符串。
    然后com.ruubypay.mock.variable.impl.DefaultCondition和config-toolkit的一个组绑定为热配置，就能实现mock在线热切。
    
    详见config-toolkit的使用。



    
    
    