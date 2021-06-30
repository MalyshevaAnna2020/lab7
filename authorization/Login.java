package server.authorization;

import java.io.Serializable;

public class Login implements Serializable {
    private static final long serialVersionUID = 96337964581L;
    private String login;
    private boolean registration;

    public void setLogin(String login){ this.login = login;}
    public String getLogin(){ return login;}

    public void setRegistration(boolean registration){ this.registration = registration;}
    public boolean getRegistration(){ return registration;}
}
