/**
 * Copyright (C) 2025 Adam. All rights reserved.
 *
 * This class is used to provide some utility functions.
 *
 * @Author: Adam Chen
 * @Date: 2025-11-24
 */
package com.adam.app.lottogame;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class Utils {
    // TAG: LottoGame
    public static final String TAG = "LottoGame";
    public static final String Utils_TAG = "Utils";

    /**
     * Logcat with message
     */
    public static void log(String title, String message) {
        Log.i(TAG, title + ": " + message);
    }

    /**
     * simple dump list
     */
    public static void dumpList(List<?> list) {
        final String tag = Utils_TAG;
        log(tag, "dumpList");
        if (list == null) {
            log(tag, "list is null");
            return;
        }
        int size = list.size();
        if (size == 0) {
            log(tag, "list is empty");
            return;
        }

        log(tag, "====================================================");
        for (int i = 0; i < size; i++) {
            Object elem = list.get(i);
            // avoid to NPE when list.get(i) is null
            log(tag, "list[" + i + "] = " + String.valueOf(elem));
        }
        log(tag, "====================================================");
    }


    /**
     * dump list with max items
     * @param list List
     * @param maxItems max items
     */
    public static void dumpList(List<?> list, int maxItems) {
        final String tag = Utils_TAG;
        if (list == null) {
            log(tag, "dumpList: list is null");
            return;
        }

        int size = list.size();
        StringBuilder sb = new StringBuilder(256);
        sb.append("dumpList (size=").append(size).append(")\n");
        sb.append("====================================================\n");

        int limit = (maxItems > 0) ? Math.min(size, maxItems) : size;
        for (int i = 0; i < limit; i++) {
            sb.append("list[").append(i).append("] = ")
                    .append(String.valueOf(list.get(i))).append('\n');
        }
        if (limit < size) {
            sb.append("... (").append(size - limit).append(" more items truncated)\n");
        }

        sb.append("====================================================");
        log(tag, sb.toString());
    }

    /**
     * dump list with formatter
     * @param name name
     * @param list list
     * @param maxItems max items
     * @param formatter formatter
     */
    public static void dumpList(
            String name,
            List<?> list,
            int maxItems,
            Function<Object, String> formatter
    ) {
        final String tag = Utils_TAG;

        if (list == null) {
            log(tag, name + ": list is null");
            return;
        }

        // avoid to change the original list
        List<?> snapshot = new ArrayList<>(list);
        int size = snapshot.size();

        StringBuilder sb = new StringBuilder(256);
        sb.append("dumpList[").append(name).append("] (size=").append(size).append(")\n");
        sb.append("====================================================\n");

        Function<Object,String> fmt = (formatter != null)
                ? formatter
                : o -> Objects.toString(o); // safety null handler

        int limit = (maxItems > 0) ? Math.min(size, maxItems) : size;
        for (int i = 0; i < limit; i++) {
            Object elem = snapshot.get(i);
            sb.append("[").append(i).append("] ")
                    .append(fmt.apply(elem)).append('\n');
        }
        if (limit < size) {
            sb.append("... (").append(size - limit).append(" more items truncated)\n");
        }

        sb.append("====================================================");
        log(tag, sb.toString());
    }


}
