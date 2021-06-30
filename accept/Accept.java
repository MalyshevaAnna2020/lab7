package server.accept;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Accept extends Thread{
    private final SocketChannel client;

    public Accept(SocketChannel socketChannel){
        this.client = socketChannel;
    }

    @Override
    public void run() {
        try {
            ExecutorService executorRead = Executors.newCachedThreadPool();
            ExecutorService executorWork = Executors.newCachedThreadPool();
            ExecutorService executorWrite = Executors.newCachedThreadPool();

            Action action = new Action(client);
            while ((client.isOpen() && (!action.getExit()))) {
                Thread thread_read = new Thread(action::read);
                executorRead.submit(thread_read).get();

                if (action.getLogin() != null) {
                    Thread thread_work = new Thread(action::work);
                    executorWork.submit(thread_work).get();
                }else{
                    //
                    break;
                }

                if (action.getWork()) {
                    Thread thread_write = new Thread(action::write);
                    executorWrite.submit(thread_write).get();
                }else{
                    //
                    break;
                }
            }
        }
        catch (ClassCastException | NullPointerException e) {
            //
            System.out.println(e.getMessage());
        } catch (InterruptedException | ExecutionException e) {
            //
            e.printStackTrace();
        } finally {
            //
            System.out.println("Клиент отключился (" + Thread.currentThread() + ")!");
        }
    }
}
