package server.accept;

import server.database.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static final int PORT = 4002;

    public static void main(String [] args) throws ClassNotFoundException, SQLException, IOException {
        DataBase dataBase = DataBase.getInstance();
        dataBase.connect();
        //dataBase.createTable();
        System.out.println(dataBase.getCollection());
        //dataBase.createHashTable();

        System.out.println("Клиенты могут подключаться!");
        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(PORT));
        server.configureBlocking(false);
        Selector selector = Selector.open();
        SelectionKey key = server.register(selector, SelectionKey.OP_ACCEPT);
        ExecutorService executorService = Executors.newCachedThreadPool();
        while(true){
            selector.select();
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()){
                ServerSocketChannel server_now = (ServerSocketChannel) key.channel();
                try{
                    SocketChannel client = null;
                    while(client == null){
                        client = server_now.accept();
                    }
                    Accept accept = new Accept(client);
                    executorService.execute(accept);
                }catch (IOException i){
                    System.out.println(i.getMessage());
                    return;
                }

                iterator.next();
                iterator.remove();
            }
        }
    }
}
