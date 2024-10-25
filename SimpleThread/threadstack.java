import java.io.*;
import java.util.*;
public class threadstack {

    public static void main(final String[] args) {

        // Create two threads where A is an instance of anyThread class
        // B is an instance of anyRunnable
        anyThread A = new anyThread(1);
        Thread B = new Thread(new anyRunnable());
        // check the state of thread 
        System.out.println("Thread A state: " + A.getState());

        // Start the two threads
        A.start();
        B.start();

        
        System.out.println("Thread A state: " + A.getState());
        System.out.println("Thread B state: " + B.getState());
        
        // create more threads 
        
        Thread C = new Thread(new simpleRunnable("C"));
        Thread D = new Thread(new Runnable(){
            public void run(){
                while(true){
                    Random rand = new Random();
                    System.out.println("Thread D: " + rand.nextInt(1000));
                    try{
                        Thread.sleep(500);
                    }catch(InterruptedException e){
                        System.out.println("Interrupted");
                        break;
                    }
                }
            }
        });
        Runnable task = () -> {
            System.out.print("task printout");
        };
        Thread E = new Thread(task);
        C.start();
        D.start();
        E.start();
        C.interrupt();
        
        try{
            A.join();
            B.join();
            System.out.println("Thread A state: " + A.getState());
            System.out.println("Thread B state: " + B.getState());
            System.out.println("Thread C state: " + D.getState());
            System.out.println("Thread D state: " + C.getState());
            System.out.println("Thread E state: " + E.getState());
            C.join();
            while (D.isAlive()){
                D.interrupt();
            }
        }catch(InterruptedException e){
            System.out.println("Interrupted");
        }

        System.out.println("Main thread is done");
    }
}
