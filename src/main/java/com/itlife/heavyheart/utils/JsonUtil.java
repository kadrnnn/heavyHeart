package com.itlife.heavyheart.utils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author kex
 * @Create 2020/9/1 9:55
 * @Description
 */
public class JsonUtil {
    static Logger log = LoggerFactory.getLogger(JsonUtil.class);
    static ObjectMapper objectMapper = new ObjectMapper();

    public static JsonNode string2JsonNode(String json) throws JsonProcessingException {
        return objectMapper.readTree(json);
    }

    public static JsonNode object2JsonNode(Object json, DateFormat dateFormat) throws JsonProcessingException {
        return objectMapper.readTree(writeValueAsString(json, dateFormat));
    }

    /**
     * 对象序列化成JSON字符串
     *
     * @param value      对象
     * @param dateFormat 日期格式
     * @return
     */
    public static String writeValueAsString(Object value, DateFormat dateFormat) {
        if (dateFormat != null) {
            objectMapper.setDateFormat(dateFormat);
        } else {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            objectMapper.setDateFormat(dateFormat);
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonGenerationException e) {
            log.error("对象序列化成JSON字符串异常", e);
        } catch (JsonMappingException e) {
            log.error("对象序列化成JSON字符串异常", e);
        } catch (IOException e) {
            log.error("对象序列化成JSON字符串异常", e);
        }
        return null;
    }

    /**
     * JSON反序列化成实体对象、实体对象集合
     *
     * @param dataJson   JSON数据字符串
     * @param classType  实体类
     * @param dateFormat 日期格式
     * @return
     */
    public static <T> T readValue(String dataJson, Class classType, DateFormat dateFormat) {
        if (dateFormat != null) {
            objectMapper.setDateFormat(dateFormat);
        } else {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            objectMapper.setDateFormat(dateFormat);
        }

        //取出实体类中的属性并拼接属性名称
        Field[] fields = classType.getDeclaredFields();
        StringBuffer stringBuffer = new StringBuffer();
        for (Field f : fields) {
            stringBuffer.append(f.getName()).append(",");
        }
        try {
            //排除不属于指定实体类的属性
            //JsonParser jsonParser = new JsonParser();
            JsonElement element = JsonParser.parseString(dataJson);
            if (dataJson.startsWith("[")) {
                //实体集合
                JsonArray jsonArray = element.getAsJsonArray();
                for (JsonElement object : jsonArray) {
                    Iterator iterator = object.getAsJsonObject().entrySet().iterator();
                    for (Iterator it = iterator; iterator.hasNext(); ) {
                        Map.Entry<String, JsonElement> entry = (Map.Entry<String, JsonElement>) it.next();
                        if (stringBuffer.indexOf(entry.getKey() + ",") == -1) {
                            it.remove();
                        }
                    }
                }
                return objectMapper.readValue(jsonArray.toString(), TypeFactory.defaultInstance().constructType(ArrayList.class, classType));
            } else {
                //单个实体
                JsonObject jsonObject = element.getAsJsonObject();
                Iterator iterator = jsonObject.entrySet().iterator();
                for (Iterator it = iterator; it.hasNext(); ) {
                    Map.Entry<String, JsonElement> entry = (Map.Entry<String, JsonElement>) it.next();
                    if (stringBuffer.indexOf(entry.getKey() + ",") == -1) {
                        it.remove();
                    }
                }
                return (T) objectMapper.readValue(jsonObject.toString(), classType);
            }
        } catch (IOException e) {
            log.error("JSON反序列化成实体对象、实体对象集合异常", e);
        }
        return null;
    }
}
