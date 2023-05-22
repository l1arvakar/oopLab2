package com.example.oopLab2.factories.serializers;

import com.example.oopLab2.serialize.serializers.JSONSerializer;
import com.example.oopLab2.serialize.Serializer;

public class JSONSerializerFactory implements SerializerFactory{
    @Override
    public Serializer getSerializer() {
        return new JSONSerializer();
    }
}
