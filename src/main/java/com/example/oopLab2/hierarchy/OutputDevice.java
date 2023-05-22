package com.example.oopLab2.hierarchy;

import com.example.oopLab2.annotation.Name;
import com.example.oopLab2.annotation.Type;

import java.io.Serializable;

public abstract class OutputDevice extends PCComponent implements Serializable {
    @Type("Enum")
    public OutputInformationType type;

    public OutputDevice(String brand, int price, String connectionType, OutputInformationType type) {
        super(brand, price, connectionType);
        this.type = type;
    }

    public OutputDevice() {

    }

    @Name("Output information type")
    public void setType(OutputInformationType type){
        this.type = type;
    }

    @Name("Output information type")
    @Type("Enum")
    public OutputInformationType getType() {
        return this.type;
    }
}
