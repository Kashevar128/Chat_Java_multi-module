package gui;

import clientlogic.Launch;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AuthGui {
    private Stage stage;

    public AuthGui() {

        Parent auth = null;
        try {
            auth = FXMLLoader.load(getClass().getResource("/fxml/auth.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            if (!InformationAlertExample.getInformationExit()) {event.consume();}
            Launch.exitClient();
        });

    }
}
