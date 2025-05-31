package org.example.lottery_system.common.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.json.JsonParseException;
import org.springframework.util.ReflectionUtils;

import java.util.List;
import java.util.RandomAccess;
import java.util.concurrent.Callable;

/**
 * 模仿框架中的Jackson实现序列化和反序列化工具
 * 无论是反序列化还是序列化都会抛出异常，此处模仿后端Jackson源码实现一个"通用异常包装器"
 * 程序员只需关注方法怎么实现，抛出的异常被统一捕获
 */
public class JacksonUtil {
    // 单例一个objectmapper
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    // 通用异常包装器(进一步封装)
    private static <T> T tryParse(Callable<T> parser) {
        return JacksonUtil.tryParse(parser, JacksonException.class);
    }

    // 通用异常包装器
    private static <T> T tryParse(Callable<T> parser, Class<? extends Exception> check) {
        try {
            return parser.call();
        } catch (Exception var4) {
            if (check.isAssignableFrom(var4.getClass())) {
                throw new JsonParseException(var4);
            }
            throw new IllegalStateException(var4);
        }
    }

    /**
     * 序列化对象
     * @param object
     * @return
     */
    public static String writeValueAsString(Object object) {
        return tryParse(()->{
            return JacksonUtil.getObjectMapper().writeValueAsString(object);
        });
    }

    /**
     * 反序列化对象
     * @param json
     * @param valueType
     * @return
     * @param <T>
     */
    public static <T> T readValue(String json, Class<T> valueType) {
      return tryParse(()->{
        return JacksonUtil.getObjectMapper().readValue(json, valueType);
      });
    };

    /**
     * 反序列化list
     * @param json
     * @param paraType
     * @return
     * @param <T>
     */
    public static <T> T readListValue(String json, Class<?> paraType) {
        JavaType javaType = objectMapper.getTypeFactory()
                .constructParametricType(List.class, paraType);
        return tryParse(()->{
            return JacksonUtil.getObjectMapper().readValue(json, javaType);
        });
    };
}
