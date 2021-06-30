package server.accept;

import server.authorization.Login;
import server.authorization.Password;
import server.database.DataBase;

import java.sql.SQLException;

public class Manager {
    public int readingLogin(Login l, boolean answer){
        if (l.getRegistration()) {
            if (answer) {
                l.setLogin("no");
                return 0;
            }
        }
        else{
            if (!answer) {
                l.setLogin("no");
                return 0;
            }
        }
        return 1;
    }

    public int readPassword(Login l, Password p, String name) throws SQLException {
        if (l.getRegistration()) {
            boolean answer = DataBase.getInstance().findPassword(p.getPassword());
            if (answer) {
                return 1;
            }
        }
        else{
            boolean answer = DataBase.getInstance().findName(name, p.getPassword());
            if (!answer){
                return 1;
            }
        }
        return 2;
    }
}
