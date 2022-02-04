package gui;

import clientlogic.Client;
import clientlogic.DataBase;
import javafx.application.Platform;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import network.Message;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class AuthController {

    public TextField login;
    public TextField password;
    private String strLogin;
    private String strPassword;
    public CheckBox RemoteIP;
    public CheckBox MySQL;
    public TextField IP_server;
    public TextField PORT_server;
    private String IP_serverStr;
    private String PORT_serverStr;
    private static AuthController authController;

    public AuthController() {
        authController = this;
    }

    public void enter() throws Exception {

        setStrLogin(login.getText());
        setStrPassword(password.getText());

        if(AuthGui.isRemoteServer()) {
            setIP_serverStr(IP_server.getText());
            setPORT_serverStr(PORT_server.getText());
            String[] str = new String[] {getIP_serverStr(), getPORT_serverStr()};
            codeFile(str);
        }
        boolean filter01 = ClientGuiController.filter(getStrLogin());
        boolean filter02 = ClientGuiController.filter(getStrPassword());
        if (!filter01 || !filter02) {
            login.clear();
            password.clear();
            return;
        }

        boolean auth = DataBase.getInstance()
                .auth(getStrLogin(), getStrPassword());
        if (auth) {
            login.getScene().getWindow().hide();
            Platform.runLater(() -> {
                try {
                    new ClientGui(getStrLogin(), DataBase.getAvatar(getStrLogin()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } else {
//            login.clear();
//            password.clear();
            WarningAlertExample.getWarningLoginOrPasswordFalse();
        }
    }

    public void reg() throws Exception {
        login.getScene().getWindow().hide();
        new RegGui();
    }

    public void select(boolean flag) {
        Client.setRemote(flag);
        AuthGui.setRemoteServer(flag);
        if (flag) {
            IP_server.setDisable(false);
            PORT_server.setDisable(false);
        } else {
            IP_server.setDisable(true);
            PORT_server.setDisable(true);
        }
    }

    public String getStrLogin() {
        return strLogin;
    }

    public static AuthController getAuthController() {
        return authController;
    }

    public String getStrPassword() {
        return strPassword;
    }

    public void setStrLogin(String strLogin) {
        this.strLogin = StringUtils.strip(strLogin);
    }

    public void setStrPassword(String strPassword) {
        this.strPassword = StringUtils.strip(strPassword);
    }

     public static boolean isFileEmpty(File file) {
        return file.length() == 0;
    }

    public void setIP_serverStr(String IP_serverStr) {
        this.IP_serverStr = StringUtils.strip(IP_serverStr);
    }

    public void setPORT_serverStr(String PORT_serverStr) {
        this.PORT_serverStr = StringUtils.strip(PORT_serverStr);
    }

    public String getIP_serverStr() {
        return IP_serverStr;
    }

    public String getPORT_serverStr() {
        return PORT_serverStr;
    }

    public void codeFile(String[] array) {
        System.out.println(array);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(AuthGui.getMemoryFile()))) {
            oos.writeObject(array);
            oos.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String[] decodeFile() {
        String[] array = new String[2];
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(AuthGui.getMemoryFile()))) {
            array = (String[]) ois.readObject();
            System.out.println("Извлеченный файл\n" + array);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return array;
    }


}
