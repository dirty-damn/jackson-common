package top.senseiliu.jackson;

import java.util.Date;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import top.senseiliu.jackson.date.DateFormatExtend;
import top.senseiliu.jackson.date.DateSerializer;

/**
 * 原地址：https://github.com/zxdposter/jackson-fastjson-like
 * 对 jackson 的封装
 * 此类有两种作用
 * 1、提供了一些基础的方法
 * 2、对公用的 ObjectMapper 做统一的配置，配合 spring 自定义配置 jackson，尽量做到与 spring 框架中的 jackson 表现相同
 *
 * @author liuguanliang
 */
public abstract class Jackson {
    /**
     * 共用对象，与系统统一设置结合，在 spring 项目中，使用 Jackson2ObjectMapperBuilder.build() 生成.
     */
    protected static ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        // 反序列化时不需要在每个类上都使用注解@JsonIgnoreProperties(ignoreUnknown = true)
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 设置反序列化日期格式扩展
        OBJECT_MAPPER.setConfig(OBJECT_MAPPER.getDeserializationConfig().with(new DateFormatExtend(OBJECT_MAPPER.getDateFormat())));
        // 序列化日期格式为yyyy-MM-dd HH:mm:ss
        OBJECT_MAPPER.registerModule(new SimpleModule().addSerializer(Date.class, new DateSerializer()));
    }

    /**
     * java 对象转化成封装的 JacksonObject 对象
     *
     * @param value java 对象，不能传递 String 或其它一些基础的变量
     * @return 封装的 JacksonObject 对象
     */
    public static JacksonObject convertObject(Object value) {
        return OBJECT_MAPPER.convertValue(value, JacksonObject.class);
    }

    /**
     * java 对象转化成封装的 JacksonArray 对象
     *
     * @param value java 对象，不能传递 String 或其它一些基础的变量
     * @return 封装的 JacksonArray 对象
     */
    public static JacksonArray convertArray(Object value) {
        return OBJECT_MAPPER.convertValue(value, JacksonArray.class);
    }

    /**
     * json string 转化成封装 JacksonObject 对象
     *
     * @param text json string
     * @return 封装的 JacksonObject 对象
     */
    public static JacksonObject parseObject(String text) {
        if (text == null) {
            return new JacksonObject();
        }

        JacksonObject jacksonObject = null;
        try {
            jacksonObject = new JacksonObject((ObjectNode) OBJECT_MAPPER.readTree(text));
        } catch (Exception e) {
            throw new RuntimeException("[Jackson]JsonString转JacksonObject对象时发生异常，msg:" + e.getMessage());
        }

        return jacksonObject;
    }

    /**
     * json string 转化成封装 parseArray 对象
     *
     * @param text json string
     * @return 封装的 parseArray 对象
     */
    public static JacksonArray parseArray(String text) {
        JacksonArray jacksonArray = null;
        try {
            jacksonArray = new JacksonArray((ArrayNode) OBJECT_MAPPER.readTree(text));
        } catch (Exception e) {
            throw new RuntimeException("[Jackson]String转JacksonArray时发生异常，msg:" + e.getMessage());
        }

        return jacksonArray;
    }

    /**
     * java 对象转化.
     *
     * @param value java 对象
     * @param type  转化类型
     * @param <T>   模版
     * @return 转化后对象
     */
    public static <T> T convert(Object value, Class<T> type) {
        return OBJECT_MAPPER.convertValue(value, type);
    }

    /**
     * java 对象转化
     *
     * @param value         java 对象
     * @param typeReference 能够嵌套模版转化，比如 new TypeReference< Map< String,String>>(){}
     * @param <T>           模版
     * @return 转化后对象
     */
    public static <T> T convert(Object value, TypeReference<T> typeReference) {
        return OBJECT_MAPPER.convertValue(value, typeReference);
    }

    /**
     * java 对象转化成 json string
     *
     * @param object java 对象
     * @return json string
     */
    public static String objectToString(Object object) {
        String jsonStr = null;
        try {
            jsonStr = OBJECT_MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("[Jackson]对象转JsonString时发生异常，msg:" + e.getMessage());
        }

        return jsonStr;
    }

    /**
     * java 对象转化成 json string 带缩进
     *
     * @param object java 对象
     * @return json string pretty
     */
    public static String objectToStringPretty(Object object) {
        String jsonStr = null;
        try {
            jsonStr = OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("[Jackson]对象转JsonString时发生异常，msg:" + e.getMessage());
        }

        return jsonStr;
    }

    /**
     * java 对象转 自定义命名策略 json string
     * 使用 PropertyNamingStrategies指定属性命名策略，例如UPPER_SNAKE_CASE，注解@JsonProperty优先级任然更高
     *
     * @param object java 对象
     * @param propertyNamingStrategy 命名策略
     * @return json string
     */
    public static String objectToString(Object object, PropertyNamingStrategy propertyNamingStrategy) {
        ObjectMapper objectMapper = OBJECT_MAPPER.copy();
        objectMapper.setPropertyNamingStrategy(propertyNamingStrategy);

        String jsonStr = null;
        try {
            jsonStr = objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("[Jackson]对象转JsonString时发生异常，msg:" + e.getMessage());
        }

        return jsonStr;
    }


    /**
     * json string 转化成 java 对象
     *
     * @param text json string
     * @return java 对象
     */
    public static <T> T parseJavaObject(String text, TypeReference<T> typeReference) {
        T t = null;
        try {
            t = OBJECT_MAPPER.readValue(text, typeReference);
        } catch (Exception e) {
            throw new RuntimeException("[Jackson]String转Java对象时发生异常，msg:" + e.getMessage());
        }

        return t;
    }


    /**
     * json string 转化成 java 对象
     *
     * @param text json string
     * @return java 对象
     */
    public static <T> T parseJavaObject(String text, Class<T> type) {
        T t = null;
        try {
            t = OBJECT_MAPPER.readValue(text, type);
        } catch (Exception e) {
            throw new RuntimeException("[Jackson]String转Java对象时发生异常，msg:" + e.getMessage());
        }

        return t;
    }

    /**
     * json string 转化成 byte 数组
     *
     * @param object java 对象
     * @return byte 数组
     */
    public static byte[] objectToBytes(Object object) {
        byte[] bytes = null;
        try {
            bytes = OBJECT_MAPPER.writeValueAsBytes(object);
        } catch (Exception e) {
            throw new RuntimeException("[Jackson]对象转byte[]时发生异常，msg:" + e.getMessage());
        }

        return bytes;
    }

    /**
     * 转化成 json string
     *
     * @return json string
     */
    public String toJsonString() {
        String s = null;
        try {
            s = OBJECT_MAPPER.writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException("[Jackson]Jackson转JsonString时发生异常，msg:" + e.getMessage());
        }

        return s;
    }

    /**
     * 转化成 json 对象
     *
     * @param type 对象类型
     * @return json 对象
     */
    public <T> T toJava(Class<T> type) {
        return OBJECT_MAPPER.convertValue(this, type);
    }

    /**
     * 转化成 json 对象
     *
     * @param typeReference 对象类型
     * @return json 对象
     */
    public <T> T toJava(TypeReference<T> typeReference) {
        return OBJECT_MAPPER.convertValue(this, typeReference);
    }

    /**
     * 复写 toString，默认输出 json string
     *
     * @return json string
     */
    @Override
    public String toString() {
        try {
            return this.toJsonString();
        } catch (Exception e) {
            return null;
        }
    }
}