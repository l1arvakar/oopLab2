package com.example.oopLab2.factories;

import com.example.oopLab2.annotation.Name;
import com.example.oopLab2.hierarchy.Monitor;
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

public class MonitorFactory implements MainFactory {

    private final ArrayList<String> labels;

    private static ArrayList<Control> inputs;

    public MonitorFactory() {
        labels = new ArrayList<>();
        Class<?> MonitorClass = Monitor.class;
        for (Method method : Arrays.stream(MonitorClass.getMethods()).filter(x -> x.getName().startsWith("get")).toList()) {
            if (method.isAnnotationPresent(Name.class))
                labels.add(method.getAnnotation(Name.class).value());
        }
    }

    @Override
    public boolean checkInputs() {
        return Check.checkInputs(inputs, labels, Monitor.class);
    }

    @Override
    public PCComponent getComponent() {
        Monitor monitor = new Monitor();
        HashMap<String,String> typesMap = Maps.getMapOfTypes(Monitor.class);
        HashMap<String,Method> settersMap = Maps.getMapOfSettersOrGetters("set", Monitor.class);
        createInstance(labels, inputs, settersMap, typesMap, monitor);
        return monitor;
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
        Monitor monitor = (Monitor) component;
        HashMap<String, Method> map = Maps.getMapOfSettersOrGetters("get", Monitor.class);
        GUI.putInfoToInputs(labels, inputs, map, monitor);
    }
}
