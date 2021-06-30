package server.spacemarine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

public class SpaceMarine implements Serializable {
    private static final long serialVersionUID = 7460446497926355485L;
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long health = 0L; //Значение поля должно быть больше 0
    private int heartCount; //Значение поля должно быть больше 0, Максимальное значение поля: 3
    private String achievements = null; //Поле может быть null
    private AstartesCategory category = null; //Поле может быть null
    private Chapter chapter = null; //Поле может быть null
    private String user;

    // сеттеры
    public void setId(int id){this.id = id;}
    public void setName(String name){this.name = name;}
    public void setCoordinates(Coordinates coordinates){ this.coordinates = coordinates; }
    public void setCreationDate(Date creationDate){ this.creationDate = creationDate; }
    public void setHealth(long health){ this.health = health; }
    public void setHeartCount(int heartCount){ this.heartCount = heartCount; }
    public void setAchievements(String achievements){this.achievements = achievements;}
    public void setCategory(String category){
        switch (category) {
            case "SUPPRESSOR":
                this.category = AstartesCategory.SUPPRESSOR;
                break;
            case "TERMINATOR":
                this.category = AstartesCategory.TERMINATOR;
                break;
            case "LIBRARIAN":
                this.category = AstartesCategory.LIBRARIAN;
                break;
            case "APOTHECARY":
                this.category = AstartesCategory.APOTHECARY;
                break;
            default:
                System.out.println("Значение поля category элемента с id=" + this.getId() + " равно null!");
                break;
        }
    }
    public void setChapter(Chapter chapter) { this.chapter = chapter;}
    public void setUser(String user) {this.user = user;}

    // геттеры
    public int getId(){ return id;}
    public String getName(){ return name;}
    public Coordinates getCoordinates(){ return coordinates;}
    public Date getCreationDate(){ return creationDate;}
    public Long getHealth(){ return health;}
    public int getHeartCount(){ return heartCount;}
    public String getAchievements(){ return achievements;}
    public AstartesCategory getCategory(){ return category;}
    public Chapter getChapter(){ return chapter;}
    public String getUser(){ return user;}

    // toString()
    @Override
    public String toString(){
        return this.getName() + " " + this.getId();
    }

    // Обработка элементов из файла
    public void setSpaceMarine(String s, int id) {

        // Большое лямбда-выражение
        GetValue getValue = (st, subst) -> st.substring(st.indexOf(subst))
                .substring(st.substring(st.indexOf(subst)).indexOf("\"") + 1, st.substring(st.indexOf(subst))
                .substring(st.substring(st.indexOf(subst)).indexOf("\"") + 1)
                        .indexOf("\"") + st.substring(st.indexOf(subst)).indexOf("\"") + 1);

        // Определение SpaceMarine
        this.setId(id);

        SetSpaceMarineFromFile setMarine = new SetSpaceMarineFromFile();
        this.setName(setMarine.setName(this, s, getValue));
        this.setCoordinates(setMarine.setCoordinates(this, s, getValue));
        this.setCreationDate(new Date());
        this.setHealth(setMarine.setHealth(this, s, getValue));
        this.setHeartCount(setMarine.setHeartCount(this, s, getValue));

        if (s.contains("achievements")) { this.setAchievements(getValue.getValue(s,"achievements"));}
        if (s.contains("category")) { this.setCategory(getValue.getValue(s, "category")); }

        this.setChapter(setMarine.setChapter(this, s, getValue));
    }

    public int getSize(){

        try{
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

            objectOutputStream.writeObject(this);
            return byteArrayOutputStream.size();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Что-то пошло не так?
        return -1;
    }
}