package server.spacemarine;

import java.io.Serializable;

public class Coordinates implements Serializable{
    private static final long serialVersionUID = 2954344353170385598L;
    private double x; // Максимальное значение поля: 955
    private int y;

    public void setX(String x){
        try {
            this.x = Double.parseDouble(x);
            if (this.x > 955){
                System.out.println("Максимальное значение поля x поля coordinates: 955. Введенное значение больше 955!");
                this.x = 955;
                System.out.println("х = " + this.x);
            }
        }catch (NumberFormatException e){
            System.out.println("Поле x поля coordinates представляет собой целое число!");
            this.x = 0;
            System.out.println("х = " + this.x);
        }
    }
    public void setY(String y){
        try {
            this.y = Integer.parseInt(y);
        }catch (NumberFormatException e){
            System.out.println("Поле y поля coordinates представляет собой целое число!");
            this.y = 0;
            System.out.println("y = " + this.y);
        }
    }
    public double getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    @Override
    public String toString(){
        return this.getX() + " " + this.getY();
    }
}
