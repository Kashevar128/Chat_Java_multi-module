package server;

import network.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;

import static network.TypeMessage.*;

public class ChatServer extends JFrame implements TCPConnectionListener, ActionListener { //Создаем класс ChatServer реализуем интерфейс для переопределения его методов

    public static void main(String[] args) {
        new ChatServer();
    } //Создание нового объекта класса ChatServer

    private static final int WIDTH = 600; // Переменная с шириной окна
    private static final int HEIGHT = 400; // Переменная с высотой окна
    private final ArrayList<TCPConnection> connections = new ArrayList<>(); // Создание коллекцию для создающихся TCP - соединений
    private final ArrayList<ClientProfile> usersProfiles = new ArrayList<>();
    private static ArrayList<Message> messages;
    private final JTextArea textArea = new JTextArea(); // Создаем поле, которое будет отражать диалоги
    private final JTextField fieldNickname = new JTextField("Admin"); // Поле с ником пользователя
    private final JTextField fieldInput = new JTextField(); // Поле для ввода сообщений
    private TCPConnection connection = null;
    private static final String NAME_SERVER = "Admin";
    private ClientProfile serverProfile;
    private static File fileReservServer;
    private Thread saveThread;

    private ChatServer() { // Конструктор класса
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // Функция для закрытия окна при нажатии на крестик

        setSize(WIDTH, HEIGHT); // Введение размеров окна
        setLocationRelativeTo(null); // Расположение окна по середине экрана
        setAlwaysOnTop(true); // Расположение в верхней части экрана
        textArea.setEditable(false); // Запрет на редактирование диалогового окна
        textArea.setLineWrap(true); // Автоматический перенос строк
        fieldInput.addActionListener(this); // Добавить на поле событие при нажатии Enter
        add(textArea, BorderLayout.CENTER); // Добавляем диалоговое поле на окно клиента (с типом размещения BorderLayout) по центру
        add(fieldInput, BorderLayout.SOUTH); // Добавляем поле ввода сообщений на юг окна клиента
        add(fieldNickname, BorderLayout.NORTH); // Добавляем поле никнейма на север окна клиента
        setResizable(false);

        this.addWindowListener(new WindowListener() {
            @Override
            public void windowClosing(WindowEvent e) {
//                sortedForDate(getMessages());
                codeReservFile(getMessages());
                saveThread.interrupt();
            }

            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosed(WindowEvent e) {
//                sortedForDate(getMessages());
                codeReservFile(getMessages());
                saveThread.interrupt();
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });

        setVisible(true); //Пишем - показать окно

        this.serverProfile = new ClientProfile(NAME_SERVER, null);

        File file = new File("Chat-java/server/src/main/resources/reserve.txt");
        fileReservServer = file;
        if (!fileReservServer.exists()) {
            try {
                fileReservServer.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (isFileEmpty(getFileReservServer())) {
            setMessages(new ArrayList<>());
        } else setMessages(decodeReservFile());

        saveThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(30000);
                    if(!getMessages().isEmpty()) {
//                        sortedForDate(getMessages());
                        codeReservFile(getMessages());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        saveThread.start();


        printMsg("Server running..."); // Консоль - запуск сервера
        printMsg("You have to wait connection");
        try (ServerSocket serverSocket = new ServerSocket(8189)) { // Создание сервер - сокета на порте 8189, слушающего клиента в try с ресурсами
            while (true) {
                connection = new TCPConnection(serverSocket.accept(), this); // В переменную connection закидываем инстанс
            }
        } catch (IOException e) {
            printMsg("Client kicked");
        }
    }

    private void sendToAllConnections(Message msg) { // Метод для рассылки сообщений всем соединениям сразу
        for (TCPConnection tcpConnection : connections) {
            tcpConnection.sendMessage(msg); // Вызываем для каждого метод отправки сообщения класса TCPConnection
        }
    }

    private synchronized void printMsg(String msg) {// Метод для выведения сообщения в поле диалога
        SwingUtilities.invokeLater(() -> {                                      // В отдельном потоке, т.к. так как есть возможность одновременного обращения к методу из нескольких мест в программе
            textArea.append(msg + "\n");                                        // Добавление сообщения
            textArea.setCaretPosition(textArea.getDocument().getLength());      // Переведение каретки в конец строки после сообщения, чтобы иметь пустую строку между сообщениями.
        });
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) { //Синхронизируем все методы tcpConnection, т.к. одними и теми же методами могут пользоваться несколько потоков
        connections.add(tcpConnection); // Добавляем в коллекцию соединений новое соединение
        printMsg("Client connected: " + tcpConnection); // Делаем рассылку все подключенным пользователям о создании нового соединения с клиентом
        printMsg(connections.toString());
    }

    @Override
    public synchronized void onReceivePackage(TCPConnection tcpConnection, Message msg) {
        messageHandler(msg, msg.getTypeMessage());
        System.out.println(tcpConnection.getClientProfile().getNameUser());
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection); // Удаление соединения из коллекции соединений
        usersProfiles.remove(tcpConnection.getClientProfile());
        Message messageUpdateList1 = new Message<>(usersProfiles, null, SERVICE_MESSAGE_UPDATE_LIST_USERS);
        if (!connections.isEmpty()) {
            sendToAllConnections(messageUpdateList1);
        }
        printMsg(usersProfiles.toString());
        printMsg("Client disconnected: " + tcpConnection);
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        printMsg("TCPConnection exception: " + e); // Исключение для соединения
    }

    @Override
    public void onSendPackage(TCPConnection tcpConnection, String stringMsg) {
        if (stringMsg.equals("")) return; // Если переменная равна пустому месту, делаем возврат из метода
        fieldInput.setText(null); // Передаем null в поле ввода сообщения, чтобы очистить его
        printMsg(stringMsg);
        Message pack = new Message(stringMsg, serverProfile);
        sendToAllConnections(pack); // Рассылка сообщений клиентам
    }

    @Override
    public void messageHandler(Message msg, TypeMessage typeMessage) {
        switch (typeMessage) {
            case VERBAL_MESSAGE:
                getMessages().add(msg);
                sendToAllConnections(msg);
                printMsg(msg.getStringValue());
                break;
            case SERVICE_MESSAGE_AUTHORIZATION:
                ClientProfile clientProfile = (ClientProfile) msg.getObjT();
                connection.setClientProfile(clientProfile);
                usersProfiles.add(connection.getClientProfile());
                printMsg(usersProfiles.toString());
//                sortedForDate(getMessages());
                Message<ArrayList<ClientProfile>, Object> messageUpdateList = new Message<>(usersProfiles, null, SERVICE_MESSAGE_UPDATE_LIST_USERS);
                Message<ArrayList<Message>, Object> messageUpdateDialogues = new Message<>(messages, null, SERVICE_MESSAGE_UPDATE_DIALOGUES);
                sendToAllConnections(messageUpdateList);
                sendToAllConnections(messageUpdateDialogues);
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        onSendPackage(connection, fieldInput.getText());
    }

//    private static ArrayList<Message> sortedForDate(ArrayList<Message> arrayMessages) {
//        Collections.sort(arrayMessages);
//        return arrayMessages;
//    }

    public static File getFileReservServer() {
        return fileReservServer;
    }

    public static synchronized ArrayList<Message> getMessages() {
        return messages;
    }


    private static void codeReservFile(ArrayList<Message> arrayList) {
//        sortedForDate(arrayList);
        System.out.println(arrayList);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ChatServer.getFileReservServer()))) {
            oos.writeObject(arrayList);
            oos.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static ArrayList<Message> decodeReservFile() {
        ArrayList<Message> arrayList = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ChatServer.getFileReservServer()))) {
            arrayList = (ArrayList<Message>) ois.readObject();
            System.out.println("Извлеченный файл\n" + arrayList);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    private boolean isFileEmpty(File file) {
        return file.length() == 0;
    }

    public static synchronized void setMessages(ArrayList<Message> messages) {
        ChatServer.messages = messages;
    }


}
