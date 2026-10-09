package com.example.advantumconverter.service.excel.converter;

import com.example.advantumconverter.support.AbstractConverterIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

class NestedDataClassesTest extends AbstractConverterIntegrationTest {

    @Autowired
    private List<ConvertService> converters;

    @Test
    void nestedDataClasses() {
        for (ConvertService converter : converters) {
            Class<?> type = converter.getClass();
            while (type != null && type != Object.class) {
                exerciseNested(type);
                type = type.getSuperclass();
            }
        }
    }

    private void exerciseNested(Class<?> outer) {
        for (Class<?> nested : outer.getDeclaredClasses()) {
            if (nested.isEnum() || nested.isInterface() || Modifier.isAbstract(nested.getModifiers())) {
                continue;
            }
            Object instance = instantiate(nested);
            if (instance != null) {
                invokeAccessors(nested, instance);
                instance.hashCode();
                instance.toString();
                instance.equals(instance);
                instance.equals(null);
                instance.equals("other");
                Object twin = instantiate(nested);
                if (twin != null) {
                    instance.equals(twin);
                    twin.equals(instance);
                }
            }
            exerciseNested(nested);
        }
    }

    private void invokeAccessors(Class<?> type, Object instance) {
        for (var method : type.getDeclaredMethods()) {
            if (Modifier.isStatic(method.getModifiers())) {
                continue;
            }
            try {
                if (method.getName().startsWith("set") && method.getParameterCount() == 1) {
                    method.setAccessible(true);
                    method.invoke(instance, defaultValue(method.getParameterTypes()[0]));
                } else if ((method.getName().startsWith("get") || method.getName().startsWith("is"))
                        && method.getParameterCount() == 0 && method.getReturnType() != void.class) {
                    method.setAccessible(true);
                    method.invoke(instance);
                }
            } catch (Exception ignored) {
            }
        }
    }

    private Object defaultValue(Class<?> type) {
        if (!type.isPrimitive()) {
            return null;
        }
        if (type == boolean.class) {
            return false;
        }
        if (type == char.class) {
            return (char) 0;
        }
        if (type == float.class) {
            return 0f;
        }
        if (type == double.class) {
            return 0d;
        }
        if (type == long.class) {
            return 0L;
        }
        if (type == byte.class) {
            return (byte) 0;
        }
        if (type == short.class) {
            return (short) 0;
        }
        return 0;
    }

    private Object instantiate(Class<?> type) {
        try {
            var constructor = type.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception ignored) {
            try {
                var field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
                field.setAccessible(true);
                var unsafe = (sun.misc.Unsafe) field.get(null);
                return unsafe.allocateInstance(type);
            } catch (Exception ex) {
                return null;
            }
        }
    }
}
