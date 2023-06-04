package com.example.oopLab2.factories;

import com.example.oopLab2.annotation.Name;
import com.example.oopLab2.annotation.Type;
import com.example.oopLab2.hierarchy.PCComponent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public interface MainFactory {
    default void createInstance(ArrayList<String> labelTexts, ArrayList<Control> inputs, HashMap<String,
            Method> mapOfSetters, HashMap<String, Class<?>> mapOfTypes, PCComponent instance) {
        for (int i = 0; i < labelTexts.size(); i++) {
            try {
                Class<?> aClass = mapOfTypes.get(labelTexts.get(i));
                if (aClass.getSimpleName().equals("String")) {
                    mapOfSetters.get(labelTexts.get(i)).invoke(instance, ((TextField) inputs.get(i)).getText());
                } else if (aClass.getSimpleName().equals("int")) {
                    mapOfSetters.get(labelTexts.get(i)).invoke(instance, Integer.parseInt(((TextField) inputs.get(i)).getText()));
                } else if (aClass.getSimpleName().equals("double")) {
                    mapOfSetters.get(labelTexts.get(i)).invoke(instance, Double.parseDouble(((TextField) inputs.get(i)).getText()));
                } else if (aClass.isEnum()) {
                    mapOfSetters.get(labelTexts.get(i)).invoke(instance, getEnumByAnnotationValue(mapOfTypes.get(labelTexts.get(i)), (String) ((ComboBox<?>) inputs.get(i)).getValue()));
                }
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);

            }
        }
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

    private static <T extends Enum<T>> T getEnumByAnnotationValue(Class<?> enumClass, String annotationValue) {
        T[] enumValues = (T[]) enumClass.getEnumConstants();
        for (T enumValue : enumValues) {
            String value = getAnnotationValue(enumValue);
            if (annotationValue.equals(value)) {
                return enumValue;
            }
        }
        return null;
    }

    private static <T extends Enum<T>> String getAnnotationValue(T enumValue) {
        try {
            Class<? extends Enum> enumClass = enumValue.getClass();
            Field field = enumClass.getField(enumValue.name());
            Annotation annotation = field.getAnnotation(Name.class);
            if (annotation != null) {
                return ((Name) annotation).value();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    boolean checkInputs();

    PCComponent getComponent();

    ArrayList<Control> getInputs();

    void createInputs(HBox container);

    void fillInputs(PCComponent component, ArrayList<Label> labels);
}
