/**
 * Copyright (C) 2025 Adam. All rights reserved.
 *
 * This class is the converter of lotto history.
 *
 * @Author: Adam Chen
 * @Date: 2025-11-26
 */
package com.adam.app.lottogame.data.util;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {
    private static final Gson mGson = new Gson();

    /**
     * Convert list to json
     * @param list List
     * @return json string
     */
    @TypeConverter
    public static String fromList(List<Integer> list) {
        return mGson.toJson(list);
    }

    /**
     * Convert json to list
     * @param json json string
     * @return list
     */
    @TypeConverter
    public static List<Integer> toList(String json) {
        Type type = new TypeToken<List<Integer>>() {}.getType();
        return mGson.fromJson(json, type);
    }
}
