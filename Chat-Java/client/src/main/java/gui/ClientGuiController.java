package gui;

import clientlogic.Client;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.apache.commons.lang3.StringUtils;
import network.ClientProfile;
import network.Message;

import java.util.ArrayList;

public class ClientGuiController {
    @FXML
    public ListView<HBox> output;
    public TextArea input;
    public TextField search;
    public Label name;
    public ListView listUsers;
    public ImageView yourAvatar;

    private Client client;


    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public void msgProcessing() {
        String msg = StringUtils.strip(input.getText());
        if (!filter(msg)) {
            input.clear();
            return;
        }
        input.clear();
        getClient().onSendPackage(client.getConnection(), msg + "\uD83D\uDE00");
    }

    public static boolean filter(String msg) {
        boolean flag = !msg.equals("");
        return flag;
    }

    public void print(final Message msg) {
        Platform.runLater(() ->
                output.getItems().add(new HBoxChat(msg))
        );
    }



    public void printListUsers(ArrayList<ClientProfile> usersList) {
        ArrayList<HBoxChat> hBoxChats = new ArrayList<>();
        for (ClientProfile userProfile : usersList) {
            if(!userProfile.getNameUser().equals(client.getLoginUser())) {
                hBoxChats.add(new HBoxChat(userProfile));
            }
        }
        Platform.runLater(() -> {
            listUsers.getItems().clear();
            listUsers.getItems().addAll(hBoxChats);
        });

    }

    public void updateOutput(ArrayList<Message> msgList) {
        ArrayList<HBoxChat> hBoxChats = new ArrayList<>();
        for(Message message : msgList) {
            hBoxChats.add(new HBoxChat(message));
        }
        Platform.runLater(() -> {
            output.getItems().clear();
            output.getItems().addAll(hBoxChats);
        });
    }
}
