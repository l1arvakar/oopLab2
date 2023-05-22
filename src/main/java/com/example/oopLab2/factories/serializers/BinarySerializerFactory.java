package com.example.oopLab2.factories.serializers;

import com.example.oopLab2.serialize.serializers.BinarySerializer;
import com.example.oopLab2.serialize.Serializer;

public class BinarySerializerFactory implements SerializerFactory{

    @Override
    public Serializer getSerializer() {
        return new BinarySerializer();
    }
}
