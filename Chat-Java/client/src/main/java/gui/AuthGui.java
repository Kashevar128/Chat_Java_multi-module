package gui;

import clientlogic.DataBase;
import clientlogic.Launch;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class AuthGui {
    private Stage stage;
    AuthController authController;
    private static File memoryFile;

    public static File getMemoryFile() {
        return memoryFile;
    }

    public AuthGui() throws Exception {

        memoryFile = new File("Chat-java/client/src/main/resources/memory.txt");
        if (!memoryFile.exists()) {
            try {
                memoryFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/auth.fxml"));
        Parent auth = loader.load();
        authController = loader.getController();

        authController.RemoteIP.setSelected(false);
        authController.MySQL.setSelected(false);
        authController.IP_server.setDisable(true);
        authController.PORT_server.setDisable(true);
        if(!AuthController.isFileEmpty(memoryFile)) {
            String[] arr = authController.decodeFile();
            authController.IP_server.setText(arr[0]);
            authController.PORT_server.setText(arr[1]);
        }

        authController.RemoteIP.setOnAction(event -> {
            authController.select(authController.RemoteIP.isSelected());
        });
        authController.MySQL.setOnAction(event -> {
            DataBase.setMySql(authController.MySQL.isSelected());
        });
        stage = new Stage();
        stage.setTitle("Авторизация");
        stage.setScene(new Scene(auth));
        stage.setResizable(false);
        stage.show();

        new Thread(() -> {
            try {
                Thread.sleep(180000);
                Platform.runLater(() -> {
                    stage.close();
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        stage.setOnCloseRequest(event -> {
            if (!WarningAlertExample.getWarningExit()) {
                event.consume();
                return;
            }
            Launch.exitClient();
        });

    }
}

