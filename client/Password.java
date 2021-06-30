package client;

import server.authorization.Login;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Password {

    public server.authorization.Password password(Socket s, Login login) throws NoSuchAlgorithmException, ClassNotFoundException {
        try {
            System.out.print("Пароль аккаунта: ");
            Console console = System.console();
            char[] passwd = console.readPassword();

            server.authorization.Password p = new server.authorization.Password();

            p.setLogin(login);

            MessageDigest md = MessageDigest.getInstance("SHA-384");
            byte[] hash = md.digest(Arrays.toString(passwd).getBytes(StandardCharsets.UTF_8));
            p.setPassword(hash);

            // Запись пароля на сервер
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(p);
            out.flush();
            byte[] yourBytes = bos.toByteArray();
            OutputStream os = s.getOutputStream();
            os.write(yourBytes);

            // Чтение ответа сервера
            ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());
            server.authorization.Login l = (server.authorization.Login) objectInputStream.readObject();

            String answer1;
            String answer2 = "";

            if (login.getRegistration()) {
                answer1 = "Данный пароль уже существует!";
                answer2 = "Регистрация закончена! ";
            } else {
                answer1 = "Неверный пароль!";
            }
            if (l.getLogin().toLowerCase().trim().equals("no")) {
                System.out.println(answer1);
                return new Password().password(s, login);
            } else {
                System.out.println(answer2 + "Вход в систему успешно выполнен!");
                return p;
            }
        }catch(IOException e){
            return new server.authorization.Password();
        }
    }
}
