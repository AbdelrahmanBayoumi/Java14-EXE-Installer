package MainView;

import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    @FXML
    private TextField appName;
    @FXML
    private TextField Description;
    @FXML
    private TextField Vendor;
    @FXML
    private TextField Version;
    @FXML
    private TextField input;
    @FXML
    private TextField output;
    @FXML
    private TextField MainJar;
    @FXML
    private CheckBox DesktopShortcut;
    @FXML
    private CheckBox StartMenuShortcut;
    @FXML
    private TextField License;
    @FXML
    private TextField Icon;
    @FXML
    private CheckBox MenuGroupBox;
    @FXML
    private TextField MenuGroup;
    @FXML
    private VBox loading;
    @FXML
    private AnchorPane AP;

    FileChooser fil_chooser = new FileChooser();
    DirectoryChooser directoryChooser = new DirectoryChooser();
    SimpleBooleanProperty loadingProperty = new SimpleBooleanProperty(false);
    File fileSource;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MenuGroup.disableProperty().bind(MenuGroupBox.selectedProperty().not());
        loading.visibleProperty().bind(loadingProperty);
    }

    @FXML
    private void ClearAction(Event event) {
        System.out.println("ClearAction");
    }

    @FXML
    private void ExeAction(Event event) {
        System.out.println("ExeAction");
        try {
            if (!check()) {
                loadingProperty.set(true);
                new Thread(() -> {
                    try {
                        int res = cmdAction();
                        if (res == 0) {
                            System.out.println("success !");
                        } else {
                            System.out.println("error : " + res);
                            Thread.sleep(2000);
                            Platform.runLater(() -> {
                                Alert a = new Alert(Alert.AlertType.ERROR);
                                a.setContentText("Error");
                                a.showAndWait();
                            });
                        }
                        loadingProperty.set(false);
                    } catch (Exception e) {
                        System.out.println(e.getLocalizedMessage());
                    }
                }).start();
            } else {
                System.out.println("fill ");
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

    }

    public int cmdAction() throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String str = "jpackage"
                + " --name " + appName.getText()
                + "  --description " + "'" + Description.getText() + "'"
                + "  --vendor " + "'" + Vendor.getText() + "'"
                + "  --app-version " + Version.getText()
                + "  --input " + input.getText()
                + "  --dest " + output.getText()
                + "  --main-jar " + MainJar.getText()
                + "  --icon " + Icon.getText()
                + "  --main-jar " + MainJar.getText()
                + " --win-dir-chooser";
        str += (!License.getText().equals("")) ? " --license-file " + License.getText() : "";
        str += (!MenuGroupBox.isSelected()) ? " --win-menu-group " + MenuGroup.getText() : "";
        str += (!DesktopShortcut.isSelected()) ? " --win-shortcut " : "";
        str += (!StartMenuShortcut.isSelected()) ? "  --win-menu " : "";
        processBuilder.command("cmd.exe", "/c", str);
        Process process = processBuilder.start();
        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line + "\n");
        }
        System.out.println("s : " + str);
        System.out.println("output : " + output);
        return process.waitFor();
    }

    private boolean check() {
        return !"".equals(appName.getText()) && !Description.getText().equals("")
                && !Vendor.getText().equals("") && !Version.getText().equals("")
                && !input.getText().equals("") && !output.getText().equals("")
                && !MainJar.getText().equals("") && !Icon.getText().equals("")
                && (!MenuGroupBox.isSelected() || !MenuGroup.getText().equals(""));
    }

    @FXML
    private void inputAction(Event actionEvent) {
        fileSource = directoryChooser.showDialog(input.getScene().getWindow());
        if (fileSource != null) {
            input.setText(fileSource.getAbsolutePath());
        }
    }

    @FXML
    private void outputAction(Event actionEvent) {
        fileSource = directoryChooser.showDialog(output.getScene().getWindow());
        if (fileSource != null) {
            output.setText(fileSource.getAbsolutePath());
        }
    }

    @FXML
    private void LicenseAction(Event actionEvent) {
        fileSource = fil_chooser.showOpenDialog(License.getScene().getWindow());
        if (fileSource != null) {
            License.setText(fileSource.getAbsolutePath());
        }
    }

    @FXML
    private void IconAction(Event actionEvent) {
        fileSource = fil_chooser.showOpenDialog(Icon.getScene().getWindow());
        if (fileSource != null) {
            Icon.setText(fileSource.getAbsolutePath());
        }
    }
}
