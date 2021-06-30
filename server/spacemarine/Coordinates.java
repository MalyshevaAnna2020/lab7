package server.spacemarine;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private static final long serialVersionUID = 2954344353170385598L;
    private double x; //Максимальное значение поля: 955
    private int y;

    public void setX(double x){this.x = x;}
    public void setY(int y){this.y = y;}
    public double getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public String toString(){
        return this.getX() + " " + this.getY();
    }
}
