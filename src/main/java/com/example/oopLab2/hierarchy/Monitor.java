package com.example.oopLab2.hierarchy;

import com.example.oopLab2.annotation.Name;
import com.example.oopLab2.annotation.Type;

public class Monitor extends OutputDevice {
    public double screenSize;

    public int brightness;

    public Monitor(String brand, int price, String connectionType, OutputInformationType type, double screenSize, int brightness) {
        super(brand, price, connectionType, type);
        this.screenSize = screenSize;
        this.brightness = brightness;
    }

    @Name("Screen size")
    @Type("Double")
    public double getScreenSize() {
        return screenSize;
    }

    @Name("Screen size")
    public void setScreenSize(double screenSize) {
        this.screenSize = screenSize;
    }

    @Name("Brightness (cd/m2)")
    @Type("Integer")
    public int getBrightness() {
        return brightness;
    }

    @Name("Brightness (cd/m2)")
    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public Monitor() {
    }
}
