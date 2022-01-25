package gui;

import clientlogic.Client;
import clientlogic.Launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;


public class ClientGui {
    private Stage stage;
    private ClientGuiController controller;
    private Client client;
    private String nameUser;
    private byte[] avatar;

    public ClientGui(String name, byte[] avatar) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/chatWork.fxml"));
        Parent chat = loader.load();
        controller = loader.getController();
        nameUser = name;
        this.avatar = avatar;
        controller.input.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                controller.msgProcessing();
            }
        });

        stage = new Stage();
        stage.setTitle("Сетевой чат");
        stage.setScene(new Scene(chat));
        stage.setResizable(false);
        controller.input.setWrapText(true);
        stage.show();

        client = new Client(controller, nameUser, this);
        controller.setClient(client);

        stage.setOnCloseRequest(event -> {
            if (!InformationAlertExample.getInformationExit()) {event.consume();}
            System.out.println("Клиент закрыт");
            try {
                client.setCorrectShutdown(true);
                client.getConnection().disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
//            Launch.getFile().delete();
         if (!InformationAlertExample.getInformationExit()) {event.consume();}
        });
    }

    public String getNameUser() {
        return nameUser;
    }

    public Stage getStage() {
        return stage;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public Client getClient() {
        return client;
    }
}
