package client;

import server.command.Command;
import server.command.NameOfCommand;
import server.spacemarine.Chapter;
import server.spacemarine.Coordinates;
import server.spacemarine.SpaceMarine;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ReadCommand {

    private static final Map<String, NameOfCommand> commandMap = new HashMap<String, NameOfCommand>(){{
        put("exit", NameOfCommand.EXIT);
        put("help", NameOfCommand.HELP);
        put("info", NameOfCommand.INFO);
        put("show", NameOfCommand.SHOW);
        put("insert", NameOfCommand.INSERT);
        put("update", NameOfCommand.UPDATE);
        put("remove_key", NameOfCommand.REMOVE_KEY);
        put("clear", NameOfCommand.CLEAR);
        put("execute_script", NameOfCommand.EXECUTE_SCRIPT);
        put("remove_lower_key", NameOfCommand.REMOVE_LOWER_KEY);
        put("remove_lower", NameOfCommand.REMOVE_LOWER);
        put("remove_greater_key", NameOfCommand.REMOVE_GREATER_KEY);
        put("remove_any_by_chapter", NameOfCommand.REMOVE_ANY_BY_CHAPTER);
        put("filter_greater_than_achievements", NameOfCommand.FILTER_GREATER_THAN_ACHIEVEMENTS);
        put("print_field_descending_category", NameOfCommand.PRINT_FIELD_DESCENDING_CATEGORY);
    }};

    public Command readCommand() {
        try {
            Command command = new Command();
            try {
                String newcommand;
                Scanner in = new Scanner(System.in);
                newcommand = in.nextLine();
                if (newcommand.length() > 256) {
                    System.err.println("Очень длинная строка! Введите более короткий вариант!");
                    return null;
                }
                if (newcommand.equals("")) return null;
                newcommand = newcommand.trim();
                for (String key : commandMap.keySet()) {
                    if (newcommand.contains(key)) {
                        command.setNameOfCommand(commandMap.get(key));
                        command.setKey(newcommand.substring(newcommand.indexOf(key) + key.length()).trim());
                        break;
                    } else {
                        command.setNameOfCommand(NameOfCommand.NOTHING);
                    }
                }
                if ((newcommand.contains("remove_lower")) && (!newcommand.contains("remove_lower_key"))) {
                    command.setNameOfCommand(NameOfCommand.REMOVE_LOWER);
                    command.setKey(newcommand.substring(newcommand.indexOf("remove_lower") + "remove_lower".length()).trim());
                }
                if (newcommand.contains("remove_lower_key")) {
                    command.setNameOfCommand(NameOfCommand.REMOVE_LOWER_KEY);
                    command.setKey(newcommand.substring(newcommand.indexOf("remove_lower_key") + "remove_lower_key".length()).trim());
                }
                if (newcommand.contains("insert")) {
                    SpaceMarine spmarine = new SpaceMarine();
                    command.setSpaceMarine(setSpaceMarine(spmarine));
                }
                if (newcommand.contains("update")) {
                    try {
                        int id = Integer.parseInt(command.getKey());
                        System.out.println(id);
                        if (id < 1) {
                            System.out.println("Id - целое число!");
                            command = null;
                        } else {
                            SpaceMarine spmarine = new SpaceMarine();
                            command.setSpaceMarine(setSpaceMarine(spmarine));
                            command.setKey(String.valueOf(id));
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("id - целое число!");
                        command = null;
                    }
                }
                if (newcommand.contains("execute_script")) {
                    File file = new File(newcommand.substring
                            (newcommand.indexOf("execute_script") + ("execute_script").length()).trim());
                    if (!file.exists()) {
                        System.out.println("Файл не существует! Эта команда не передана серверу!");
                        command = null;
                    } else if (!file.canRead()) {
                        System.out.println("Файл не доступен для чтения! Эта команда не передана серверу!");
                        command = null;
                    } else {
                        assert command != null;
                        StringBuilder stringBuilder = new StringBuilder();
                        while (file.length() > 4096) {
                            stringBuilder.append(this.writeCommandsFromFiles(file));
                        }
                        stringBuilder.append(this.writeCommandsFromFiles(file));
                        String s = file.getAbsolutePath() + "\n" + stringBuilder.toString();
                        command.setKey(s);
                    }
                }

            } catch (NoSuchElementException e) {
                command = null;
                System.exit(0);
            }
            return command;
        }catch(IOException e){
            return null;
        }
    }

    public String writeCommandsFromFiles(File file) throws IOException {
        char [] chars = new char[4096];
        FileReader reader = new FileReader(file);
        reader.read(chars);
        String s = (new String(chars)).trim();
        reader.close();
        return s;
    }

    public SpaceMarine setSpaceMarine(SpaceMarine spaceMarine){
        Insert insert = new Insert();

        spaceMarine.setName(insert.insertName());

        Coordinates coordinates = new Coordinates();
        coordinates.setX(insert.insertX());
        coordinates.setY(insert.insertY());
        spaceMarine.setCoordinates(coordinates);

        spaceMarine.setCreationDate(new Date());

        spaceMarine.setHealth(insert.insertHealth());

        spaceMarine.setHeartCount(insert.insertHeartCount());

        spaceMarine.setAchievements(insert.insertAchievements());

        spaceMarine.setCategory(insert.insertCategory());

        Chapter chapter = new Chapter();
        chapter.setName(insert.insertNameOfChapter());
        chapter.setMarinesCount(insert.insertMarinesCount());
        spaceMarine.setChapter(chapter);

        return spaceMarine;
    }
}
