package server.process.commands;

import server.command.Answer;
import server.command.Command;
import server.database.DataBase;
import server.spacemarine.SpaceMarine;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Hashtable;

public class Process {
    public Answer process(Command command) throws IOException, SQLException {
        Answer mainAnswer = new Answer();
        System.out.println("Обработка команды " + command.getNameOfCommand() + "!");
        Hashtable<Hashtable<String, SpaceMarine>, String> commandHashtable = new Play().play(command, "");
        System.out.println(commandHashtable);
        mainAnswer.setKey("В коллекции нет элементов!");
        for (Hashtable<String, SpaceMarine> hashtable1 : commandHashtable.keySet()){
            DataBase.getInstance().setHashtable(hashtable1);
            mainAnswer.setKey(commandHashtable.get(DataBase.getInstance().getHashtable()));
        }
        return mainAnswer;
    }
}
