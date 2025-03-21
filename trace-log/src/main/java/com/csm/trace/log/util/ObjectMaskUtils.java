package com.csm.trace.log.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class ObjectMaskUtils {

    public static Object[] maskCollections(Object[] objs, boolean maskEnabled) {
        if (!maskEnabled || objs == null) {
            return objs;
        }

        return Arrays.stream(objs)
                .map(obj -> ObjectMaskUtils.maskCollections(obj, maskEnabled))
                .toArray();
    }

    public static Object maskCollections(Object obj, boolean maskEnabled) {
        if (!maskEnabled || obj == null) {
            return obj;
        }

        if (obj instanceof Collection) {
            Collection<?> coll = (Collection<?>) obj;
            return String.format("%s[size=%d]", coll.getClass().getSimpleName(), coll.size());
        }
        if (obj instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) obj;
            return String.format("%s[size=%d]", map.getClass().getSimpleName(), map.size());
        }
        if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            return String.format("%s[length=%d]", obj.getClass().getComponentType().getSimpleName() + "[]", length);
        }
        if (obj instanceof Iterable) {
            int size = 0;
            for (Object o : (Iterable<?>) obj) {
                size++;
            }
            return String.format("%s[size=%d]", obj.getClass().getSimpleName(), size);
        }
        return obj;
    }
}