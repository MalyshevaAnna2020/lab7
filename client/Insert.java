package client;

import java.util.Scanner;

public class Insert {

    public String insertName(){
        Scanner in = new Scanner(System.in);
        System.out.print("Введите значение поля name: ");
        String name = in.nextLine();
        if (name.length() == 0){
            System.out.println("\nПоле name пустой строкой!");
            return insertName();
        }
        return name;
    }

    public double insertX(){
        Scanner in = new Scanner(System.in);
        System.out.print("Введите начение поля X поля coordinates (максимальное значение равно 955): ");
        try {
            double x = Double.parseDouble(in.nextLine());
            if (x > 955) {
                System.out.println("\nПоле X поля coordinates не может быть больше 955!");
                return insertX();
            }
            return x;
        } catch (NumberFormatException e) {
            System.out.println("\nПоле X поля coordinates представляет собой целое число!");
            return insertX();
        }
    }

    public int insertY(){
        Scanner in = new Scanner(System.in);
        System.out.print("Введите значение поля Y поля coordinates (целое число): ");
        try {
            return Integer.parseInt(in.nextLine());
        }catch (NumberFormatException e){
            System.out.println("\nПоле Y поля coordinates представляет собой целое число!");
            return insertY();
        }
    }

   public Long insertHealth(){
        Scanner in = new Scanner(System.in);
        System.out.print("Введите значение поля health (целое неотрицательное число): ");
        try {
            long health = Long.parseLong(in.nextLine());
            if (health <= 0){
                System.out.println("\nПоле health не может быть меньше 0!");
                return insertHealth();
            }
            return health;
        }catch (NumberFormatException e){
            System.out.println("\nПоле health представляет собой целое число!");
            return insertHealth();
        }
    }

   public int insertHeartCount(){
        Scanner in = new Scanner(System.in);
        System.out.print("Введите значение поля HeartCount (1, 2 или 3): ");
        try {
            int heartCount = Integer.parseInt(in.nextLine());
            if ((heartCount < 1) || (heartCount > 3)){
                System.out.println("\nПоле HeartCount может принимать 1, 2 или 3!");
                return insertHeartCount();
            }
            return heartCount;
        }catch (NumberFormatException e){
            System.out.println("\nПоле HeartCount может принимать 1, 2 или 3!");
            return insertHeartCount();
        }
   }

   public String insertAchievements(){
        Scanner in = new Scanner(System.in);
        System.out.print("Введите значение поля achievements: ");
        return in.nextLine();
   }

    public String insertCategory(){
        Scanner in = new Scanner(System.in);
        System.out.println("Введите значение category из следующих: \nSUPPRESSOR,\nTERMINATOR,\nLIBRARIAN,\nAPOTHECARY");
        String s = in.nextLine();
        if ((s.equals("SUPPRESSOR"))|| (s.equals("TERMINATOR")) || (s.equals("LIBRARIAN"))  || (s.equals("APOTHECARY"))) {
            return s;
        }else{
            return insertCategory();
        }
    }

    public String insertNameOfChapter(){
        Scanner in = new Scanner(System.in);
        System.out.println("Введите значение поля name поля Chapter: ");
        String name = in.nextLine();
        if (name.length() == 0){
            System.out.println("\nПоля name поля Chapter не может быть пустой строкой");
            return insertNameOfChapter();
        }
        return name;
    }

    public int insertMarinesCount(){
        Scanner in = new Scanner(System.in);
        System.out.print("Введите значение поля marinesCount поля Chapter (натуральное число): ");
        try {
            int marinesCount = Integer.parseInt(in.nextLine());
            if (marinesCount < 1){
                System.out.println("\nПоле marinesCount поля Chapter не может быть меньше 1!");
                return insertMarinesCount();
            }
            return marinesCount;
        }catch (NumberFormatException e){
            System.out.println("\nПоля marinesCount поля Chapter представляет собой натуральное число!");
            return insertMarinesCount();
        }
    }


}
