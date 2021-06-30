package server.spacemarine;

import java.io.Serializable;
import java.util.Date;

public class SpaceMarine implements Serializable {
    private static final long serialVersionUID = 7460446497926355485L;
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long health = null; //Поле может быть null, Значение поля должно быть больше 0
    private int heartCount; //Значение поля должно быть больше 0, Максимальное значение поля: 3
    private String achievements = null; //Поле может быть null
    private AstartesCategory category = null; //Поле может быть null
    private Chapter chapter = null; //Поле может быть null

    public void setId(int id){this.id = id;}
    public void setName(String name){this.name = name;}
    public void setCoordinates(Coordinates coordinates){
        this.coordinates = coordinates;
    }
    public void setCreationDate(Date creationDate){
        this.creationDate = creationDate;
    }
    public void setHealth(Long health){this.health = health; }
    public void setHeartCount(int heartCount){this.heartCount = heartCount; }
    public void setAchievements(String achievements){this.achievements = achievements;}
    public void setCategory(String category){
        if (category.equals("SUPPRESSOR")){
            this.category = AstartesCategory.SUPPRESSOR;
        }
        if (category.equals("TERMINATOR")){
            this.category = AstartesCategory.TERMINATOR;
        }
        if (category.equals("LIBRARIAN")){
            this.category = AstartesCategory.LIBRARIAN;
        }
        if (category.equals("APOTHECARY")){
            this.category = AstartesCategory.APOTHECARY;
        }
    }
    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }
    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public Coordinates getCoordinates(){
        return coordinates;
    }
    public Date getCreationDate(){
        return creationDate;
    }
    public Long getHealth(){
        return health;
    }
    public int getHeartCount(){
        return heartCount;
    }
    public String getAchievements(){
        return achievements;
    }
    public AstartesCategory getCategory(){
        return category;
    }
    public Chapter getChapter(){
        return chapter;
    }
    @Override
    public String toString(){
        return this.getName() + " " + this.getId();
    }

}