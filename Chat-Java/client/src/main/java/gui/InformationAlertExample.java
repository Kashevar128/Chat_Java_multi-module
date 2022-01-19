package gui;

import javafx.scene.control.Alert;

public class InformationAlertExample {

    public static void getInformationConnectionComplete() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Попытка соединения.");
        alert.setHeaderText("Связь с сервером установлена");
        alert.setContentText("Нажмите ОК для продолжения.");

        alert.showAndWait();
    }

    public static void getInformationAuthComplete(String name) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Здравствуйте, " + name + "!");
        alert.setContentText("Нажмите ОК для продолжения.");

        alert.showAndWait();
    }

    public static void getInformationRegistrationComplete(String name) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Добро пожаловать, " + name + "!");
        alert.setContentText("Нажмите ОК для продолжения.");

        alert.showAndWait();
    }

}
