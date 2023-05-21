package com.example.oopLab2.factories;

import com.example.oopLab2.annotation.Name;
import com.example.oopLab2.hierarchy.GraphicsCard;
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

public class GraphicsCardFactory implements MainFactory {

    private final ArrayList<String> labels;

    private static ArrayList<Control> inputs;

    public GraphicsCardFactory() {
        labels = new ArrayList<>();
        Class<?> GraphicsCardClass = GraphicsCard.class;
        for (Method method : Arrays.stream(GraphicsCardClass.getMethods()).filter(x -> x.getName().startsWith("get")).toList()) {
            if (method.isAnnotationPresent(Name.class))
                labels.add(method.getAnnotation(Name.class).value());
        }
    }

    @Override
    public boolean checkInputs() {
        return Check.checkInputs(inputs, labels, GraphicsCard.class);
    }

    @Override
    public PCComponent getComponent() {
        GraphicsCard graphicsCard = new GraphicsCard();
        HashMap<String,String> typesMap = Maps.getMapOfTypes(GraphicsCard.class);
        HashMap<String,Method> settersMap = Maps.getMapOfSettersOrGetters("set", GraphicsCard.class);
        createInstance(labels, inputs, settersMap, typesMap, graphicsCard);
        return graphicsCard;
    }

    @Override
    public ArrayList<Control> getInputs() {
        return inputs;
    }

    @Override
    public void createInputs(HBox container) {
        inputs = GUI.createLabelsAndInputs(container, labels, Maps.getMapOfTypes(GraphicsCard.class), GraphicsCard.class);
    }

    @Override
    public void fillInputs(PCComponent component, ArrayList<Label> labels) {
        GraphicsCard graphicsCard = new GraphicsCard();
        HashMap<String, Method> map = Maps.getMapOfSettersOrGetters("get", GraphicsCard.class);
        GUI.putInfoToInputs(labels, inputs, map, graphicsCard);
    }
}
