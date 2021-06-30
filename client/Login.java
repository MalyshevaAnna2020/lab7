package client;

import java.io.*;
import java.net.Socket;

public class Login {
    public static final int PORT = 4002;
    public static final String HOST = "localhost";

    public server.authorization.Login login(Socket s) throws IOException, ClassNotFoundException {
        try {
            server.authorization.Login login = new server.authorization.Login();
            System.out.println("Введите логин (если Вы еще не зарегистрировались, введите \"no\":");
            Console console = System.console();
            String l = console.readLine();
            if (l.toLowerCase().trim().equals("no")) {
                login.setRegistration(true);

                while (true) {
                    System.out.print("Имя будущего аккаунта: ");
                    l = console.readLine();
                    login.setLogin(l);

                    // Запись логина на сервер
                    new Login().writeLoginToServer(login, s);

                    // Чтение ответа сервера
                    ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());
                    login = (server.authorization.Login) objectInputStream.readObject();
                    if (login.getLogin().toLowerCase().trim().equals("no")) {
                        System.out.println("Пользователь с данным аккаунтом уже существует!");
                    } else break;
                }
            } else {
                login.setRegistration(false);
                login.setLogin(l);

                // Запись логина на сервер
                System.out.println("Запись логина на сервер");
                new Login().writeLoginToServer(login, s);

                // Чтение ответа сервера
                System.out.println("Чтение ответа сервера");
                ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());
                login = (server.authorization.Login) objectInputStream.readObject();
                if (login.getLogin().toLowerCase().trim().equals("no")) {
                    System.out.println("Неверно введен логин!");
                    return new Login().login(s);
                }
            }
            return login;
        }catch(IOException e){
            return null;
        }
    }
    public void writeLoginToServer(server.authorization.Login login, Socket s) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(login);
        out.flush();
        byte[] yourBytes = bos.toByteArray();
        OutputStream os = s.getOutputStream();
        os.write(yourBytes);
    }

}
