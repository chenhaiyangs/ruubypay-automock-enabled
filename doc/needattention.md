# 源代码中，DefaultCondition为何会报错？

    代码使用了lombok依赖。去除了繁琐getter和setter方法。但是会在编译时自己加上getter和setter方法。
    这么做主要是因为避免代码过于繁琐。
    
    如果想抑制报错。请自行安装lombok插件。
    
# 生产环境如何避免逻辑走到mock分支

    两种方式：
    （一）和配置中心集成时，在配置中心控制台时生产环境关闭mock全局开关即可。
    （二) 由于该框架只有指定 java -Dmock=true -jar application.jar mock才生效。因此，如果你在启动时没有追加参数 -Dmock=true，即使你配置Condition类中mock的参数为打开，也是不会走mock逻辑的。
    
    只要做好多重隔离（物理隔离（卸载配置文件）运行时参数隔离：（启动时只有指定-Dmock=true，mock才生效）配置隔离（关闭全局开关）），就不会出现生产环境逻辑走到mock分支的情况
    
    