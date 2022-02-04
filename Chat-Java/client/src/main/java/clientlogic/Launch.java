package clientlogic;

import gui.AuthGui;
import javafx.application.Platform;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Launch {
    private static File file;

    private static AuthGui authGui;

    public static File getFile() {
        return file;
    }

    public static void setFile(File file) {
        Launch.file = file;
    }

    public static void main(String[] args) {
        running();
    }

    public static void running() {
        setFile(new File("run.txt"));
        if (!getFile().exists()) {
            try {
                getFile().createNewFile();
                Platform.startup(() -> {
                    try {
                        authGui = new AuthGui();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public static void exitClient() {
        Launch.getFile().delete();
        try {
            DataBase.getInstance().updateStatus(authGui.getAuthController().login.getText(), false);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
