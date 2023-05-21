package com.example.oopLab2;

import com.example.oopLab2.factories.*;
import com.example.oopLab2.hierarchy.*;
import com.example.oopLab2.tools.GUI;
import com.example.oopLab2.tools.Maps;
import com.example.oopLab2.tools.TableObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class HelloController {

    private final HashMap<String, MainFactory> mapOfFactories = new HashMap<>();
    private final ObservableList<com.example.oopLab2.tools.TableObject> tableObjects = FXCollections.observableArrayList();
    private final ArrayList<PCComponent> components = new ArrayList<>();

    private ArrayList<Control> inputs;
    private TableObject selectedRow;
    private boolean isUpdated = false;

    @FXML
    private TableColumn<TableObject, Integer> IdColumn;
    @FXML
    private TableColumn<TableObject, String> TypeColumn;
    @FXML
    private TableColumn<TableObject, String> BrandColumn;
    @FXML
    public TableColumn<TableObject, Integer> PriceColumn;
    public TableColumn<TableObject, String> ConnectionColumn;
    @FXML
    private TableView<TableObject> ObjectsTable;


    @FXML
    private Button UpdateBtn;

    @FXML
    private AnchorPane root;

    @FXML
    private VBox InputsVBox;

    @FXML
    private HBox LabelsAndInputsHBox;

    @FXML
    private VBox LabelsVBox;

    @FXML
    private Button AddBtn;

    @FXML
    private HBox ButtonsVBox;

    @FXML
    private ChoiceBox<String> ClassChoice;

    @FXML
    private VBox ContainerVBox;

    @FXML
    private Button DeleteBtn;


    ArrayList<Label> getLabels() {
        ArrayList<Label> labels = new ArrayList<>();
        for (int i = 0; i < LabelsVBox.getChildren().size(); i++) {
            labels.add((Label) LabelsVBox.getChildren().get(i));
        }
        return labels;
    }

    @FXML
    void onUpdateBtnClick(ActionEvent event) {
        if (!isUpdated) {
            ClassChoice.setValue(selectedRow.getType());
        }
        MainFactory factory = mapOfFactories.get(ClassChoice.getValue());
        inputs = factory.getInputs();
        ArrayList<Label> labels = getLabels();
        disableElements(isUpdated);
        if (!isUpdated) {
            factory.fillInputs(components.get(selectedRow.getIndex() - 1), getLabels());
        } else {
            if (factory.checkInputs()) {
                PCComponent temp = components.get(selectedRow.getIndex() - 1);
                components.set(selectedRow.getIndex() - 1, factory.getComponent());
                tableObjects.set(selectedRow.getIndex() - 1, new TableObject(selectedRow.getIndex(), ClassChoice.getValue(), factory.getComponent().getBrand(), factory.getComponent().getPrice(), factory.getComponent().getConnectionType()));
                GUI.clearInputs(inputs, labels, Maps.getMapOfTypes(temp.getClass()), temp.getClass());
                selectedRow = null;
                DeleteBtn.setDisable(true);
                UpdateBtn.setDisable(true);
                InputsVBox.setSpacing(5);
            } else {
                InputsVBox.setSpacing(3);
                isUpdated = !isUpdated;
                disableElements(isUpdated);
            }
        }
        isUpdated = !isUpdated;
    }

    void disableElements(boolean isUpdated) {
        AddBtn.setDisable(!isUpdated);
        DeleteBtn.setDisable(!isUpdated);
        ClassChoice.setDisable(!isUpdated);
        ObjectsTable.setDisable(!isUpdated);
    }

    @FXML
    void onDeleteBtnClick(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete object");
        alert.setHeaderText("Are you sure you want to delete the selected object?");
        alert.setContentText("This action cannot be returned.");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");

        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent())
            if (result.get() == yesButton) {
                components.remove(selectedRow.getIndex() - 1);
                tableObjects.remove(selectedRow.getIndex() - 1);
                for (int i = 0; i < components.size(); i++) {
                    tableObjects.get(i).setIndex(i + 1);
                }
                UpdateBtn.setDisable(true);
                DeleteBtn.setDisable(true);
            } else {
                alert.close();
            }
    }

    @FXML
    public void initialize() {
        initFactories();
        initGUI();
        initObjects();
    }


    public void onInstanceSelected(MouseEvent event) {
        if (event.getClickCount() == 1) {
            selectedRow = ObjectsTable.getSelectionModel().getSelectedItem();
            if (selectedRow != null) {
                UpdateBtn.setDisable(false);
                DeleteBtn.setDisable(false);
            }
        }
    }

    public void onAddBtnClick(ActionEvent event) {
        MainFactory mainFactory = mapOfFactories.get(ClassChoice.getValue());
        if (mainFactory.checkInputs()) {

            components.add(mainFactory.getComponent());
            tableObjects.add(new TableObject(components.size(), ClassChoice.getValue(), components.get(components.size() - 1).getBrand(), components.get(components.size() - 1).getPrice(), components.get(components.size() - 1).getConnectionType()));
            GUI.clearInputs(mainFactory.getInputs(), getLabels(), Maps.getMapOfTypes(mainFactory.getComponent().getClass()), mainFactory.getComponent().getClass());
            InputsVBox.setSpacing(5);
        } else {
            InputsVBox.setSpacing(3);
        }
    }

    private void initObjects() {
        components.add(new Monitor("Huawei", 1200, "HDMI", OutputInformationType.GraphicInfo, 24, 250));
        tableObjects.add(new TableObject(components.size(), "Monitor", "Huawei", 1200, "HDMI"));
        components.add(new Webcam("Sven", 50, "USB 2.0", InputInformationType.GraphicInfo, 30, 1.2));
        tableObjects.add(new TableObject(components.size(), "Webcam", "Sven", 50, "USB 2.0"));
        components.add(new CPU("Intel", 400, "LGA1200", 4,4,1,6, 2.9));
        tableObjects.add(new TableObject(components.size(), "CPU", "Intel", 400, "LGA1200"));
    }

    private void initGUI() {
        ClassChoice.getItems().addAll(mapOfFactories.keySet());
        ClassChoice.setOnAction(this::onClassChoice);
        ClassChoice.setValue("Webcam");

        mapOfFactories.get(ClassChoice.getValue()).createInputs(LabelsAndInputsHBox);

        IdColumn.setCellValueFactory(new PropertyValueFactory<>("index"));
        TypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        BrandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        PriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        ConnectionColumn.setCellValueFactory(new PropertyValueFactory<>("connection"));

        ObjectsTable.setItems(tableObjects);

        UpdateBtn.setDisable(true);
        DeleteBtn.setDisable(true);
    }

    private void onClassChoice(ActionEvent e) {
        mapOfFactories.get(ClassChoice.getValue()).createInputs(LabelsAndInputsHBox);
    }

    private void initFactories() {
        mapOfFactories.put("CPU", new CPUFactory());
        mapOfFactories.put("Graphics card", new GraphicsCardFactory());
        mapOfFactories.put("Headphones", new HeadphonesFactory());
        mapOfFactories.put("Monitor", new MonitorFactory());
        mapOfFactories.put("Webcam", new WebcamFactory());
    }
}