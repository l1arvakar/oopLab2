package com.example.oopLab2.hierarchy;

import com.example.oopLab2.annotation.Name;

public enum InputInformationType {
    @Name("Graphic")
    GraphicInfo,
    @Name("Audio")
    AudioInfo,
    @Name("Coordinate")
    CoordinateInfo;
}
