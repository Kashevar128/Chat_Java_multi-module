package gui;

import clientlogic.Launch;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class ErrorAlertExample {


    public static void getErrorConnection() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка подключения.");
        alert.setHeaderText("Нет связи с сервером");
        alert.setContentText("Нажмите ОК для продолжения.");

        alert.showAndWait();
    }

    public static void getErrorConnectionDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Переподключение.");
        alert.setHeaderText("Попробовать переподключиться?");
        alert.setContentText("Нажмите ОК для переподключения или ОТМЕНА - для выходы из диалога.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return;
        }
        getErrorConnectionFatal();
        Launch.exitClient();
    }

    public static void getErrorConnectionFatal() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка подключения.");
        alert.setHeaderText("Не удалось установить связь");
        alert.setContentText("Нажмите ОК для выхода из приложения.");

        alert.showAndWait();

    }

    public static void getErrorConnectionSQLFatal() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка подключения.");
        alert.setHeaderText("Нет связи с базой данных.");
        alert.setContentText("Нажмите ОК для выхода из приложения.");

        alert.showAndWait();

    }

    public static void getErrorIncorrectData() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка подключения.");
        alert.setHeaderText("Не корректные данные IP и порт.");

        alert.showAndWait();
        Launch.exitClient();

    }

}

