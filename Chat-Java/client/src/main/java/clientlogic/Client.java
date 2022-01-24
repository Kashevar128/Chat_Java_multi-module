package clientlogic;

import gui.*;
import javafx.application.Platform;
import javafx.scene.image.Image;
import network.*;

import java.io.File;
import java.io.IOException;

import java.net.InetAddress;
import java.net.SocketException;

import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client implements TCPConnectionListener { // делаем наследоваие от JFrame и осуществляем интерфейсы ActionListener и TCPConnectionListener

    private static String IP_ADDR;// 192.168.0.104 - доп. IP // Переменная c IP машины
    private ArrayList<ClientProfile> usersList;
    private ArrayList<Message> messagesList;
    private boolean correctShutdown;
    org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Client.class);

    static {
        try {
            IP_ADDR = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private static final int PORT = 8189; // Переменная с портом

    private ClientGuiController controller;
    private TCPConnection connection; // Поле для экземпляра канала
    private String loginUser;
    private ClientProfile myClientProfile;
    private ClientGui clientGui;
    private Image ava;


    public Client(ClientGuiController controller, String name, ClientGui clientGui) throws IOException {

        this.clientGui = clientGui;
        this.controller = controller;
        loginUser = name;
        controller.name.setText(name);

        byte[] byteAva = clientGui.getAvatar();
        ava = HBoxChat.decodeByteToImage(byteAva);
        this.myClientProfile = new ClientProfile(loginUser, byteAva);

        Platform.runLater(() -> {
            controller.yourAvatar.setImage(ava);
        });

        connection = connect();

    }

    public TCPConnection getConnection() {
        return connection;
    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) { // Расписываем интерфейсы для работы со стороны клиента, методы синхронизировать не надо, т.к. с ними работаем только сам клиент
        System.out.println("Connection ready...");
        Message pack = new Message(myClientProfile, null, TypeMessage.SERVICE_MESSAGE_AUTHORIZATION);
        connection.sendMessage(pack);
    }

    @Override
    public void onReceivePackage(TCPConnection tcpConnection, Message msg) {
        messageHandler(msg, msg.getTypeMessage());
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) throws SocketException {
        if (isCorrectShutdown()) {
            System.out.println("Корректное завершение работы.");
            connection.disconnect();
            System.exit(0);
        }
        System.out.println("Сервер упал.");
        Platform.runLater(() -> {
            connection = connect();
            InformationAlertExample.getInformationConnectionComplete();
        });
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onSendPackage(TCPConnection tcpConnection, String msg) {
        Message pack = new Message(msg, myClientProfile);
        connection.sendMessage(pack);
    }

    @Override
    public void messageHandler(Message msg, TypeMessage typeMessage) {
        switch (typeMessage) {
            case VERBAL_MESSAGE:
                inOrOutSorter(msg);
                controller.print(msg);
                break;
            case SERVICE_MESSAGE_UPDATE_LIST_USERS:
                usersList = (ArrayList<ClientProfile>) msg.getObjT();
                controller.printListUsers(getUsersList());
                break;
            case SERVICE_MESSAGE_UPDATE_DIALOGUES:
                messagesList = (ArrayList<Message>) msg.getObjT();
                for (Message message : messagesList) {
                    inOrOutSorter(message);
                }
                controller.updateOutput(getMessagesList());
        }
    }

    public String getLoginUser() {
        return loginUser;
    }

    public ArrayList<ClientProfile> getUsersList() {
        return usersList;
    }

    public ClientProfile getMyClientProfile() {
        return myClientProfile;
    }

    public ClientGui getClientGui() {
        return clientGui;
    }

    private void inOrOutSorter(Message msg) {
        if (msg.getProfile().getNameUser().equals(getLoginUser())) {
            msg.setInOrOut(false);
        } else msg.setInOrOut(true);
    }

    private TCPConnection connect() {
        boolean connectionCompleted = false;
        while (!connectionCompleted) {
            try {
                if (connection != null) {
                    ErrorAlertExample.getErrorConnection();
                    ErrorAlertExample.getErrorConnectionDialog();
                    connection.disconnect();
                    connection = null;
                }
                connection = new TCPConnection(IP_ADDR, PORT, this);
                connectionCompleted = true;
            } catch (IOException e) {
                ErrorAlertExample.getErrorConnection();
                ErrorAlertExample.getErrorConnectionDialog();
            }
        }
        return connection;
    }

    public boolean isCorrectShutdown() {
        return correctShutdown;
    }

    public void setCorrectShutdown(boolean correctShutdown) {
        this.correctShutdown = correctShutdown;
    }

    public ArrayList<Message> getMessagesList() {
        return messagesList;
    }
}
