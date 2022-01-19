package server;

import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ChatServerController {

    private EventHandler<WindowEvent> closeEventHandler = new EventHandler<WindowEvent>() {
        @Override
        public void handle(WindowEvent event) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ChatServer.getFileReservServer()))) {
                oos.writeObject(ChatServer.getMessages());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    public EventHandler<WindowEvent> getCloseEventHandler() {
        return closeEventHandler;
    }
}
