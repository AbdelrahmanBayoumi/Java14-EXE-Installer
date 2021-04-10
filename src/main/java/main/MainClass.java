package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import util.Logger;

public class MainClass extends Application {
    private Long startTime;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        Logger.init();
        startTime = System.currentTimeMillis();
        Logger.info("App Launched");
    }

    @Override
    public void stop() throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                Long exitTime = System.currentTimeMillis();
                Logger.info("App closed - Used for "
                        + (exitTime - startTime) + " ms\n");
            }
        });
    }

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/MainView/MainView.fxml"));
            Scene scene = new Scene(root);
//            scene.getStylesheets().add("");
//            stage.getIcons().add(new Image(""));
            stage.setTitle("EXE Installer");
            stage.setScene(scene);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.error(null, ex, getClass().getName());
        }
    }

        /*
  jpackage
* --name JFX-Version
*  --description "JFX-Version"
* --vendor "Bayoumi"
* --app-version 1.1
* --input input
* --dest output
* --main-jar JFX-Version-1.0-SNAPSHOT.jar
* --win-dir-chooser
* --win-shortcut
* --win-menu
* --win-menu-group "Bayoumi"
* --icon "D:\Testt\input\icon.ico"
* --license-file "D:\Testt\input\license.txt"
* */
}
