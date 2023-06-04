package com.example.oopLab2.serialize;

import com.example.oopLab2.hierarchy.PCComponent;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.File;
import java.util.ArrayList;

public interface Serializer {
    String getName();
    String getExtension();
    byte[] serialize(ArrayList<PCComponent> components);

    ArrayList<PCComponent> deserialize(final byte[] bytes) throws JsonProcessingException;
}
