package com.itlife.heavyheart.utils;

import java.util.Arrays;

// 类似pthon切片
public final class Slice {
    private Slice() {}

    public static <T> T[] copyOfRange(final T[] a, int start) {
        return copyOfRange(a, start, a.length);
    }

    public static <T> T[] copyOfRange(final T[] a, int start, int end) {
        if (start < 0) start = a.length + start;
        if (end < 0) end = a.length + end;
        return Arrays.copyOfRange(a, start, end);
    }
}
