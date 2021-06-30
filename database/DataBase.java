package server.database;

import server.spacemarine.Chapter;
import server.spacemarine.Coordinates;
import server.spacemarine.SpaceMarine;

import java.sql.*;
import java.util.Arrays;
import java.util.Hashtable;

public class DataBase {
    private Hashtable<String, SpaceMarine> hashtable;
    public void setHashtable(Hashtable<String, SpaceMarine> hashtable){this.hashtable = hashtable;}
    public Hashtable<String, SpaceMarine> getHashtable() {
        return hashtable;
    }

    private static final String url = "jdbc:postgresql://pg:5432/studs";
    private static final String name = "s311793";
    private static final String password = "ndj043";

    private static Connection connection = null;

    private static DataBase instance = null;

    private DataBase(){ }

    public static DataBase getInstance() {
        if (instance == null) instance = new DataBase();
        return instance;
    }

    public void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        System.out.println("Драйвер подключен!");
        connection = DriverManager.getConnection(url, name, password);
        System.out.println("Соединение с базой данных установлено!");
    }

    /*
    synchronized public void createHashTable() throws SQLException {
        // Создание таблицы
        Statement statement = connection.createStatement();
        statement.executeUpdate("DROP TABLE users");
        String sql = "CREATE TABLE users (name text not NULL UNIQUE, hash text)";
        statement.executeUpdate(sql);
        statement.executeUpdate("DROP SEQUENCE s1");
        statement.executeUpdate("create sequence s1 start with 1 increment by 1");
    }

     */
    synchronized public boolean findUser(String user) throws SQLException {
        ResultSet resultCollection = connection.prepareStatement("SELECT * FROM users;").executeQuery();
        while(resultCollection.next()) {
            String name = resultCollection.getString("name");
            if (user.equals(name)) {
                return true;
            }
        }
        return false;
    }
    synchronized public boolean findPassword(byte[] password) throws SQLException {
        ResultSet resultCollection = connection.prepareStatement("SELECT * FROM users;").executeQuery();
        while(resultCollection.next()) {
            if (Arrays.toString(password).equals(resultCollection.getString("hash"))) {
                return true;
            }
        }
        return false;
    }
    synchronized public boolean findName(String name, byte[] password) throws SQLException {
        ResultSet resultCollection = connection.prepareStatement
                ("SELECT * FROM users WHERE (name = '" + name + "') and " +
                        "(hash = '" + Arrays.toString(password) + "');")
                .executeQuery();
        int count = 0;
        while (resultCollection.next()){
            count++;
        }
        return count != 0;
    }

    synchronized public void addUser(String user, byte[] hash) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("INSERT INTO users " +
                "(name, hash)" +
                " VALUES ('" + user + "', '" + Arrays.toString(hash) + "')");
    }

    synchronized public void createTable() throws SQLException {
        // Создание таблицы
        Statement statement = connection.createStatement();
        statement.executeUpdate("DROP TABLE SPACEMARINE");
        String sql = "CREATE TABLE SPACEMARINE " +
                "(person text not NULL UNIQUE," +
                " key text not NULL UNIQUE," +
                " id INTEGER not NULL UNIQUE CHECK(id > 0)," +
                " name text not NULL," +
                " x DECIMAL(5) not NULL CHECK(x < 955), " +
                " y INTEGER not NULL, " +
                " creationDate timestamp not NULL," +
                " health BIGINT CHECK(health > 0)," +
                " heartCount INTEGER CHECK((heartCount > 0) AND (heartCount < 4))," +
                " achievements text NULL," +
                " category text NULL," +
                " nameChapter text not NULL," +
                " marinesCount INTEGER CHECK(marinesCount > 0)," +
                " PRIMARY KEY (id))";
        statement.executeUpdate(sql);
        statement.executeUpdate("DROP SEQUENCE s");
        statement.executeUpdate("create sequence s start with 1 increment by 1");
    }

    synchronized public void addSpaceMarine(SpaceMarine spaceMarine, String key) throws SQLException {
        Statement statement = connection.createStatement();

        statement.executeUpdate("INSERT INTO SPACEMARINE" +
                "(person, key, id, name, x,y,creationDate, health, heartCount, achievements, category, nameChapter, marinesCount)" +
                " VALUES ('" + spaceMarine.getUser() + "', " +
                "'" + key + "', " +
                "nextval('s'), " +
                "'" + spaceMarine.getName() + "', " +
                "'" + spaceMarine.getCoordinates().getX() + "', " +
                "'" + spaceMarine.getCoordinates().getY() + "', " +
                "'" + spaceMarine.getCreationDate() +"', " +
                "'" + spaceMarine.getHealth() + "', " +
                "'" + spaceMarine.getHeartCount() + "', " +
                "'" + spaceMarine.getAchievements() + "', " +
                "'" + spaceMarine.getCategory().toString() + "', " +
                "'" + spaceMarine.getChapter().getName() + "', " +
                "'" + spaceMarine.getChapter().getMarinesCount() + "')\n");
    }


    synchronized public String getCollection() throws SQLException {
        ResultSet resultCollection = connection.prepareStatement("SELECT * FROM SPACEMARINE").executeQuery();
        String result = "";
        int count = 0;
        while(resultCollection.next()){
            count++;
            result ="user=" + resultCollection.getString("person").trim() + ", "+
                    "key=" + resultCollection.getString("key").trim() + ", " +
                    "id=" + resultCollection.getInt("id") + ", " +
                    "name=" + resultCollection.getString("name").trim() + ", " +
                    "coordinates=[" + resultCollection.getDouble("x") + " " +
                    resultCollection.getInt("y") + "], " +
                    "creationDate=" + resultCollection.getTimestamp("creationDate") + ", " +
                    "health=" + resultCollection.getLong("health") + ", " +
                    "heartCount=" + resultCollection.getInt("heartCount") + ", " +
                    "achievements=" + resultCollection.getString("achievements").trim() + ", " +
                    "category=" + resultCollection.getString("category").trim() + ", " +
                    "chapter=[name: " + resultCollection.getString("nameChapter").trim() + "; " +
                    "marinesCount: " + resultCollection.getInt("marinesCount") + "]\n";
        }
        if (count == 0){
            result = "В коллекции пока нет элементов!";
        }
        return result;
    }

    synchronized public Hashtable<String, SpaceMarine> updateCollection() throws SQLException {
        ResultSet resultCollection = connection.prepareStatement("SELECT * FROM SPACEMARINE;").executeQuery();
        Hashtable<String, SpaceMarine> hashtable = new Hashtable<>();
        int count = 0;

        while(resultCollection.next()){
            String key = resultCollection.getString("key").trim();
            System.out.println(key);
            int id = resultCollection.getInt("id");
            String name = resultCollection.getString("name").trim();
            double x = resultCollection.getDouble("x");
            int y = resultCollection.getInt("y");
            Date creationDate = new Date(resultCollection.getTimestamp("creationDate").getTime());
            long health = resultCollection.getLong("health");
            int heartCount = resultCollection.getInt("heartCount");
            String achievements = resultCollection.getString("achievements").trim();
            String category = resultCollection.getString("category").trim();
            String nameChapter = resultCollection.getString("nameChapter").trim();
            int marinesCount = resultCollection.getInt("marinesCount");
            String user = resultCollection.getString("person").trim();

            Coordinates coordinates = new Coordinates();
            coordinates.setX(String.valueOf(x));
            coordinates.setY(String.valueOf(y));

            Chapter chapter = new Chapter();
            chapter.setName(nameChapter);
            chapter.setMarinesCount(String.valueOf(marinesCount));

            SpaceMarine spaceMarine = new SpaceMarine();
            spaceMarine.setName(name);
            spaceMarine.setId(id);
            spaceMarine.setCoordinates(coordinates);
            spaceMarine.setCreationDate(creationDate);
            spaceMarine.setHealth(health);
            spaceMarine.setHeartCount(heartCount);
            spaceMarine.setAchievements(achievements);
            spaceMarine.setCategory(category);
            spaceMarine.setChapter(chapter);
            spaceMarine.setUser(user);

            hashtable.put(key, spaceMarine);
            count++;
        }
        System.out.println("Всего элементов в коллекции: " + count);
        return hashtable;
    }

    synchronized public void newTable(Hashtable<String, SpaceMarine> hashtable) throws SQLException {
        DataBase.getInstance().createTable();
        for (String key : hashtable.keySet()){
            DataBase.getInstance().addSpaceMarine(hashtable.get(key), key);
        }
    }
}
