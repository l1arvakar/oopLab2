package com.example.oopLab2.factories;

import com.example.oopLab2.annotation.Name;
import com.example.oopLab2.hierarchy.Headphones;
import com.example.oopLab2.hierarchy.PCComponent;
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

public class HeadphonesFactory implements MainFactory {

    private final ArrayList<String> labels;

    private static ArrayList<Control> inputs;

    public HeadphonesFactory() {
        labels = new ArrayList<>();
        Class<?> HeadphonesClass = Headphones.class;
        for (Method method : Arrays.stream(HeadphonesClass.getMethods()).filter(x -> x.getName().startsWith("get")).toList()) {
            if (method.isAnnotationPresent(Name.class))
                labels.add(method.getAnnotation(Name.class).value());
        }
    }

    @Override
    public boolean checkInputs() {
        return Check.checkInputs(inputs, labels, Headphones.class);
    }

    @Override
    public PCComponent getComponent() {
        Headphones headphones = new Headphones();
        HashMap<String, Class<?>> typesMap = Maps.getMapOfTypes(Headphones.class);
        HashMap<String, Method> settersMap = Maps.getMapOfSettersOrGetters("set", Headphones.class);
        createInstance(labels, inputs, settersMap, typesMap, headphones);
        return headphones;
    }

    @Override
    public ArrayList<Control> getInputs() {
        return inputs;
    }

    @Override
    public void createInputs(HBox container) {
        inputs = GUI.createLabelsAndInputs(container, labels, Maps.getMapOfTypes(Headphones.class), Headphones.class);
    }

    @Override
    public void fillInputs(PCComponent component, ArrayList<Label> labels) {
        Headphones headphones = (Headphones) component;
        HashMap<String, Method> map = Maps.getMapOfSettersOrGetters("get", Headphones.class);
        GUI.putInfoToInputs(labels, inputs, map, headphones);
    }
}
