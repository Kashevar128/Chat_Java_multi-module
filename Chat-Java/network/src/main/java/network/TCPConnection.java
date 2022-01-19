package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class TCPConnection{   // Класс, инкапсулирующий в себе логику интернет - соединения

    private final Socket socket; // Поле сокета
    private Thread readThread; // Поток чтения сообщений, не финал, так как состояние потока может изменится - он может оказаться прерванным
    private final TCPConnectionListener eventListener; // Экземпляр интерфейса для доступа к его методам - событиям
    private final ObjectOutputStream outObj;
    private final ObjectInputStream inObj;
    private ClientProfile clientProfile = null;

    public TCPConnection (String ipAdd, int port, TCPConnectionListener eventListener) throws IOException {
        this (new Socket(ipAdd, port), eventListener); // Создаем конструктор для нового сокета, использует ссылку на второй конструктор
    }

    public TCPConnection(Socket socket, TCPConnectionListener eventListener) throws IOException { //Конструктор с исключением для случая неудачного установления или прерывания канала чтения, конструктор используется для созданного заранее сокета
        this.socket = socket;
        outObj = new ObjectOutputStream(socket.getOutputStream());
        inObj = new ObjectInputStream(socket.getInputStream());
        this.eventListener = eventListener;
        readThread = new Thread( () -> { // На вход экземпляру потока подается анонимный класс имплементированный от Runnable, тут же создается объект
            try {                         //этого класса и пишется тело класса - переопределенный метода run, это все можно заменить объектом с содержанием метода - лямбда - выражением
                eventListener.onConnectionReady(TCPConnection.this); //Событие - установление связи, передаем экземпляр сообщения
                while (!readThread.isInterrupted()) { // Цикл работает, пока не прервется поток чтения
                    Message msg = (Message) inObj.readObject(); // Читаем из обработанного потока сообщение в переменную msg
                    eventListener.onReceivePackage(TCPConnection.this, msg); // Вызываем событие - получение сообщения, передаем экземпляр подключения и само сообщение
                }
            } catch (IOException | ClassNotFoundException e) {
                eventListener.onException(TCPConnection.this, e); // Вызываем событие - выброс исключения
            } finally {
                try {
                    eventListener.onDisconnect(TCPConnection.this); // Вызываем событие - прерывание связи, но не метод disconnect, так как прерывание со стороны сервера и со стороны клиента будут обрабатываться по разному
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            }
        });
        readThread.start(); // Стартуем поток
    }

    public Socket getSocket() {
        return socket;
    }


    public synchronized void sendMessage (Message msg) { // Метод для отправки сообщений
        try {
            if(msg.getTypeMessage().equals(TypeMessage.SERVICE_MESSAGE_UPDATE_LIST_USERS)) {
                outObj.reset();
            }
            outObj.writeObject(msg); // Пишем в поток, \r - перевод каретки на начало строки для корректной работы с программой Putty, \n - перенос каретки на следующую строку
            outObj.flush(); // Сбрасываем написанное в буфере в поток, если вдруг сообщение застряло в буфере и не передалось по сети
        } catch (IOException e) {
            throw new RuntimeException("Connection close");
        }
    }

    public synchronized void disconnect () { // Метод для прерывания связи
        readThread.interrupt(); // Прерываем поток чтения
        try {
            socket.close(); // После прерывания закрываем сокет
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() { // Переопределение метода для вывода нужной информации
        return "TCPConnection: " + socket.getInetAddress() + ": " + socket.getPort(); // Вывод в консоль адреса машины сервера и порта, который он занял
    }

    public ClientProfile getClientProfile() {
        return clientProfile;
    }

    public void setClientProfile(ClientProfile clientProfile) {
        this.clientProfile = clientProfile;
    }

}
