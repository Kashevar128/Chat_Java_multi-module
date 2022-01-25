package clientlogic;

public interface AuthService {

    public boolean addUser(String name, String pass) throws Exception;

    public boolean auth(String name, String pass) throws Exception;

   // public void offLine(String name) throws Exception;
}
