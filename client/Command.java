package client;

import server.authorization.Password;
import server.command.Answer;
import server.command.NameOfCommand;

import java.io.*;
import java.net.Socket;

public class Command {

    public server.command.Command command(Socket s, Password password) throws IOException, ClassNotFoundException {
        while(true) {
            server.command.Command command = new ReadCommand().readCommand();
            if (command == null){
                return null;
            }else{
                command.setPassword(password);
                // Запись
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(bos);
                out.writeObject(command);
                out.flush();
                byte[] yourBytes = bos.toByteArray();
                OutputStream os = s.getOutputStream();
                os.write(yourBytes);

                // Чтение результата работы сервера
                ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());
                Answer answer = (Answer) objectInputStream.readObject();
                System.out.println(answer.getKey());

                if (command.getNameOfCommand() == NameOfCommand.EXIT) System.exit(0);
            }
        }
    }
}
