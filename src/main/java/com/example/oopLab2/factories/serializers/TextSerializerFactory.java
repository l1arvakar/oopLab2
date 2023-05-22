package com.example.oopLab2.factories.serializers;

import com.example.oopLab2.serialize.Serializer;
import com.example.oopLab2.serialize.serializers.TextSerializer;

public class TextSerializerFactory implements SerializerFactory{
    @Override
    public Serializer getSerializer() {
        return new TextSerializer();
    }
}
