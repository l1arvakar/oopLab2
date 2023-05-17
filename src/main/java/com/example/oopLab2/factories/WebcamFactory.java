package com.example.oopLab2.factories;

import com.example.oopLab2.annotation.Name;
import com.example.oopLab2.hierarchy.PCComponent;
import com.example.oopLab2.hierarchy.Webcam;
import com.example.oopLab2.tools.Check;
import com.example.oopLab2.tools.GUI;
import com.example.oopLab2.tools.Maps;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class WebcamFactory implements MainFactory {

    private final ArrayList<String> labels;

    private static ArrayList<Control> inputs;

    public WebcamFactory() {
        labels = new ArrayList<>();
        Class<?> WebcamClass = Webcam.class;
        for (Method method : Arrays.stream(WebcamClass.getMethods()).filter(x -> x.getName().startsWith("get")).toList()) {
            if (method.isAnnotationPresent(Name.class))
                labels.add(method.getAnnotation(Name.class).value());
        }
    }

    @Override
    public boolean checkInputs() {
        return Check.checkInputs(inputs, labels, Webcam.class);
    }

    @Override
    public PCComponent getComponent() {
        Webcam webcam = new Webcam();
        HashMap<String,String> typesMap = Maps.getMapOfTypes(Webcam.class);
        HashMap<String,Method> settersMap = Maps.getMapOfSettersOrGetters("set", Webcam.class);
        createInstance(labels, inputs, settersMap, typesMap, webcam);
        return webcam;
    }

    @Override
    public ArrayList<Control> getInputs() {
        return inputs;
    }

    @Override
    public void createInputs(HBox container) {
        inputs = GUI.createLabelsAndInputs(container, labels);
    }

    @Override
    public void fillInputs(PCComponent component, ArrayList<Label> labels) {
        Webcam webcam = (Webcam) component;
        HashMap<String, Method> map = Maps.getMapOfSettersOrGetters("get", Webcam.class);
        GUI.putInfoToInputs(labels, inputs, map, webcam);
    }
}
