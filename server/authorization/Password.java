package server.authorization;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Password implements Serializable {
    private static final long serialVersionUID = 963379642345581L;
    private byte [] hash;
    private Login login;

    public void setPassword(byte[] password){ this.hash = password;}
    public byte[] getPassword(){ return hash;}

    public void setLogin(Login login){ this.login = login;}
    public Login getLogin(){ return login;}
}
