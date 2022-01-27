package gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class WarningAlertExample {

    public static void getWarningRepeatUser() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Такой пользователь уже существует.");
        alert.setContentText("Нажмите ОК для продолжения.");

        alert.showAndWait();
    }

    public static void getWarningLoginOrPasswordFalse() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Что-то не так с вводимыми данными. Проверьте и введите еще раз))");
        alert.setContentText("Нажмите ОК для продолжения.");

        alert.showAndWait();
    }

    public static void getWarningBigLenght() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Длина логина и пароля не должна превышать 30 символов");
        alert.setContentText("Нажмите ОК для продолжения.");

        alert.showAndWait();
    }

    public static void getWarningIsLogged() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Вы уже вошли в систему");
        alert.setContentText("Нажмите ОК для продолжения.");

        alert.showAndWait();
    }

    public static boolean getWarningExit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Выход");
        alert.setHeaderText("Вы точно хотите выйти из приложения?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    public static void getWarningDoubleUser() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Вы уже вошли в систему.");

        Optional<ButtonType> result = alert.showAndWait();

    }

}
