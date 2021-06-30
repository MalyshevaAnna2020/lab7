package client;

import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

public class Client {

    public static final int PORT = 4002;
    public static final String HOST = "localhost";

    public static void main(String[] args) throws ClassNotFoundException, NoSuchAlgorithmException {

        boolean start = false;
        boolean connect = false;
        boolean loginPassed = false;
        boolean passwordPassed = false;

        while(true) {

            try {
                // Подключение
                Socket s = new Socket(HOST, PORT);
                connect = true;

                System.out.println("Авторизация...");

                // Логин
                Login login = new Login();
                server.authorization.Login l = login.login(s);
                if (l != null) {

                    // Пароль
                    Password password = new Password();
                    server.authorization.Password p = password.password(s, l);
                    System.out.println(p == null);
                    if (p != null) {
                        // Команды
                        System.out.println("Можно начинать вводить команды (длина строки не должна превышать 256)!");
                        server.command.Command command = new Command().command(s, p);
                        System.out.println("Сервер временно не доступен. " +
                                "Как только соединение будет восстановленно, работа возобновится!");
                    }
                    else{
                        if (!passwordPassed){
                            passwordPassed = true;
                            connect = false;
                            System.out.println("Сервер временно не доступен. " +
                                    "Как только соединение будет восстановленно, работа возобновится!");
                        }

                    }
                }else{
                    if (!loginPassed){
                        loginPassed = true;
                        connect = false;
                        System.out.println("Сервер временно не доступен. " +
                                "Как только соединение будет восстановленно, работа возобновится!");
                    }
                }

            }catch(IOException e){
                if ((!start) && (!connect)) {
                    start = true;
                    System.out.println("Сервер еще не подключился!");
                }else{
                    if ((start) && (connect)) {
                        connect = false;
                        System.out.println("Сервер отключился! " +
                                "Как только соединение будет восстановленно, работа с коллекцией возобновится");
                    }
                }
            }
        }
    }
}
