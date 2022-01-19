package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AuthGui extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent auth = FXMLLoader.load(getClass().getResource("/fxml/auth.fxml"));
        primaryStage.setTitle("Авторизация");
        primaryStage.setScene(new Scene(auth));
        primaryStage.setResizable(false);
        primaryStage.show();

        new Thread(() -> {
            try {
                Thread.sleep(180000);
                Platform.runLater(() -> {
                    primaryStage.close();
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });

    }
}
