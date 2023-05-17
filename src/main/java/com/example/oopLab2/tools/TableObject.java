package com.example.oopLab2.tools;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class TableObject {
    private final SimpleIntegerProperty index;
    private final SimpleStringProperty type;
    private final SimpleStringProperty brand;
    private final SimpleIntegerProperty price;
    private final SimpleStringProperty connection;

    public String getConnection() {
        return connection.get();
    }

    public void setConnection(String connection) {
        this.connection.set(connection);
    }


    public int getPrice() {
        return price.get();
    }

    public void setPrice(int price) {
        this.price.set(price);
    }

    public TableObject(int index, String type, String brand, int price, String connection) {
        this.index = new SimpleIntegerProperty(index);
        this.type = new SimpleStringProperty(type);
        this.brand = new SimpleStringProperty(brand);
        this.price = new SimpleIntegerProperty(price);
        this.connection = new SimpleStringProperty(connection);
    }
    public String getBrand() {
        return brand.get();
    }

    public void setBrand(String brand) {
        this.brand.set(brand);
    }

    public int getIndex() {
        return index.get();
    }

    public void setIndex(int index) {
        this.index.set(index);
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }
}
