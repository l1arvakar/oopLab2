package com.example.oopLab2.tools;

import com.example.oopLab2.annotation.Name;
import com.example.oopLab2.hierarchy.InputInformationType;
import com.example.oopLab2.hierarchy.OutputInformationType;
import com.example.oopLab2.hierarchy.PCComponent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class GUI {
    private static void renameLabels(VBox LabelsVBox, ArrayList<String> labelTexts) {
        for (int i = 0; i < labelTexts.size(); i++) {
            Label label = (Label) LabelsVBox.getChildren().get(i);
            label.setText(labelTexts.get(i));
        }
    }

    public static ArrayList<Control> createLabelsAndInputs(HBox container, ArrayList<String> labelTexts) {
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
            if (labelTexts.get(i).startsWith("Input")) {
                ComboBox comboBox = new ComboBox();
                for (InputInformationType value : InputInformationType.values()) {
                    comboBox.getItems().add(getAnnotationValue(value));
                }
                comboBox.setValue(getAnnotationValue(InputInformationType.values()[0]));
                inputs.add(comboBox);
                InputsVBox.getChildren().add(comboBox);
            } else if (labelTexts.get(i).startsWith("Output")) {
                ComboBox comboBox = new ComboBox();
                for (OutputInformationType value : OutputInformationType.values()) {
                    comboBox.getItems().add(getAnnotationValue(value));
                }
                comboBox.setValue(getAnnotationValue(OutputInformationType.values()[0]));
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

    public static void clearInputs(ArrayList<Control> inputs, ArrayList<Label> labels) {
        for (int i = 0; i < inputs.size(); i++) {
            if (labels.get(i).getText().startsWith("Input")) {
                ComboBox comboBox = (ComboBox) inputs.get(i);
                comboBox.setValue(getAnnotationValue(InputInformationType.values()[0]));
            } else if (labels.get(i).getText().startsWith("Output")) {
                ComboBox comboBox = (ComboBox) inputs.get(i);
                comboBox.setValue(getAnnotationValue(OutputInformationType.values()[0]));
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
                if (labels.get(i).getText().startsWith("Input") || labels.get(i).getText().startsWith("Output")) {
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
