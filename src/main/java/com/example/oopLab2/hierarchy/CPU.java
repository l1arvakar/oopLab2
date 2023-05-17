package com.example.oopLab2.hierarchy;

import com.example.oopLab2.annotation.Name;
import com.example.oopLab2.annotation.Type;

public class CPU extends SystemBlockDevice {

    public int numberOfCores;

    public double clockSpeed;


    public CPU (String brand, int price, String connectionType, int height, int length, int width, int numberOfCores, double clockSpeed) {
        super(brand, price, connectionType, height, length, width);
        this.numberOfCores = numberOfCores;
        this.clockSpeed = clockSpeed;
    }

    public CPU() {

    }

    @Name("Number of cores")
    @Type("Integer")
    public int getNumberOfCores() {
        return numberOfCores;
    }

    @Name("Number of cores")
    public void setNumberOfCores(int numberOfCores) {
        this.numberOfCores = numberOfCores;
    }

    @Name("Clock speed")
    @Type("Double")
    public double getClockSpeed() {
        return clockSpeed;
    }

    @Name("Clock speed")
    public void setClockSpeed(double clockSpeed) {
        this.clockSpeed = clockSpeed;
    }
}
