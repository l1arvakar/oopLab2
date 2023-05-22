package com.example.oopLab2.serialize;

import com.example.oopLab2.hierarchy.PCComponent;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;

public interface Serializer {
    String getName();
    String getExtension();
    void serialize(ArrayList<PCComponent> components, String path);

    ArrayList<PCComponent> deserialize(String path) throws JsonProcessingException;
}
