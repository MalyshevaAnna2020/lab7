package server.spacemarine;

import java.io.Serializable;

public class Chapter implements Serializable{
    private static final long serialVersionUID = 91592442311309654L;
    private String name; //Поле не может быть null, Строка не может быть пустой
    private int marinesCount; //Значение поля должно быть больше 0, Максимальное значение поля: 1000

    public void setName(String nameOfChapter){name = nameOfChapter;}
    public void setMarinesCount(String marinesCount){
        try {
            this.marinesCount = Integer.parseInt(marinesCount);
            if (this.marinesCount < 1) this.marinesCount = 1;
            if (this.marinesCount > 1000) this.marinesCount = 1000;
        } catch (NumberFormatException e){
            this.marinesCount = 1;
        }
    }

    public int getMarinesCount() {
        return marinesCount;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString(){
        return this.getName() + ". " + this.getMarinesCount();
    }

}
