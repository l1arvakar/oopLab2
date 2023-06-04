package com.example.oopLab2.serialize.serializers;

import com.example.oopLab2.hierarchy.*;
import com.example.oopLab2.serialize.Serializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

public class JSONSerializer implements Serializer {
    private final Type collectionType = new TypeToken<Collection<PCComponent>>() {
    }.getType();
    private final Gson gson;

    public JSONSerializer()  {
        RuntimeTypeAdapterFactory<PCComponent> vehicleAdapterFactory = RuntimeTypeAdapterFactory.of(PCComponent.class, "Type")
                .registerSubtype(CPU.class, "CPU")
                .registerSubtype(GraphicsCard.class, "GraphicsCard")
                .registerSubtype(Headphones.class, "Headphones")
                .registerSubtype(Monitor.class, "Monitor")
                .registerSubtype(Webcam.class, "Webcam");

        gson = new GsonBuilder().registerTypeAdapterFactory(vehicleAdapterFactory).create();
    }

    @Override
    public String getName() {
        return "JSON";
    }

    @Override
    public String getExtension() {
        return "*.json";
    }

    @Override
    public byte[] serialize(ArrayList<PCComponent> components) {
            String json = gson.toJson(components, collectionType);
            return json.getBytes();
    }

    @Override
    public ArrayList<PCComponent> deserialize(byte[] bytes) throws JsonProcessingException {
        try {
            String json = new String(bytes);
            return gson.fromJson(json, collectionType);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while JSON deserialization");
            alert.setContentText("Check file info.");
            alert.showAndWait();
            return null;
        }
    }

    private byte[] readFile(File file) {
        byte[] bytes = new byte[(int) file.length()];
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            fileInputStream.read(bytes, 0, bytes.length);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bytes;
    }
}
