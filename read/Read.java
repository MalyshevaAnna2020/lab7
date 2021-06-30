package server.read;

import server.authorization.Password;
import server.authorization.Login;
import server.command.Command;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Read {
    private Login login;
    private Password password;

    private static Read instance = null;
    private Read(){ }

    public static Read getInstance() {
        if (instance == null) instance = new Read();
        return instance;
    }

    public Login getLogin(){ return login;}
    public Password getPassword(){ return password;}

    private Command command;
    public Command getCommand(){ return command;}

    public void readLogin(SocketChannel socketChannel1) throws IOException, ClassNotFoundException {
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        socketChannel1.read(buffer);
        ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(buffer.array()));
        this.login = (Login) in.readObject();
    }

    public void readPassword(SocketChannel socketChannel) throws IOException, ClassNotFoundException {
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        socketChannel.read(buffer);
        ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(buffer.array()));
        this.password = (Password) in.readObject();
    }

    public void readCommand(SocketChannel socketChannel) throws IOException, ClassNotFoundException {
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        socketChannel.read(buffer);
        ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(buffer.array()));
        command = (Command) in.readObject();

    }

}
