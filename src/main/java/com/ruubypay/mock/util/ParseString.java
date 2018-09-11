package com.ruubypay.mock.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.Map;

/**
 * 内置一些支持类，针对原生值类型进行转换
 * zookeeper上的配置节点全部为字符串，mock中需要的配置为boolean或者long
 * @author chenhaiyang
 */
public class ParseString{

    /**
     * 将字符串转换为数字
     * @return 返回布尔类型
     */
    public static Boolean parseStringToBoolean(String booleanStr){
        return booleanStr!=null
                && !"".equals(booleanStr.trim())
                && Boolean.parseBoolean(booleanStr.trim());
    }

    /**
     * 将字符串转换为Long类型
     * @param longStr lang  字符串
     * @return 返回Long类型(如果不能解析,就返回null)
     */
    public static Long parseStringToLang(String longStr){

        if(longStr==null||"".equals(longStr.trim())){
            return null;
        }
        try{
            return Long.parseLong(longStr.trim());
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 将json转为map<String,Boolean>
     * @param json json串
     * @return 返回转换结果
     */
    public static Map<String,Boolean> parseJsonAsBooleanMap(String json){
        return JSON.parseObject(json,new TypeReference<Map<String,Boolean>>(){});
    }

    /**
     * 将json转为map<String,Long>
     * @param json json串
     * @return 返回转换结果
     */
    public static Map<String,Long> parseJsonAsLongMap(String json){
        return JSON.parseObject(json,new TypeReference<Map<String,Long>>(){});
    }
}
