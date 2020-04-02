package com.solan.jbase.utils;

/**
 * TODO:
 *
 * @author: hyl
 * @date: 2020/3/30 13:59
 */
public class StringUtils {
    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }
}
