package server.process.commands;

import server.spacemarine.SpaceMarine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

public class WriteToFile {
    public void write(Hashtable<String, SpaceMarine> hashtable, File file) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<!DOCTYPE hashtable>\n<hashtable>\n");
        if (!hashtable.isEmpty()) {
            for (String key1 : hashtable.keySet())
                writer.write("    <" + key1 + " id=\"" + hashtable.get(key1).getId() + "\" name=\"" + hashtable.get(key1).getName() + "\" coordinates=\""
                        + hashtable.get(key1).getCoordinates().toString() + "\" creationDate=\"" + hashtable.get(key1).getCreationDate()
                        + "\" health=\"" + hashtable.get(key1).getHealth() + "\" heartCount=\"" + hashtable.get(key1).getHeartCount()
                        + "\" achievements=\"" + hashtable.get(key1).getAchievements() + "\" category=\""
                        + hashtable.get(key1).getCategory() + "\" chapter=\"" + hashtable.get(key1).getChapter() + "\" />\n");
        }
        writer.write("</hashtable>\n");
        writer.close();
    }
}
