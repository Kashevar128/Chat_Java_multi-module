package gui;

import clientlogic.DataBase;
import javafx.application.Platform;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;

public class AuthController {

    public TextField login;
    public TextField password;
    private String strLogin;
    private String strPassword;
    public CheckBox RemoteIP;
    public CheckBox MySQL;
    private static AuthController authController;

    public AuthController() {
        authController = this;
    }

    public void enter() throws Exception {

        setStrLogin(login.getText());
        setStrPassword(password.getText());

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
            login.clear();
            password.clear();
            WarningAlertExample.getWarningLoginOrPasswordFalse();
        }
    }

    public void reg() throws Exception {
        login.getScene().getWindow().hide();
        new RegGui();
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
}
