package com.example.oopLab2.hierarchy;

import com.example.oopLab2.annotation.Name;
import com.example.oopLab2.annotation.Type;

import java.io.Serializable;

public class GraphicsCard extends SystemBlockDevice implements Serializable {
    @Type("Integer")
    public int gpuMemorySize;

    @Type("String")
    public String coolingSystem;

    public GraphicsCard (String brand, int price, String connectionType, int height, int length, int width, int gpuMemorySize, String coolingSystem) {
        super(brand, price, connectionType, height, length, width);
        this.gpuMemorySize = gpuMemorySize;
        this.coolingSystem = coolingSystem;
    }

    public GraphicsCard() {

    }

    @Name("Gpu memory size(GB)")
    @Type("Integer")
    public int getGpuMemorySize() {
        return gpuMemorySize;
    }

    @Name("Gpu memory size(GB)")
    public void setGpuMemorySize(int gpuMemorySize) {
        this.gpuMemorySize = gpuMemorySize;
    }

    @Name("Cooling system")
    @Type("String")
    public String getCoolingSystem() {
        return coolingSystem;
    }

    @Name("Cooling system")
    public void setCoolingSystem(String coolingSystem) {
        this.coolingSystem = coolingSystem;
    }
}
