package server.process.commands;

import server.command.Command;
import server.command.NameOfCommand;
import server.spacemarine.Chapter;
import server.spacemarine.Coordinates;
import server.spacemarine.SpaceMarine;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExecuteScript {

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

    private static ExecuteScript instance = null;

    private ExecuteScript(){ }

    public static ExecuteScript getInstance() {
        if (ExecuteScript.instance == null) ExecuteScript.instance = new ExecuteScript();
        return ExecuteScript.instance;
    }

    public String checkFile(String file){
        return "execute_script " + 
                file + 
                " - это команда на исполнение этого же файла, возможна рекурсия!";
    }


    public Command setCommand(String newcommand){
        Command command1 = new Command();
        for (String keyScript : commandMap.keySet()) {
            if (newcommand.contains(keyScript)) {
                command1.setNameOfCommand(commandMap.get(keyScript));
                command1.setKey(newcommand.substring(newcommand.indexOf(keyScript) + keyScript.length()).trim());
                break;
            } else command1.setNameOfCommand(NameOfCommand.NOTHING);
        }
        if ((newcommand.contains("remove_lower")) && (!newcommand.contains("remove_lower_key"))) {
            command1.setNameOfCommand(NameOfCommand.REMOVE_LOWER);
        }
        return command1;
    }

    public SpaceMarine setSpacemarine(String s, SpaceMarine spmarine, int i, Coordinates coordinates, Chapter chapter){
        switch (i) {
            case 0:
                spmarine.setName(s.substring(0, s.indexOf("\n") - 1));
                break;
            case 1:
                coordinates.setX(s.substring(0, s.indexOf("\n") - 1));
                spmarine.setCoordinates(coordinates);
                break;
            case 2:
                coordinates.setY(s.substring(0, s.indexOf("\n") - 1));
                spmarine.setCoordinates(coordinates);
                break;
            case 3:
                String health = s.substring(0, s.indexOf("\n") - 1);
                try {
                    spmarine.setHealth(Long.parseLong(health));
                    if (Long.parseLong(health) <= 0) spmarine.setHealth(1L);
                } catch (NumberFormatException e) {
                    spmarine.setHealth(1L);
                }
                break;
            case 4:
                String strHeartCount = s.substring(0, s.indexOf("\n") - 1);
                try {
                    spmarine.setHeartCount(Integer.parseInt(strHeartCount.trim()));
                    if (Integer.parseInt(strHeartCount.trim()) > 3)
                        spmarine.setHeartCount(3);
                    if (Integer.parseInt(strHeartCount.trim()) < 1)
                        spmarine.setHeartCount(1);
                } catch (NumberFormatException e) {
                    spmarine.setHeartCount(1);
                }
                break;
            case 5:
                spmarine.setAchievements(s.substring(0, s.indexOf("\n") - 1));
                break;
            case 6:
                spmarine.setCategory(s.substring(0, s.indexOf("\n") - 1));
                break;
            case 7:
                chapter.setName(s.substring(0, s.indexOf("\n") - 1));
                spmarine.setChapter(chapter);
                break;
            case 8:
                chapter.setMarinesCount(s.substring(0, s.indexOf("\n") - 1));
                spmarine.setChapter(chapter);
                break;
        }
        return spmarine;
    }

    public SpaceMarine setSpacemarine(int i, SpaceMarine spmarine, Coordinates coordinates, Chapter chapter){
        switch (i) {
            case 0:
                spmarine.setName("Id_" + spmarine.getId());
                break;
            case 1:
                coordinates.setX("0");
                spmarine.setCoordinates(coordinates);
                break;
            case 2:
                coordinates.setY("0");
                spmarine.setCoordinates(coordinates);
                break;
            case 3:
                spmarine.setHealth(1L);
                break;
            case 4:
                spmarine.setHeartCount(1);
                break;
            case 5:
                spmarine.setAchievements("Достижений нет");
                break;
            case 7:
                chapter.setName("-");
                spmarine.setChapter(chapter);
                break;
            case 8:
                chapter.setMarinesCount("1");
                spmarine.setChapter(chapter);
                break;
        }
        return spmarine;
    }

    public Command setExecuteScript(File file1, Command command1) throws IOException {
        if (!file1.exists()){
            System.out.println("Файл не существует! Эта команда не передана серверу!");
            command1 = null;
        }
        else if (!file1.canRead()){
            System.out.println("Файл не доступен для чтения! Эта команда не передана серверу!");
            command1 = null;
        }
        else{
            command1.setKey(this.writeCommandsFromFiles(file1));
        }
        return command1;
    }

    public String writeCommandsFromFiles(File file) throws IOException {
        char [] chars = new char[4096];
        FileReader reader = new FileReader(file);
        reader.read(chars);
        String s = file.getAbsolutePath() + "\n" + (new String(chars)).trim();
        reader.close();
        return s;
    }

}
