package com.example.oopLab2.tools;

import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.HashMap;

public class Check {
    private static void recolorInput(TextField input, String color) {
        input.setStyle("-fx-background-color: white; -fx-border-width: 1; -fx-border-color: " + color + "; -fx-border-radius: 3");
    }

    private static boolean checkStringValue(TextField textField) {
        boolean isCorrect = textField.getText().matches("^[a-zA-Z]+[a-zA-Z0-9\\s-_]*$") || textField.getText().length() <= 20;
        if (!isCorrect)
            recolorInput(textField, "#ff0000");
        else
            recolorInput(textField, "#808080");
        return isCorrect;

    }

    public static boolean checkIntegerValue(TextField input, Integer min, Integer max) {
        boolean isCorrect;
        try {
            isCorrect = Integer.parseInt(input.getText()) >= min && Integer.parseInt(input.getText()) <= max;
        } catch (NumberFormatException e) {
            isCorrect = false;
        }
        if (!isCorrect)
            recolorInput(input, "#ff0000");
        else
            recolorInput(input, "#808080");
        return isCorrect;
    }

    public static boolean checkDoubleValue(TextField input, Double min, Double max) {
        boolean isCorrect;
        try {
            isCorrect = Double.parseDouble(input.getText()) >= min && Double.parseDouble(input.getText()) <= max;
        } catch (NumberFormatException e) {
            isCorrect = false;
        }
        if (!isCorrect)
            recolorInput(input, "#ff0000");
        else
            recolorInput(input, "#808080");
        return isCorrect;
    }

    public static boolean checkInputs(ArrayList<Control> inputs, ArrayList<String> labelsTexts, Class thisClass) {
        HashMap<String, String> map = Maps.getMapOfTypes(thisClass);
        boolean[] isCorrects = new boolean[map.size() + 1];
        for (int i = 0; i < map.size(); i++) {
            isCorrects[i] = true;
        }
        for (int i = 0; i < labelsTexts.size(); i++) {
            switch (map.get(labelsTexts.get(i))) {
                case "String" -> isCorrects[i] = checkStringValue((TextField) inputs.get(i));
                case "Integer" -> {
                    switch (labelsTexts.get(i)) {
                        case "Number of cores" -> isCorrects[i] = checkIntegerValue((TextField) inputs.get(i), 1, 256);
                        case "Gpu memory size(GB)" -> isCorrects[i] = checkIntegerValue((TextField) inputs.get(i), 1, 100);
                        case "Max volume(dB)" -> isCorrects[i] = checkIntegerValue((TextField) inputs.get(i), 40, 140);
                        case "Brightness (cd/m2)" -> isCorrects[i] = checkIntegerValue((TextField) inputs.get(i), 1, 600);
                        case "Price($)" -> isCorrects[i] = checkIntegerValue((TextField) inputs.get(i), 1, 100000);
                        case "Height", "Length", "Width" -> isCorrects[i] = checkIntegerValue((TextField) inputs.get(i), 1, 50);
                        case "Frame rate(FPS)" -> isCorrects[i] = checkIntegerValue((TextField) inputs.get(i), 15, 390);
                    }
                }
                    case "Double" -> {
                        switch (labelsTexts.get(i)) {
                            case "Clock speed" -> isCorrects[i] = checkDoubleValue((TextField) inputs.get(i), 0.1, 100.0);
                            case "Screen size" -> isCorrects[i] = checkDoubleValue((TextField) inputs.get(i), 10.0, 60.0);
                            case "Matrix resolution(Mp)" -> isCorrects[i] = checkDoubleValue((TextField) inputs.get(i), 1.00, 33.0);
                        }
                    }
                }
            }


        for (int i = 0; i < map.size(); i++) {
            if (!isCorrects[i]) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Incorrect input!");
                alert.setContentText("Check input(-s).");
                alert.showAndWait();
                return false;
            }
        }

        return true;
    }


}
