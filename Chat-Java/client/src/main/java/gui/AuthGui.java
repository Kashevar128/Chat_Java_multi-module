package gui;

import clientlogic.DataBase;
import clientlogic.Launch;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AuthGui {
    private Stage stage;
    AuthController authController;

    public AuthGui() throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/auth.fxml"));
        Parent auth = loader.load();
        authController = loader.getController();

        authController.RemoteIP.setSelected(false);
        authController.MySQL.setSelected(false);
        authController.MySQL.setOnAction(event -> {DataBase.setMySql(authController.MySQL.isSelected());});
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
