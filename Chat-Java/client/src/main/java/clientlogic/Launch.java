package clientlogic;

import gui.AuthGui;
import javafx.application.Platform;

import java.io.File;
import java.io.IOException;

public class Launch {
    private static File file;

    public static File getFile() {
        return file;
    }

    public static void setFile(File file) {
        Launch.file = file;
    }

    public static void main(String[] args) {
        running();
    }

    private static void running() {
//        setFile(new File("Chat-Java/client/src/main/resources/values/run.txt"));
//        if (!file.exists()) {
//            try {
//                file.createNewFile();
//                Platform.startup(() -> new AuthGui());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else return;

        Platform.startup(()->new AuthGui());

    }

    public static void exitClient() {
//        Launch.getFile().delete();
        System.exit(0);
    }


}
