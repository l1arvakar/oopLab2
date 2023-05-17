package com.example.oopLab2.factories;

import com.example.oopLab2.annotation.Name;
import com.example.oopLab2.hierarchy.InputInformationType;
import com.example.oopLab2.hierarchy.OutputInformationType;
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
            Method> mapOfSetters, HashMap<String, String> mapOfTypes, PCComponent instance) {
        for (int i = 0; i < labelTexts.size(); i++) {
            try {
                switch (mapOfTypes.get(labelTexts.get(i))) {
                    case "String":
                        mapOfSetters.get(labelTexts.get(i)).invoke(instance, ((TextField) inputs.get(i)).getText());
                        break;
                    case "Integer":
                        mapOfSetters.get(labelTexts.get(i)).invoke(instance, Integer.parseInt(((TextField) inputs.get(i)).getText()));
                        break;
                    case "Double":
                        mapOfSetters.get(labelTexts.get(i)).invoke(instance, Double.parseDouble(((TextField) inputs.get(i)).getText()));
                        break;
                    case "Enum":
                        if (labelTexts.get(i).startsWith("Input")) {
                            mapOfSetters.get(labelTexts.get(i)).invoke(instance, getEnumByAnnotationValue(InputInformationType.class, (String) ((ComboBox<?>) inputs.get(i)).getValue()));
                        } else {
                            mapOfSetters.get(labelTexts.get(i)).invoke(instance, getEnumByAnnotationValue(OutputInformationType.class, (String) ((ComboBox<?>) inputs.get(i)).getValue()));
                        }
                }
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);

            }
        }
    }

    private static <T extends Enum<T>> T getEnumByAnnotationValue(Class<T> enumClass, String annotationValue) {
        T[] enumValues = enumClass.getEnumConstants();
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