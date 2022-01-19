package gui;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RegGui {
    private Stage stage;
    RegistrationController controller;

    public RegGui() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/registration.fxml"));
        Parent reg = loader.load();
        controller = loader.getController();

        stage = new Stage();
        stage.setTitle("Регистрация");
        stage.setScene(new Scene(reg));
        stage.setResizable(false);
        stage.show();

        stage.setOnCloseRequest(event -> {
            System.exit(0);
        });

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
    }


}
