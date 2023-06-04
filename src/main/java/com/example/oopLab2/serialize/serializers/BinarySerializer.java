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
    public byte[] serialize(ArrayList<PCComponent> components) {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);

            objectStream.writeObject(components);
            objectStream.close();

            byte[] byteArray = byteStream.toByteArray();
            return byteArray;
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while binary serialization");
            alert.setContentText("Check file info.");
            alert.showAndWait();
        }
        return new byte[0];
    }

    @Override
    public ArrayList<PCComponent> deserialize(byte[] bytes) {
        ArrayList<PCComponent> newArray = new ArrayList<>();
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);){
            ObjectInputStream objectInputStream = new ObjectInputStream(bais);
            return (ArrayList<PCComponent>) objectInputStream.readObject();
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
