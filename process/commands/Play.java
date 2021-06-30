package server.process.commands;

import server.command.Command;
import server.database.DataBase;
import server.spacemarine.Chapter;
import server.spacemarine.Coordinates;
import server.spacemarine.SpaceMarine;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

public class Play {
    private final List<String> files = new ArrayList<>();

    public Hashtable<Hashtable<String, SpaceMarine>, String> play (Command command, String mess) throws IOException, SQLException {
        // Сборка ответа клиенту
        StringBuilder message = new StringBuilder();
        message.append(mess);

        // [коллекция : ответ клиенту]
        Hashtable<Hashtable<String, SpaceMarine>, String> mainHashtable = new Hashtable<>();

        // Действия перенесены в другой класс, чтобы не загромождать
        ResultOfCommand resultOfCommand = new ResultOfCommand();

        // Чтение коллекции из PostgreSQL
        Hashtable<String, SpaceMarine> hashFromSQL = DataBase.getInstance().updateCollection();
        int size = hashFromSQL.size();

        // Обновлять состояние коллекции в памяти только при успешном добавлении объекта в БД
        boolean updateSQL = false;

        // Выбор команд (сделано с помощью switch,
        // так как многие команды меняют не только ответ клиенту (добавление предложения в конец "конечной" строки),
        // но и коллекцию
        switch (command.getNameOfCommand()){
            case EXIT:
                message.append(resultOfCommand.info(hashFromSQL)).append("Завершение работы!");
                mainHashtable.put(hashFromSQL, message.toString());
                // Коллекция уже автоматически записана в файле, поэтому дополнительно записывать ее не требуется
                // ОДНАКО КОЛЛЕКЦИЮ НАДО ПЕРЕДАТЬ КЛИЕНТУ
                break;
            case HELP:
                message.append(resultOfCommand.help());
                mainHashtable.put(hashFromSQL, message.toString());
                break;
            case INFO:
                message.append(resultOfCommand.info(hashFromSQL));
                mainHashtable.put(hashFromSQL, message.toString());
                break;
            case SHOW:
                message.append(resultOfCommand.show(hashFromSQL));
                mainHashtable.put(hashFromSQL, message.toString());
                break;
            case REMOVE_KEY:
                if (hashFromSQL.containsKey(command.getKey())) {
                    if (resultOfCommand.removeKey(command, hashFromSQL)) {
                        hashFromSQL.remove(command.getKey());
                        message.append("Элемент с ключом \"").append(command.getKey()).append("\" удален!\n");
                        updateSQL = true;
                    }else{
                        message.append("Элемент с ключом \"").append(command.getKey()).append("\" " +
                                "принадлежит другому пользователю, поэтому Вы не можете его удалить!\n");
                    }
                }else{
                    message.append("Элемент с ключом \"").append(command.getKey()).append("\" не найден!\n");
                }
                mainHashtable.put(hashFromSQL, message.toString());
                break;
            case CLEAR:
                int countRemove = 0;
                if (hashFromSQL.size() > 0) {
                    for (int i = 0; i < hashFromSQL.size(); i ++){
                        if (resultOfCommand.removeKey(command, hashFromSQL)) {
                            hashFromSQL.remove(command.getKey());
                            countRemove++;
                            updateSQL = true;
                        }
                    }
                    if (countRemove > 0) {
                        message.append("Все элементы коллекции, принадлежащие Вам, успешно удалены (удалено ")
                                .append(countRemove)
                                .append(" элементов)!\n");
                    }else{
                        message.append("Коллекция не содержала ни одного элемента, принадлежащий Вам!\n");
                    }
                }
                else{ message.append("Коллекция уже была пуста!"); }
                mainHashtable.put(hashFromSQL, message.toString());
                break;
            case PRINT_FIELD_DESCENDING_CATEGORY:
                message.append(resultOfCommand.print(hashFromSQL));
                mainHashtable.put(hashFromSQL, message.toString());
                break;
            case REMOVE_ANY_BY_CHAPTER:
                hashFromSQL = resultOfCommand.remove_any_by_chapter(hashFromSQL, command);
                message.append(resultOfCommand.remove_any_by_chapter(command, size, hashFromSQL.size()));
                mainHashtable.put(hashFromSQL, message.toString());
                if (size !=hashFromSQL.size()) updateSQL = true;
                break;
            case REMOVE_LOWER_KEY:
                try {
                    hashFromSQL = resultOfCommand.remove_lower_key(hashFromSQL, command);
                    message.append(resultOfCommand.remove_lower_key(command, size, hashFromSQL.size()));
                } catch (NumberFormatException e) {
                    message.append("Количество символов строки ключа представляет собой натуральное число!");
                }
                mainHashtable.put(hashFromSQL, message.toString());
                if (size !=hashFromSQL.size()) updateSQL = true;
                break;
            case REMOVE_LOWER:
                try {
                    hashFromSQL = resultOfCommand.remove_lower(hashFromSQL, command);
                    message.append(resultOfCommand.remove_lower(command, size, hashFromSQL.size()));
                } catch (NumberFormatException e) {
                    message.append("Переменная id представляет собой натуральное число!");
                }
                mainHashtable.put(hashFromSQL, message.toString());
                if (size !=hashFromSQL.size()) updateSQL = true;
                break;
            case REMOVE_GREATER_KEY:
                try {
                    hashFromSQL = resultOfCommand.remove_greater_key(hashFromSQL, command);
                    message.append(resultOfCommand.remove_greater_key(command, size, hashFromSQL.size()));
                } catch (NumberFormatException e) {
                    message.append("Количество символов строки ключа представляет собой натуральное число!");
                }
                mainHashtable.put(hashFromSQL, message.toString());
                if (size !=hashFromSQL.size()) updateSQL = true;
                break;
            case FILTER_GREATER_THAN_ACHIEVEMENTS:
                try {
                    message.append(resultOfCommand.filter_greater_than_achievements(hashFromSQL, command));
                } catch (NumberFormatException e) {
                    message.append("Количество символов в строке достижений представляет собой натуральное число!");
                }
                mainHashtable.put(hashFromSQL, message.toString());
                break;
            case INSERT:
                SpaceMarine spaceMarine = command.getSpaceMarine();
                System.out.println(hashFromSQL + "&");
                spaceMarine.setId(hashFromSQL.size() + 1);
                spaceMarine.setUser(command.getPassword().getLogin().getLogin());
                hashFromSQL.put(command.getKey(), spaceMarine);
                message.append(resultOfCommand.insert(command));
                mainHashtable.put(hashFromSQL, message.toString());
                if (size !=hashFromSQL.size()) updateSQL = true;
                break;
            case UPDATE:
                hashFromSQL = resultOfCommand.update(hashFromSQL, command);
                System.out.println(hashFromSQL + "$");
                String answer = resultOfCommand.update(command, hashFromSQL);
                System.out.println(answer);
                message.append(answer);
                mainHashtable.put(hashFromSQL, message.toString());
                System.out.println(mainHashtable);
                if (answer.contains("обновлен")) {
                    updateSQL = true;
                }
                message.append(answer);
                break;
            case EXECUTE_SCRIPT:
                // Передаваемый текст
                String textFromFile = command.getKey();
                // Файл, в котором прописаны команды
                String file = textFromFile.substring(0, textFromFile.indexOf("\n"));
                // Штука для удобства (Singleton)
                ExecuteScript executeScript = ExecuteScript.getInstance();
                // !Рекурсия!
                if (files.contains(file)){
                    // Переходим к выполнению следующей команды
                    message.append(executeScript.checkFile(file));
                }else {
                    // Во избежание рекурсии
                    files.add(file);
                    // Строка, прочитанная из файла
                    String s = textFromFile.substring(textFromFile.indexOf("\n") + 1) + "\n";
                    // Чтение каждой "строки" (то есть до переноса строки) строки s
                    while (s.contains("\n")) {
                        // Команда
                        String newcommand = s.substring(0, s.indexOf("\n"));
                        // Очень длинная строка - переходим к выполнению следующей
                        if (newcommand.length() > 256) {
                            message.append(newcommand).append(" - очень длинная строка!");
                            continue;
                        }
                        // Такую команду не стоит обрабатывать
                        if (newcommand.equals("")) continue;
                        // Создание настоящей команды
                        Command command1 = executeScript.setCommand(newcommand);
                        // Если команда - insert или update, надо команде добавить SpaceMarine
                        SpaceMarine spmarine = new SpaceMarine();
                        // insert
                        if (newcommand.contains("insert")) {
                            spmarine.setId(hashFromSQL.size() + 1);
                        }
                        // update
                        int count = 0;
                        if (newcommand.contains("update")) {
                            try {
                                int newId = Integer.parseInt(newcommand.substring(newcommand.indexOf("update") + 6).trim());
                                s += "\n";
                                for (String s1 : hashFromSQL.keySet())
                                    if (hashFromSQL.get(s1).getId() == newId) {
                                        count = 1;
                                        command1.setKey(s1);
                                    }
                                if (count == 1) {
                                    spmarine.setId(newId);
                                    command1.setKey(String.valueOf(newId));
                                } else {
                                    message.append("В коллекции нет элемента с id=").append(newId).append("!");
                                    continue;
                                }
                            } catch (NumberFormatException e) {
                                message.append("Переменная id представляет собой число (команда update)!");
                                continue;
                            }
                        }
                        // insert & update
                        if ((newcommand.contains("insert")) ||
                                ((newcommand.contains("update")) && (count == 1))) {
                            s = s.substring(s.indexOf("\n") + 1);
                            Coordinates coordinates = new Coordinates();
                            Chapter chapter = new Chapter();
                            for (int i = 0; i < 9; i++) {
                                if (s.contains("\n")) {
                                    spmarine = executeScript.setSpacemarine(s, spmarine, i, coordinates, chapter);
                                    s = s.substring(s.indexOf("\n") + 1);
                                } else {
                                    spmarine = executeScript.setSpacemarine(i, spmarine, coordinates, chapter);
                                }
                            }
                            spmarine.setCreationDate(new Date());
                            command1.setSpaceMarine(spmarine);
                        }
                        if (newcommand.contains("execute_script")){
                            File file1 = new File(newcommand.substring
                                    (newcommand.indexOf("execute_script") + ("execute_script").length()).trim());
                            if (file1.exists()){
                                message.append("Файл ").append(file1.getAbsolutePath()).append(" не существует!");
                            }
                            else if (file1.canRead()){
                                message.append("Файл ").append(file1.getAbsolutePath()).append(" не доступен для чтения!");
                            }
                            command1 = executeScript.setExecuteScript(file1, command1);
                        }
                        Play play = new Play();
                        mainHashtable.clear();
                        mainHashtable = play.play(command1, message.toString());
                        for (Hashtable<String, SpaceMarine> hashtable1 : mainHashtable.keySet()) {
                            hashFromSQL = hashtable1;
                            message.setLength(0);
                            message.append(mainHashtable.get(hashFromSQL)).append("\n");
                        }
                        s = s.substring(s.indexOf("\n") + 1);
                        if (s.length() <= 1) break;
                        else s = s.substring(1);
                    }
                }
                break;
            case NOTHING:
                message.append("Команда help поможет Вам узнать о существующих командах");
                break;
        }

        System.out.println(message.toString());
        // Теперь запись не в файл, а в PostgreSQL
        if (updateSQL){
            DataBase.getInstance().newTable(hashFromSQL);
        }

        // new WriteToFile().write(DataBase.getInstance().getHashtable(), DataBase.getInstance().getFile_main());
        // mainHashtable.put(DataBase.getInstance().getHashtable(), message.toString());
        return mainHashtable;
    }
}