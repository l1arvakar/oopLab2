package com.example.oopLab2.serialize.serializers;

import com.example.oopLab2.annotation.Type;
import com.example.oopLab2.hierarchy.PCComponent;
import com.example.oopLab2.serialize.Serializer;
import com.example.oopLab2.tools.Maps;
import javafx.scene.control.Alert;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TextSerializer implements Serializer {
    private final String CLASS_NAME_PREFIX = "com.example.oopLab2.hierarchy.";

    @Override
    public String getName() {
        return "Text";
    }

    @Override
    public String getExtension() {
        return "*.txt";
    }

    @Override
    public byte[] serialize(ArrayList<PCComponent> components) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            for (PCComponent gadget : components) {
                String fullClassName = gadget.getClass().getSimpleName();
                stringBuilder.append("Class:" + fullClassName + ";");
                HashMap<String, Method> map = Maps.getMapOfSettersOrGetters("get", gadget.getClass());
                for (String key : map.keySet()) {
                    Class<?> returnType = map.get(key).getReturnType();
                    String simpleName = returnType.getSimpleName();
                    if (returnType.isEnum() || simpleName.equals("int") || simpleName.equals("boolean") || simpleName.equals("double")) {
                        String value = String.valueOf(map.get(key).invoke(gadget));
                        stringBuilder.append(key + ":" + value + ";");
                    } else if (simpleName.equals("String")) {
                        String value = String.valueOf(map.get(key).invoke(gadget));
                        value = escaping(value);
                        stringBuilder.append(key + ":" + value + ";");
                    }
                }
                stringBuilder.append("\n");
            }
            return stringBuilder.toString().getBytes();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while txt serialization");
            alert.setContentText("Check file info.");
            alert.showAndWait();
        }
        return null;
    }

    @Override
    public ArrayList<PCComponent> deserialize(byte[] bytes) {
        ArrayList<PCComponent> components = new ArrayList<>();
        List<String> stringList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            char c = (char) b;
            if (c == '\n') {
                stringList.add(stringBuilder.toString());
                stringBuilder.setLength(0);  // Очищаем StringBuilder для следующей строки
            } else {
                stringBuilder.append(c);
            }
        }
        if (stringBuilder.length() > 0) {
            stringList.add(stringBuilder.toString());
        }
        try {
            String className;
            Object instance;
            HashMap<String, Method> map;
            HashMap<String, Class<?>> mapOfTypes;
            for (String str: stringList) {
                str = str.trim();
                HashMap<String, String> valuesMap = strToMap(str);
                className = valuesMap.get("Class");
                Class<?> clazz = Class.forName(CLASS_NAME_PREFIX + className);
                instance = clazz.getConstructor().newInstance();
                map = Maps.getMapOfSettersOrGetters("set", clazz);
                mapOfTypes = Maps.getMapOfTypes(clazz);
                for (String field: valuesMap.keySet()) {
                    String value = valuesMap.get(field);
                    if (mapOfTypes.containsKey(field)) {
                        Class<? extends Enum> aClass = (Class<? extends Enum>) mapOfTypes.get(field);
                        String name = aClass.getSimpleName();
                        if (name.equals("int")) {
                            map.get(field).invoke(instance, Integer.parseInt(value));
                        } else if (name.equals("String")) {
                            value = removeBackSlash(value);
                            map.get(field).invoke(instance, value);
                        } else if (name.equals("boolean")) {
                            map.get(field).invoke(instance, Boolean.parseBoolean(value));
                        } else if (name.equals("double")) {
                            map.get(field).invoke(instance, Double.parseDouble(value));
                        } else if (aClass.isEnum()) {
                            map.get(field).invoke(instance, Enum.valueOf( aClass, value));
                        }
                    }
                }
                components.add((PCComponent) instance);
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while txt deserialization");
            alert.setContentText("Check file info.");
            alert.showAndWait();
        }
        return components;
    }

    private static Class getEnumClass(Class thisClass) {
        Field[] fields = thisClass.getFields();
        Field enumField = null;
        for (Field field : fields) {
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Type) {
                    Type typeAnnotation = (Type) annotation;
                    if ("Enum".equals(typeAnnotation.value())) {
                        enumField = field;
                    }
                }
            }
        }
        if (enumField != null) {
            return enumField.getType();
        } else {
            return null;
        }
    }

    private HashMap<String, String> strToMap(String line) throws IOException {
        HashMap<String, String> valuesMap = new HashMap<>();
        int index = 0;
        while(index < line.length()) {
            String part = "";
            int bs = 0;
            while (line.toCharArray()[index] != ';' || (line.toCharArray()[index - 1] == '\\' && ((bs & 1) == 1))) {
                if (line.toCharArray()[index] != '\\' && bs != 0) {
                    bs = 0;
                }
                if (line.toCharArray()[index] == '\\') {
                    bs++;
                }
                part = part + line.toCharArray()[index];
                index++;
            }
            String field = part.substring(0, part.indexOf(":"));
            String value = part.substring(part.indexOf(":") + 1);
            valuesMap.put(field, value);
            index++;
        }
        return valuesMap;
    }

    private String escaping(String line) {
        String[] splitters = {"\\", ":", ";"};
        for(int i = 0; i < 3; i++) {
            int index = 0;
            while(line.indexOf(splitters[i], index) != -1) {
                StringBuilder builder = new StringBuilder(line);
                builder.insert(line.indexOf(splitters[i], index), "\\");
                line = builder.toString();
                index = line.indexOf(splitters[i], index) + 1  + (i == 0 ? 1 : 0);
            }
        }
        return line;
    }
    private String removeBackSlash(String line) {
        String[] splitters = {"\\:", "\\;", "\\\\"};
        for(int i = 0; i < 3; i++) {
            int index = 0;
            while(line.indexOf(splitters[i], index) != -1) {
                index = line.indexOf(splitters[i], index);
                StringBuilder builder = new StringBuilder(line);
                builder.replace(line.indexOf(splitters[i], index), line.indexOf(splitters[i], index) + 1, "");
                line = builder.toString();
                index++;
            }
        }
        return line;
    }
}

