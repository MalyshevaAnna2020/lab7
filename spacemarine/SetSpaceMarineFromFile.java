package server.spacemarine;

public class SetSpaceMarineFromFile {
    public String setName(SpaceMarine spaceMarine, String s, GetValue getValue){
        String name;
        if (s.contains("name")) {
            name = getValue.getValue(s, "name");
            if (name.equals("")) {
                name = "id_" + spaceMarine.getId();
                System.out.println("Поле name не может быть пустой строкой!");
                System.out.println("Новео значение поля name элемента с id=" + spaceMarine.getId() + ": "
                        + name + "\"");
            }
        } else {
            name = "id_" + spaceMarine.getId();
            System.out.println("У элемента с id=" + spaceMarine.getId() + " отсутствует поле name!");
            System.out.println("Новео значение поля name элемента с id=" + spaceMarine.getId() + ": "
                    + name + "\"");
        }
        return name;
    }

    public Coordinates setCoordinates(SpaceMarine spaceMarine, String s, GetValue getValue){
        Coordinates coordinates = new Coordinates();
        if (s.contains("coordinates")) {
            String strcoordinates = getValue.getValue(s, "coordinates");
            coordinates.setX(strcoordinates.substring(0, strcoordinates.indexOf(" ")));
            coordinates.setY(strcoordinates.substring(strcoordinates.indexOf(" ") + 1));
        } else {
            coordinates.setX("0");
            coordinates.setY("0");
            System.out.println("У элемента с id=" + spaceMarine.getId() + "отсутствует поле coordinates!");
            System.out.println("Новое значение поля coordinates элемента с id=" + spaceMarine.getId() + ": \"" +
                    coordinates.toString() + "\"");
        }
        return coordinates;
    }

    public Long setHealth(SpaceMarine spaceMarine, String s, GetValue getValue){
        long health;
        if (s.contains("health")) {
            String strHealth = getValue.getValue(s,"health");
            try {
                health = Long.parseLong(strHealth);
                if (health <= 0){
                    System.out.println("Поле health представляет собой положительное число!");
                    health = 1L;
                    System.out.println("Новое значение поля health элемента с id=" + spaceMarine.getId() + ": \"" +
                            health + "\"");
                }
            }catch (NumberFormatException e){
                System.out.println("Поле health представляет собой целое число!");
                health = 1L;
                System.out.println("Новое значение поля health элемента с id=" + spaceMarine.getId() + ": \"" +
                        health + "\"");
            }
        } else {
            System.out.println("У элемента с id=" + spaceMarine.getId() + "отсутствует поле health!");
            health = 1L;
            System.out.println("Новое значение поля health элемента с id=" + spaceMarine.getId() + ": \"" +
                    health + "\"");
        }
        return health;
    }

    public int setHeartCount(SpaceMarine spaceMarine, String s, GetValue getValue){
        int heartCount;
        if (s.contains("heartCount")) {
            String strHeartCount = getValue.getValue(s,"heartCount");
            try{
                heartCount = Integer.parseInt(strHeartCount.trim());
                if (heartCount > 3){
                    System.out.println("Поле heartCount не может быть больше 3!");
                    heartCount = 3;
                    System.out.println("Новое значение поля heartCount элемента с id=" + spaceMarine.getId() + ": \"" +
                            heartCount + "\"");
                }
                if (heartCount < 1){
                    System.out.println("Поле heartCount не может быть меньше 1!");
                    heartCount = 1;
                    System.out.println("Новое значение поля heartCount элемента с id=" + spaceMarine.getId() + ": \"" +
                            heartCount + "\"");
                }
            }catch (NumberFormatException e){
                System.out.println("Поле heartCount представляет собой целое число!");
                heartCount = 1;
                System.out.println("Новое значение поля heartCount элемента с id=" + spaceMarine.getId() + ": \"" +
                        heartCount + "\"");
            }
        } else {
            System.out.println("У элемента с id=" + spaceMarine.getId() + "отсутствует поле heartCount!");
            heartCount = 1;
            System.out.println("Новое значение поля heartCount элемента с id=" + spaceMarine.getId() + ": \"" +
                    heartCount + "\"");
        }
        return heartCount;
    }

    public Chapter setChapter(SpaceMarine spaceMarine, String s, GetValue getValue){
        if (s.contains("chapter")) {
            String chapter = getValue.getValue(s,"chapter");
            String name = chapter;
            int marinesCount = 0;
            if (chapter.contains(".")) {
                name = chapter.substring(0, chapter.indexOf("."));
                String strMarinesCount = chapter.substring(chapter.indexOf(".") + 1);
                try {
                    marinesCount = Integer.parseInt(strMarinesCount.trim());
                } catch (NumberFormatException e){
                    marinesCount = 1;
                }
            }else {
                System.out.println("Отсутствует поле marinesCount поля chapter!");
                System.out.println("Новое значение поля marinesCount элемента с id=" + spaceMarine.getId() +
                        ": \"" + marinesCount + "\"");
            }
            Chapter chapter1 = new Chapter();
            chapter1.setName(name);
            chapter1.setMarinesCount(String.valueOf(marinesCount));
            return chapter1;
        }else{
            System.out.println("Отсутствует поле chapter!");
            return null;
        }
    }
}
