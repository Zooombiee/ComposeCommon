package com.cxc.common.utils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.TreeMap;

/**
 * Created by wpt on 2017/4/13.
 */

public class ZKGsonUtils {

    private static Gson instance;

    static {
        instance = new Gson();
    }

    private ZKGsonUtils() {
    }

    public static Gson getGsonInstance() {
        return instance;
    }

    public String toJson(Object object) {
        return instance.toJson(object);
    }

    public String toJson(Object object, Type type) {
        return instance.toJson(object, type);
    }

    public <T> T fromJson(String json, Class<T> classOfT) {
        if (json != null && classOfT != null) {
            Object object = fromJson((Reader) (new StringReader(json)), (Type) classOfT);

            try {
                return Primitives.wrap(classOfT).cast(object);
            } catch (Throwable var4) {
                return null;
            }
        } else {
            return null;
        }
    }

    public <T> T fromJson(String json, Type typeOf) {
        return json != null && typeOf != null ? fromJson((Reader) (new StringReader(json)), (Type) typeOf) : null;
    }

    public <T> T fromJson(Reader json, Type typeOfT) {
        if (json != null && typeOfT != null) {
            try {
                JsonReader jsonReader = new JsonReader(json);
                return instance.fromJson(jsonReader, typeOfT);
            } catch (Throwable var3) {
                return null;
            }
        } else {
            return null;
        }
    }


    //使用MapTypeAdapter构建的Gson对象（用于解决int型数据转成double的问题）
    private static Gson gsonFromMapAdapter;
    /**
     * json转map，使用自定义的{@link MapTypeAdapter}，避免int型数据转成double的问题
     *
     * @param strJson
     * @return
     */
    public static TreeMap<Object, Object> jsonToMap(String strJson) {
        if (gsonFromMapAdapter == null) {
            gsonFromMapAdapter = new GsonBuilder()
                    .registerTypeAdapter(new TypeToken<TreeMap<String, Object>>() {
                    }.getType(), new MapTypeAdapter()).create();
        }
        return gsonFromMapAdapter.fromJson(strJson, new TypeToken<TreeMap<String, Object>>() {
        }.getType());
    }


}
