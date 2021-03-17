package com.ivory.isasa.util;

import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Map;

public final class JsonUtil {

    /**
     * 将对象转换成json字符串
     * @param obj
     * @return
     */
    public static String object2JsonStr(Object obj) {
        return JSON.toJSONString(obj);
    }

    /**
     * 将字符串转换为对象
     * @param <T> 泛型
     */
    public static <T> T jsonStr2Object(String jsonStr, Class<T> clazz) {
        return JSON.parseObject(jsonStr, clazz);
    }

    /**
     * 将json数据转换成pojo对象list
     * <p>Title: jsonToList</p>
     * <p>Description: </p>
     * @param jsonStr
     * @param beanType
     * @return
     */
    public static <T> List<T> jsonToList(String jsonStr, Class<T> beanType) {
        return JSON.parseArray(jsonStr, beanType);
    }

    public static Map<String,Object> objectToMap(Object obj){

            return JSON.parseObject(JSON.toJSONString(obj));

    }
}