package com.example.oopLab2.tools;

import com.example.oopLab2.serialize.Serializer;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FileChooser {
    private static final Map<String, String> savingExtensions = new HashMap<>();

    private static void initSavingMap() {
        final String PATH = "src\\main\\java\\com\\example\\oopLab2\\serialize\\serializers";
        final String PREFIX = "com.example.oopLab2.serialize.serializers.";
        try {
            File folder = new File(PATH);
            File[] files = folder.listFiles();
            for (File file : Objects.requireNonNull(files)) {
                String className = file.getName().substring(0, file.getName().lastIndexOf("."));
                Class<?> serializer = Class.forName(PREFIX + className);
                Serializer thisSerializer = (Serializer) serializer.getConstructor().newInstance();
                savingExtensions.put(thisSerializer.getName(), thisSerializer.getExtension());
            }

        } catch (Exception ignored) {

        }
    }

    public static File getOpenFile() {
        initSavingMap();
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Open File");
        for (String key : savingExtensions.keySet()) {
            fileChooser.getExtensionFilters().add(
                    new javafx.stage.FileChooser.ExtensionFilter(key, savingExtensions.get(key)));
        }
        try {
            return fileChooser.showOpenDialog(new Stage());
        } catch (Exception e) {
            System.err.println("Select file!");
            return null;
        }
    }

    public static File getSaveFile() {
        initSavingMap();
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Save File");
        for (String key : savingExtensions.keySet()) {
            fileChooser.getExtensionFilters().add(
                    new javafx.stage.FileChooser.ExtensionFilter(key, savingExtensions.get(key))
            );
        }
        try {
            return fileChooser.showSaveDialog(new Stage());
        } catch (Exception e) {
            System.err.println("Select file!");
            return null;
        }
    }
}
