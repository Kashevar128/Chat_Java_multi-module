package network;

import java.io.Serializable;

public class ClientProfile implements Serializable {
    private String nameUser;
    private byte[] avatar;

    public ClientProfile(String nameUser, byte[] avatar) {
        this.nameUser = nameUser;
        this.avatar = avatar;
    }

    public String getNameUser() {
        return nameUser;
    }

    public byte[] getAvatar() {
        return avatar;
    }
}
