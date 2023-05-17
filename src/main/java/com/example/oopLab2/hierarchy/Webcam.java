package com.example.oopLab2.hierarchy;

import com.example.oopLab2.annotation.Name;
import com.example.oopLab2.annotation.Type;

public class Webcam extends InputDevice {
    public int frameRate;

    public double matrixResolution;

    public Webcam(String brand, int price, String connectionType, InputInformationType type, int frameRate, double matrixResolution) {
        super(brand, price, connectionType, type);
        this.frameRate = frameRate;
        this.matrixResolution = matrixResolution;
    }

    @Name("Frame rate(FPS)")
    @Type("Integer")
    public int getFrameRate() {
        return frameRate;
    }

    @Name("Frame rate(FPS)")
    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }

    @Name("Matrix resolution(Mp)")
    @Type("Double")
    public double getMatrixResolution() {
        return matrixResolution;
    }

    @Name("Matrix resolution(Mp)")
    public void setMatrixResolution(double matrixResolution) {
        this.matrixResolution = matrixResolution;
    }

    public Webcam() {
    }
}
