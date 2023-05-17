package com.example.oopLab2.hierarchy;

import com.example.oopLab2.annotation.Name;
import com.example.oopLab2.annotation.Type;

public abstract class PCComponent {
    public String brand;

    public int price;

    public String connectionType;

    public PCComponent(String brand, int price, String connectionType) {
        this.brand = brand;
        this.price = price;
        this.connectionType = connectionType;
    }

    public PCComponent() {

    }

    @Name("Brand")
    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Name("Price($)")
    public void setPrice(int price) {
        this.price = price;
    }

    @Name("Connection type")
    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    @Name("Brand")
    @Type("String")
    public String getBrand() {
        return this.brand;
    }

    @Name("Price($)")
    @Type("Integer")
    public int getPrice() {
        return this.price;
    }

    @Name("Connection type")
    @Type("String")
    public String getConnectionType() {
        return this.connectionType;
    }
}
