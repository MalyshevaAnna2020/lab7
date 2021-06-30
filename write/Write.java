package server.write;

import server.authorization.Login;
import server.command.Command;
import server.command.NameOfCommand;
import server.process.commands.Process;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;

public class Write {
    private static Write instance = null;
    private Write(){ }

    public static Write getInstance() {
        if (instance == null) instance = new Write();
        return instance;
    }

    private Login login;
    public void setLogin(Login login){ this.login = login;}
    //public Login getLogin(){ return login;}

    private int countActions;
    public void setCountActions(int countActions){ this.countActions = countActions;}
    //public int getCountActions(){ return countActions;}

    private Command command;
    public void setCommand( Command command){ this.command =command;}

    public void writeLogin(SocketChannel channel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        // Надо ввести логин еще раз
        if (countActions == 0){
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
        channel.write(buffer);
        if (buffer.hasRemaining()){
            buffer.compact();
        }else{
            buffer.clear();
        }
    }

    public void writePassword(SocketChannel channel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        // Надо ввести пароль еще раз
        if (countActions == 1){
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
        channel.write(buffer);
        if (buffer.hasRemaining()){
            buffer.compact();
        }else{
            buffer.clear();
        }
    }
    public void writeCommand(SocketChannel channel) throws IOException, SQLException {
        // Запись ответа сервера клиенту
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(new Process().process(command));
        out.flush();
        byte[] b = bos.toByteArray();
        buffer.clear();
        for (byte value : b) {
            buffer.put(value);
        }

        buffer.flip();
        channel.write(buffer);
        if (buffer.hasRemaining()){
            buffer.compact();
        }else{
            buffer.clear();
        }
        if (command.getNameOfCommand() == NameOfCommand.EXIT) System.exit(0);

    }
}
