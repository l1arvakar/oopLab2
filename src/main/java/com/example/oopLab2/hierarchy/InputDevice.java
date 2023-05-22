package com.example.oopLab2.hierarchy;

import com.example.oopLab2.annotation.Name;
import com.example.oopLab2.annotation.Type;

import java.io.Serializable;

public abstract class InputDevice extends PCComponent implements Serializable {
    @Type("Enum")
    public InputInformationType type;

    public InputDevice(String brand, int price, String connectionType, InputInformationType type) {
        super(brand, price, connectionType);
        this.type = type;
    }

    public InputDevice() {

    }

    @Name("Input information type")
    public void setType(InputInformationType type) {
        this.type = type;
    }

    @Name("Input information type")
    @Type("Enum")
    public InputInformationType getType() {
        return this.type;
    }
}
