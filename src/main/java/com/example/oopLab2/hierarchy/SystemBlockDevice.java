package com.example.oopLab2.hierarchy;

import com.example.oopLab2.annotation.Name;
import com.example.oopLab2.annotation.Type;

public abstract class SystemBlockDevice extends PCComponent {
    public int height;

    public int length;

    public int width;

    public SystemBlockDevice(String brand, int price, String connectionType, int height, int length, int width) {
        super(brand, price, connectionType);
        this.height = height;
        this.length = length;
        this.width = width;
    }

    public SystemBlockDevice() {

    }

    @Name("Height")
    public void setHeight(int height) {
        this.height = height;
    }

    @Name("Length")
    public void setLength(int length) {
        this.length = length;
    }

    @Name("Width")
    public void setWidth(int width) {
        this.width = width;
    }

    @Name("Height")
    @Type("Integer")
    public int getHeight() {
        return this.height;
    }

    @Name("Length")
    @Type("Integer")
    public int getLength() {
        return this.length;
    }

    @Name("Width")
    @Type("Integer")
    public int getWidth() {
        return this.width;
    }
}
