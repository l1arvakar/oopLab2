package com.example.oopLab2.tools;

import com.example.oopLab2.annotation.Name;
import com.example.oopLab2.annotation.Type;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

public class Maps {
    public static HashMap<String, Method> getMapOfSettersOrGetters(String type, Class thisClass) {
        HashMap<String, Method> map = new HashMap<>();
        for (Method method : Arrays.stream(thisClass.getMethods()).filter(x -> x.getName().startsWith(type)).toList()) {
            if (method.isAnnotationPresent(Name.class)) {
                map.put(method.getAnnotation(Name.class).value(), method);
            }
        }
        return map;
    }

    public static HashMap<String, String> getMapOfTypes(Class thisClass) {
        HashMap<String, String> mapOfTypes = new HashMap<>();
        for (Method method : Arrays.stream(thisClass.getMethods()).filter(x -> x.getName().startsWith("get")).toList()) {
            if (method.isAnnotationPresent(Type.class) && method.isAnnotationPresent(Name.class)) {
                mapOfTypes.put(method.getAnnotation(Name.class).value(), method.getAnnotation(Type.class).value());
            }
        }
        return mapOfTypes;
    }
}
