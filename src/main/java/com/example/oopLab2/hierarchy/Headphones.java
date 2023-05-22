package com.example.oopLab2.hierarchy;

import com.example.oopLab2.annotation.Name;
import com.example.oopLab2.annotation.Type;

import java.io.Serializable;

public class Headphones extends OutputDevice implements Serializable {
    @Type("Integer")
    public int maxVolume;

    public Headphones(String brand, int price, String connectionType, OutputInformationType type, int maxVolume) {
        super(brand, price, connectionType, type);
        this.maxVolume = maxVolume;
    }

    public Headphones() {
    }

    @Name("Max volume(dB)")
    @Type("Integer")
    public int getMaxVolume() {
        return maxVolume;
    }

    @Name("Max volume(dB)")
    public void setMaxVolume(int maxVolume) {
        this.maxVolume = maxVolume;
    }
}
