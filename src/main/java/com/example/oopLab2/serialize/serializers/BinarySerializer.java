package com.example.oopLab2.serialize.serializers;

import com.example.oopLab2.hierarchy.PCComponent;
import com.example.oopLab2.serialize.Serializer;
import javafx.scene.control.Alert;

import java.io.*;
import java.util.ArrayList;

public class BinarySerializer implements Serializer {
    @Override
    public String getName() {
        return "Binary";
    }

    @Override
    public String getExtension() {
        return "*.bin";
    }

    @Override
    public void serialize(ArrayList<PCComponent> components, String path) {
        try {
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(path));
            stream.writeObject(components);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while binary serialization");
            alert.setContentText("Check file info.");
            alert.showAndWait();
        }
    }

    @Override
    public ArrayList<PCComponent> deserialize(String path) {
        ArrayList<PCComponent> newArray = new ArrayList<>();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(path))){
            newArray = (ArrayList<PCComponent>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while binary deserialization");
            alert.setContentText("Check file info.");
            alert.showAndWait();
        }
        return newArray;
    }
}
