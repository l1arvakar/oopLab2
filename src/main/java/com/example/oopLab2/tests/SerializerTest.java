package com.example.oopLab2.tests;

import com.example.oopLab2.hierarchy.*;
import com.example.oopLab2.serialize.serializers.TextSerializer;
import com.example.oopLab2.tools.Maps;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;



public class SerializerTest {
    private ArrayList<PCComponent> testComponents = new ArrayList<>();
    private TextSerializer textSerializer;
    private String text = "Class:Monitor;Brand:Huawei;Screen size:24.0;Connection type:HDMI;Price($):1200;Brightness (cd/m2):250;Output information type:GraphicInfo;\n" +
            "Class:Webcam;Matrix resolution(Mp):1.2;Brand:Sven;Input information type:GraphicInfo;Frame rate(FPS):30;Connection type:USB 2.0;Price($):50;\n" +
            "Class:CPU;Clock speed:2.9;Brand:Intel;Length:4;Connection type:LGA1200;Price($):400;Height:4;Number of cores:6;Width:1;";

    private String filePath = "E:\\Java\\oopLab\\oopLab2\\src\\main\\java\\com\\example\\oopLab2\\tests\\test.txt";

    private void initFile(ArrayList<PCComponent> components) {
        TextSerializer textSerializer = new TextSerializer();
        textSerializer.serialize(components, filePath);
    }

    private void compareTwoArraysOfComponents(ArrayList<PCComponent> components1, ArrayList<PCComponent> components2) throws InvocationTargetException, IllegalAccessException {
        for (int i = 0; i < components1.size(); i++) {
            HashMap<String, Method> map = Maps.getMapOfSettersOrGetters("get", components1.get(i).getClass());
            for (String key : map.keySet()) {
                assertEquals(map.get(key).invoke(components1.get(i)), map.get(key).invoke(components2.get(i)));
            }
        }
    }

    @Test
    public void testTextSerializeForCPU() throws InvocationTargetException, IllegalAccessException {
        ArrayList<PCComponent> components = new ArrayList<>();
        components.add(new CPU("Intel", 400, "LGA1200", 4,4,1,6, 2.9));
        initFile(components);
        TextSerializer textSerializer = new TextSerializer();
        ArrayList<PCComponent> newComponents = textSerializer.deserialize(filePath);
        compareTwoArraysOfComponents(components, newComponents);
    }

    @Test
    public void testTextSerializeForWebcam() throws InvocationTargetException, IllegalAccessException {
        ArrayList<PCComponent> components = new ArrayList<>();
        components.add(new Webcam("Sven", 50, "USB 2.0", InputInformationType.GraphicInfo, 30, 1.2));
        initFile(components);
        TextSerializer textSerializer = new TextSerializer();
        ArrayList<PCComponent> newComponents = textSerializer.deserialize(filePath);
        compareTwoArraysOfComponents(components, newComponents);
    }

    @Test
    public void testTextSerializeForMonitor() throws InvocationTargetException, IllegalAccessException {
        ArrayList<PCComponent> components = new ArrayList<>();
        components.add(new Monitor("Huawei", 1200, "HDMI", OutputInformationType.GraphicInfo, 24, 250));
        initFile(components);
        TextSerializer textSerializer = new TextSerializer();
        ArrayList<PCComponent> newComponents = textSerializer.deserialize(filePath);
        compareTwoArraysOfComponents(components, newComponents);
    }

    @Test
    public void testTextSerializeForAll() throws InvocationTargetException, IllegalAccessException {
        ArrayList<PCComponent> components = new ArrayList<>();
        components.add(new Monitor("Huawei", 1200, "HDMI", OutputInformationType.GraphicInfo, 24, 250));
        components.add(new Webcam("Sven", 50, "USB 2.0", InputInformationType.GraphicInfo, 30, 1.2));
        components.add(new CPU("Intel", 400, "LGA1200", 4,4,1,6, 2.9));
        initFile(components);
        TextSerializer textSerializer = new TextSerializer();
        ArrayList<PCComponent> newComponents = textSerializer.deserialize(filePath);
        compareTwoArraysOfComponents(components, newComponents);
    }

}
