package com.example.oopLab2.serialize.serializers;

import com.example.oopLab2.annotation.Type;
import com.example.oopLab2.hierarchy.PCComponent;
import com.example.oopLab2.serialize.Serializer;
import com.example.oopLab2.tools.Maps;
import javafx.scene.control.Alert;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

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
    public void serialize(ArrayList<PCComponent> components, String path) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path))) {
            for (PCComponent gadget : components) {
                String fullClassName = gadget.getClass().getSimpleName();
                bufferedWriter.write("Class:" + fullClassName + ";");
                HashMap<String, Method> map = Maps.getMapOfSettersOrGetters("get", gadget.getClass());
                for (String key : map.keySet()) {
                    String returnType = map.get(key).getReturnType().getSimpleName();
                    if (returnType.equals("int") || returnType.equals("boolean") || returnType.equals("double") || returnType.equals("OutputInformationType") || returnType.equals("InputInformationType")) {
                        String value = String.valueOf(map.get(key).invoke(gadget));
                        bufferedWriter.write(key + ":" + value + ";");
                    } else if (returnType.equals("String")) {
                        String value = String.valueOf(map.get(key).invoke(gadget));
                        value = escaping(value);
                        bufferedWriter.write(key + ":" + value + ";");
                    }
                }
                bufferedWriter.write("\n");
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while txt serialization");
            alert.setContentText("Check file info.");
            alert.showAndWait();
        }
    }

    @Override
    public ArrayList<PCComponent> deserialize(String path) {
        ArrayList<PCComponent> components = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String str = bufferedReader.readLine();
            String className;
            Object instance;
            HashMap<String, Method> map;
            HashMap<String, String> mapOfTypes;
            while (str != null) {
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
                        switch (mapOfTypes.get(field)) {
                            case "Integer" -> map.get(field).invoke(instance, Integer.parseInt(value));
                            case "String" -> {
                                value = removeBackSlash(value);
                                map.get(field).invoke(instance, value);
                            }
                            case "Boolean" -> map.get(field).invoke(instance, Boolean.parseBoolean(value));
                            case "Double" -> map.get(field).invoke(instance, Double.parseDouble(value));
                            case "Enum" -> {
                                Class<? extends Enum> enumClass = getEnumClass(instance.getClass());
                                map.get(field).invoke(instance, Enum.valueOf(enumClass, value));
                            }
                        }
                    }
                }
                components.add((PCComponent) instance);
                str = bufferedReader.readLine();
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

