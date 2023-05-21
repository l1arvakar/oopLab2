package com.example.oopLab2.tools;

import com.example.oopLab2.annotation.Name;
import com.example.oopLab2.annotation.Type;
import com.example.oopLab2.hierarchy.PCComponent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class GUI {
    private static void renameLabels(VBox LabelsVBox, ArrayList<String> labelTexts) {
        for (int i = 0; i < labelTexts.size(); i++) {
            Label label = (Label) LabelsVBox.getChildren().get(i);
            label.setText(labelTexts.get(i));
        }
    }

    public static ArrayList<Control> createLabelsAndInputs(HBox container, ArrayList<String> labelTexts, HashMap<String,String> typesMap, Class thisClass) {
        ArrayList<Control> inputs = new ArrayList<>();
        VBox LabelsVBox = (VBox) container.getChildren().get(0);
        VBox InputsVBox = (VBox) container.getChildren().get(1);
        LabelsVBox.setSpacing(5);
        InputsVBox.setSpacing(5);
        LabelsVBox.getChildren().clear();
        InputsVBox.getChildren().clear();
        int i = 0;
        while (i < labelTexts.size()) {
            Label label = new Label();
            if (Objects.equals(typesMap.get(labelTexts.get(i)), "Enum")) {
                ComboBox comboBox = new ComboBox();
                Class<? extends Enum> enumClass = getEnumClass(thisClass);
                for (Enum<?> value : enumClass.getEnumConstants()) {
                    comboBox.getItems().add(getAnnotationValue(value));
                }
                comboBox.setValue(getAnnotationValue(enumClass.getEnumConstants()[0]));
                inputs.add(comboBox);
                InputsVBox.getChildren().add(comboBox);
            } else {
                TextField input = new TextField();
                inputs.add(input);
                InputsVBox.getChildren().add(input);
            }
            label.setStyle("-fx-font-size: 16px");
            LabelsVBox.getChildren().add(label);
            i++;
        }
        renameLabels(LabelsVBox, labelTexts);
        return inputs;
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

    private static String getAnnotationValue(Enum<?> enumValue) {
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

    public static void clearInputs(ArrayList<Control> inputs, ArrayList<Label> labels, HashMap<String,String> typesMap, Class thisClass) {
        for (int i = 0; i < inputs.size(); i++) {
            if (Objects.equals(typesMap.get(labels.get(i).getText()), "Enum")) {
                ComboBox comboBox = (ComboBox) inputs.get(i);
                Class<? extends Enum> enumClass = getEnumClass(thisClass);
                comboBox.setValue(getAnnotationValue(enumClass.getEnumConstants()[0]));
            } else {
                TextField textField = (TextField) inputs.get(i);
                textField.setText("");
                textField.setStyle("");
            }
        }
    }

    public static void putInfoToInputs(ArrayList<Label> labels, ArrayList<Control> inputs, HashMap<String, Method> map, PCComponent instance) {
        for (int i = 0; i < labels.size(); i++) {
            try {
                if (inputs.get(i) instanceof ComboBox) {
                    ((ComboBox) inputs.get(i)).setValue(getAnnotationValue((Enum<?>) map.get(labels.get(i).getText()).invoke(instance)));
                } else {
                    ((TextField) inputs.get(i)).setText(String.valueOf(map.get(labels.get(i).getText()).invoke(instance)));
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
