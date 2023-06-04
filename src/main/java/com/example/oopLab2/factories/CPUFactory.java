package com.example.oopLab2.factories;

import com.example.oopLab2.annotation.Name;
import com.example.oopLab2.hierarchy.CPU;
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

public class CPUFactory implements MainFactory{

    private final ArrayList<String> labels;

    private static ArrayList<Control> inputs;

    public CPUFactory() {
        labels = new ArrayList<>();
        Class<?> CPUClass = CPU.class;
        for (Method method : Arrays.stream(CPUClass.getMethods()).filter(x -> x.getName().startsWith("get")).toList()) {
            if (method.isAnnotationPresent(Name.class))
                labels.add(method.getAnnotation(Name.class).value());
        }
    }


    @Override
    public boolean checkInputs() {
        return Check.checkInputs(inputs, labels, CPU.class);
    }

    @Override
    public PCComponent getComponent() {
        CPU cpu = new CPU();
        HashMap<String,Class<?>> typesMap = Maps.getMapOfTypes(CPU.class);
        HashMap<String,Method> settersMap = Maps.getMapOfSettersOrGetters("set", CPU.class);
        createInstance(labels, inputs, settersMap, typesMap, cpu);
        return cpu;
    }

    @Override
    public ArrayList<Control> getInputs() {
        return inputs;
    }

    @Override
    public void createInputs(HBox container) {
        inputs = GUI.createLabelsAndInputs(container, labels, Maps.getMapOfTypes(CPU.class), CPU.class);
    }

    @Override
    public void fillInputs(PCComponent component, ArrayList<Label> labels) {
        CPU cpu = (CPU) component;
        HashMap<String, Method> map = Maps.getMapOfSettersOrGetters("get", CPU.class);
        GUI.putInfoToInputs(labels, inputs, map, cpu);
    }
}
