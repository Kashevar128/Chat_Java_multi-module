package gui;

import javafx.scene.control.Alert;

public class WarningAlertExample {

    public static void getWarningRepeatUser() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Такой пользователь уже существует.");
        alert.setContentText("Нажмите ОК для продолжения.");

        alert.showAndWait();
    }

    public static void getWarningLoginOrPasswordFalse() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Неправильное имя или пароль.");
        alert.setContentText("Нажмите ОК для продолжения.");

        alert.showAndWait();
    }

    public static void getWarningBigLenght() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Длина логина и пароля не должна превышать 30 символов");
        alert.setContentText("Нажмите ОК для продолжения.");

        alert.showAndWait();
    }

}
