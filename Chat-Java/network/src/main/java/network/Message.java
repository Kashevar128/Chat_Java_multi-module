package network;

import java.io.Serializable;
import java.util.Date;

public class Message<T, V> implements Serializable, Comparable<Message> {

    private static final long serialVersionUID = 1L;

    private TypeMessage typeMessage;
    private ClientProfile profile;
    private String stringValue;
    private boolean inOrOut;
    private boolean Logged;
    private Date sendAt;
    private long sendingTime;

    private T obj;
    private V obj_2;

    public Message(String stringValue, ClientProfile user) {
        this.typeMessage = TypeMessage.VERBAL_MESSAGE;
        this.profile = user;
        this.stringValue = "[" + profile.getNameUser() + "] " + stringValue;
        this.inOrOut = true;
        setSendAt(new Date());
        setSendingTime(System.nanoTime());
    }

    public Message(T obj, V obj_2, TypeMessage typeMessage) {
        this.typeMessage = typeMessage;
        this.obj = obj;
        this.obj_2 = obj_2;
        setSendAt(new Date());
        setSendingTime(System.nanoTime());
    }

    public Message (boolean flag, TypeMessage typeMessage) {
        setLogged(flag);
    }


    public boolean isInOrOut() {
        return inOrOut;
    }

    public void setInOrOut(boolean inOrOut) {
        this.inOrOut = inOrOut;
    }

    public ClientProfile getProfile() {
        return profile;
    }

    public String getStringValue() {
        return stringValue;
    }

    public TypeMessage getTypeMessage() {
        return typeMessage;
    }

    public T getObjT() {
        return obj;
    }

    public V getObj_2() {
        return obj_2;
    }

    public void setSendAt(Date sendAt) {
        this.sendAt = sendAt;
    }

    public Date getSendAt() {
        return sendAt;
    }

    public long getSendingTime() {
        return sendingTime;
    }

    public void setSendingTime(long sendingTime) {
        this.sendingTime = sendingTime;
    }

    public void setLogged(boolean logged) {
        this.Logged = logged;
    }

    public boolean isLogged() {
        return Logged;
    }

    @Override
    public int compareTo(Message o) {
        if(this.sendingTime - o.sendingTime < 0) return -1;
        if(this.sendingTime - o.sendingTime > 0) return 1;
        return 0;
    }

    @Override
    public String toString() {
        return "Message{" +
                "stringValue='" + stringValue + '\'' +
                '}';
    }
}
