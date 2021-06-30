package server.accept;

import server.authorization.Login;
import server.authorization.Password;
import server.command.Answer;
import server.command.Command;
import server.command.NameOfCommand;
import server.database.DataBase;
import server.process.commands.Process;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;

public class Action {
    private Login login = null;
    private Password password;
    private Command command;
    private Answer answer;

    public Login getLogin(){return login;}

    private int countActions = 0;

    SocketChannel client;
    public Action(SocketChannel socketChannel){
        client = socketChannel;
    }

    private boolean work = false;
    public boolean getWork(){return work;}

    private int read = 0;

    private boolean exit = false;
    public boolean getExit(){return exit;}

    String name;

    // Чтение
    public void readLogin() throws ClassNotFoundException, IOException {
        read = 0;
        if (client.isOpen()) {
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            client.read(buffer);
            ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(buffer.array()));
            login = (Login) in.readObject();
            name = login.getLogin();
        }else{
            exit = true;
        }
    }

    public void readPassword() throws IOException, ClassNotFoundException {
        read = 1;
        if (client.isOpen()) {
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            client.read(buffer);
            ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(buffer.array()));
            this.password = (Password) in.readObject();
        }else{
            exit = true;
        }
    }

    public void readCommand() throws IOException, ClassNotFoundException {
        read = 2;
        if (client.isOpen()) {
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            client.read(buffer);
            ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(buffer.array()));
            command = (Command) in.readObject();
            System.out.println(command.getNameOfCommand() + "!");
        }else{
            exit = true;
        }
    }

    // Запись
    public void writeLogin() throws IOException {
        if (client.isOpen()) {
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            // Надо ввести логин еще раз
            if (countActions == 0) {
                login.setLogin("no");
            }
            // Запись ответа сервера клиенту
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(login);
            out.flush();
            byte[] b = bos.toByteArray();
            buffer.clear();
            for (byte value : b) {
                buffer.put(value);
            }

            buffer.flip();
            client.write(buffer);
            if (buffer.hasRemaining()) {
                buffer.compact();
            } else {
                buffer.clear();
            }
        }else{
            exit = true;
        }
    }

    public void writePassword() throws IOException {
        if (client.isOpen()) {
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            // Надо ввести пароль еще раз
            if (countActions == 1){
                login.setLogin("no");
            }
            else{
                login.setLogin(name);
            }
            // Запись ответа сервера клиенту
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(login);
            out.flush();
            byte[] b = bos.toByteArray();
            buffer.clear();
            for (byte value : b) {
                buffer.put(value);
            }

            buffer.flip();
            client.write(buffer);
            if (buffer.hasRemaining()){
                buffer.compact();
            }else{
                buffer.clear();
            }
        }
        else{
            exit = true;
        }
    }
    public void writeCommand() throws IOException {
        if (client.isOpen()) {
            // Запись ответа сервера клиенту
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(answer);
            out.flush();
            byte[] b = bos.toByteArray();
            buffer.clear();
            for (byte value : b) {
                buffer.put(value);
            }

            buffer.flip();
            client.write(buffer);
            if (buffer.hasRemaining()) {
                buffer.compact();
            } else {
                buffer.clear();
            }
            //if (command.getNameOfCommand() == NameOfCommand.EXIT) System.exit(0);
        }else{
            exit = true;
        }
    }

    public void read(){
        try {
            switch (countActions) {
                case 0:
                    System.out.println("Чтение логина. Поток " + Thread.currentThread());
                    readLogin();
                    break;
                case 1:
                    System.out.println("Чтение пароля. Поток " + Thread.currentThread());
                    readPassword();
                    break;
                case 2:
                    System.out.println("Чтение команды. Поток " + Thread.currentThread());
                    readCommand();
                    break;
            }
        } catch (IOException | ClassNotFoundException e) {
            exit = true;
        }
    }

    public void work(){
        try{
            Manager manager = new Manager();
            if (countActions == 0){
                System.out.println("Обработка запроса (логин). Поток " + Thread.currentThread());
                boolean answer = DataBase.getInstance().findUser(login.getLogin());
                countActions = manager.readingLogin(login, answer);
            }
            else if (countActions == 1){
                System.out.println("Обработка запроса (пароль). Поток " + Thread.currentThread());
                countActions = manager.readPassword(login, password, name);
                if ((login.getRegistration()) && (countActions == 2)) {
                    DataBase.getInstance().addUser(name, password.getPassword());
                }
            }
            else {
                System.out.println("Обработка запроса (команда). Поток " + Thread.currentThread());
                answer = new Process().process(command);
            }
            work = true;
        } catch (SQLException | IOException | NullPointerException e) {
            //
            exit = true;
        }
    }

    public void write(){
        try {
            switch (read) {
                case 0:
                    System.out.println("Запись (логин). Поток " + Thread.currentThread());
                    writeLogin();
                    break;
                case 1:
                    System.out.println("Запись (пароль). Поток " + Thread.currentThread());
                    writePassword();
                    break;
                case 2:
                    System.out.println("Запись (команда). Поток " + Thread.currentThread());
                    writeCommand();
                    break;
            }
            command = null;
        } catch (IOException e) {
            e.printStackTrace();
            exit = true;
        }
    }
}
