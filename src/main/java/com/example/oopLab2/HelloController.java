package com.example.oopLab2;

import com.example.oopLab2.factories.*;
import com.example.oopLab2.factories.serializers.BinarySerializerFactory;
import com.example.oopLab2.factories.serializers.JSONSerializerFactory;
import com.example.oopLab2.factories.serializers.SerializerFactory;
import com.example.oopLab2.factories.serializers.TextSerializerFactory;
import com.example.oopLab2.hierarchy.*;
import com.example.oopLab2.serialize.Serializer;
import com.example.oopLab2.tools.CustomFileChooser;
import com.example.oopLab2.tools.GUI;
import com.example.oopLab2.tools.Maps;
import com.example.oopLab2.tools.TableObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.example.Plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class HelloController {

    private final HashMap<String, MainFactory> mapOfFactories = new HashMap<>();
    private final ObservableList<com.example.oopLab2.tools.TableObject> tableObjects = FXCollections.observableArrayList();
    private ArrayList<PCComponent> components = new ArrayList<>();
    private final HashMap<String, SerializerFactory> mapOfSerializers = new HashMap<>();
    private static HashMap<String, Plugin> plugins = new HashMap<>();

    private ArrayList<Control> inputs;
    private TableObject selectedRow;
    private boolean isUpdated = false;

    private File selectedFile;

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
    @FXML
    private ChoiceBox<String> pluginChoice;
    @FXML
    private Pane pluginPane;
    @FXML
    private Button btnConfirm;
    @FXML
    private Button btnCancel;


    ArrayList<Label> getLabels() {
        ArrayList<Label> labels = new ArrayList<>();
        for (int i = 0; i < LabelsVBox.getChildren().size(); i++) {
            labels.add((Label) LabelsVBox.getChildren().get(i));
        }
        return labels;
    }

    @FXML
    void onUpdateBtnClick() {
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
    void onDeleteBtnClick() {
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
        initSerializers();
        initGUI();
        initObjects();
        initPlugins();
    }

    private void initPlugins() {
        String path = "src/main/java/com/example/oopLab2/plugins";
        File dir = new File(path);
        for(File file : dir.listFiles()) {
            try {
                String pathToJar = file.getPath();
                JarFile jarFile = new JarFile(pathToJar);
                Enumeration<JarEntry> entry = jarFile.entries();

                URL[] urls = {new URL("jar:file:" + pathToJar + "!/")};
                URLClassLoader cl = URLClassLoader.newInstance(urls);

                while (entry.hasMoreElements()) {
                    JarEntry jarEntry = entry.nextElement();
                    if (jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")) {
                        continue;
                    }
                    String className = jarEntry.getName().substring(0, jarEntry.getName().length() - 6);
                    className = className.replace('/', '.');
                    Plugin plugin = (Plugin) cl.loadClass(className).getConstructor().newInstance();
                    plugins.put(plugin.getName(), plugin);
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Не удалось загрузить плагины").showAndWait();
                e.printStackTrace(); // Вывод стека вызова исключения
            }
        }

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

    public void onAddBtnClick() {
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

    private void initSerializers() {
        mapOfSerializers.put(".bin", new BinarySerializerFactory());
        mapOfSerializers.put(".json", new JSONSerializerFactory());
        mapOfSerializers.put(".txt", new TextSerializerFactory());
    }

    public void onMenuClick() {
        selectedRow = null;
        UpdateBtn.setDisable(true);
        DeleteBtn.setDisable(true);
    }

    public void onFileOpenClick() throws JsonProcessingException {
        try {
            FileChooser chooser = new FileChooser();
            List<String> extensions = new ArrayList<>();
            for (String extension : mapOfSerializers.keySet()) {
                String info = mapOfSerializers.get(extension).getSerializer().getExtension() + "(*" + extension;
                extensions.clear();
                extensions.add("*" + extension);
                for(String name : plugins.keySet()) {
                    extensions.add("*" + extension + plugins.get(name).getExtension());
                    info = info + ", *" + plugins.get(name).getExtension();
                }
                info = info + ")";
                chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                        info, extensions));
                extensions.remove("*" + extension);
            }

            File file = chooser.showOpenDialog(HelloApplication.stStage);
            if(file != null) {
                byte[] data = null;
                String extension = file.getName().substring(file.getName().lastIndexOf("."));
                byte[] bytes = Files.readAllBytes(file.toPath());
                if(isEncoded(file.getName())) {
                    data = decript(bytes, file);
                    extension = file.getName().substring(0, file.getName().lastIndexOf("."));
                    extension = extension.substring(extension.lastIndexOf("."));
                } else {
                    data = bytes;
                }
                SerializerFactory serializerFactory = mapOfSerializers.get(extension);

                components = mapOfSerializers.get(extension).getSerializer().deserialize(data);
                tableObjects.clear();
                for (int i = 0; i < components.size(); i++) {
                    tableObjects.add(new TableObject(i + 1, components.get(i).getClass().getSimpleName(), components.get(i).getBrand(), components.get(i).getPrice(), components.get(i).getConnectionType()));
                }
//                if(isEncoded(file.getName())) {
//                    FileOutputStream stream = new FileOutputStream(file);
//                    stream.write(data);
//                    stream.close();
//                }
            }
        } catch (Exception e){
            System.out.println("file not found");
        }
//        deserialize();
    }

    private byte[] decript(byte[] bytes, File file) {
            Plugin plugin = null;
            for(String name : plugins.keySet()) {
                if(plugins.get(name).getExtension().equals(file.getName().substring(file.getName().lastIndexOf(".")))){
                    plugin = plugins.get(name);
                    break;
                }
            }
            return plugin.decrypt(bytes);
    }

    private void deserialize() throws IOException {
        selectedFile = CustomFileChooser.getOpenFile();
        if (selectedFile != null) {
            String ext = getExtension(selectedFile.getPath());
            byte[] bytes = Files.readAllBytes(selectedFile.toPath());
            components = mapOfSerializers.get(ext).getSerializer().deserialize(bytes);

            tableObjects.clear();
            for (int i = 0; i < components.size(); i++) {
                tableObjects.add(new TableObject(i + 1, components.get(i).getClass().getSimpleName(), components.get(i).getBrand(), components.get(i).getPrice(), components.get(i).getConnectionType()));
            }
        }
    }

    private String getExtension(String path) {
        String[] parts = path.split("\\.");
        return parts[parts.length - 1];
    }

    public void onFileSaveClick() {
        showPluginPane();
    }

    private void showPluginPane() {
        pluginChoice.getItems().clear();
        pluginChoice.getItems().add("None");
        pluginChoice.setValue("None");
        for(String plugin : plugins.keySet()) {
            pluginChoice.getItems().add(plugin);
        }
        pluginPane.setVisible(true);
    }

    private void serialize() {
        selectedFile = CustomFileChooser.getSaveFile();
        byte[] bytes = null;
        if (selectedFile != null) {
            String ext = getExtension(selectedFile.getPath());
            bytes = mapOfSerializers.get(ext).getSerializer().serialize(components);
        }
        try (FileOutputStream fos = new FileOutputStream(selectedFile)) {
            fos.write(bytes);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while saving bytes to file");
            alert.setContentText("Check file info.");
            alert.showAndWait();
        }
    }

    public void onConfirm(MouseEvent event) {
        try {
            FileChooser chooser = new FileChooser();
            for(String extension : mapOfSerializers.keySet()) {
                String extensions = "*" + extension;
                String info = mapOfSerializers.get(extension).getSerializer().getExtension() + "(*" + extension;
                if(!pluginChoice.getValue().equals("None")) {
                    extensions = extensions + plugins.get(pluginChoice.getValue()).getExtension();
                    info = info + plugins.get(pluginChoice.getValue()).getExtension();
                }
                info = info + ")";
                chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                        mapOfSerializers.get(extension).getSerializer().getExtension(), extensions));
            }
            File file = chooser.showOpenDialog(HelloApplication.stStage);
            if(file != null) {
                String extension = file.getName().substring(file.getName().lastIndexOf("."));
                if(isEncoded(file.getName())) {
                    extension = file.getName().substring(0, file.getName().lastIndexOf("."));
                    extension = extension.substring(extension.lastIndexOf("."));
                }
                SerializerFactory serializerFactory = mapOfSerializers.get(extension);
                byte[] bytes = serializerFactory.getSerializer().serialize(components);
                Serializer serializer = serializerFactory.getSerializer();
                char[] charArray = new String(bytes, StandardCharsets.UTF_8).toCharArray(); //// to delete
                if(!pluginChoice.getValue().equals("None")) {
                    encrypt(bytes, file);
                } else {
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        fos.write(bytes);
                        fos.close();
                    } catch (IOException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error while saving bytes to file");
                        alert.setContentText("Check file info.");
                        alert.showAndWait();
                    }
                }
                hidePluginPane();
            }
        } catch (Exception e) {
            System.out.println("file not found");
        }
//        serialize();
    }

    private void encrypt(byte[] bytes, File file) {
        try {
            byte[] data = plugins.get(pluginChoice.getValue()).encrypt(bytes);

            FileOutputStream output = new FileOutputStream(file);
            output.write(data);
            output.close();
        } catch (FileNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Не удалось применить плагин").showAndWait();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Не удалось применить плагин").showAndWait();
        }
    }

    private boolean isEncoded(String name) {
        for(String plugin : plugins.keySet()){
            String plugExt = plugins.get(plugin).getExtension();
            String nameExt = name.substring(name.lastIndexOf("."));
            if(plugExt.equals(nameExt)) return true;
        }
        return false;
    }

    public void hidePluginPane() {
        pluginPane.setVisible(false);
    }
}