/* A simple code that demonstrate use if statement  */

import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GameRoomSimulation {
    private static int MAXIMUM_THREAD = 10;
    private static int intheRoom = 0; // measure number of the player in the room

    public static void main(String[] args) {

        // fixed size thread pool
        ExecutorService pool = java.util.concurrent.Executors.newFixedThreadPool(MAXIMUM_THREAD);
        Semaphore roomPermit = new Semaphore(3);
        Lock lock = new ReentrantLock();
        System.out.println("Start multithreading");

        // player
        Runnable player = () -> {
            while (true) {
                System.out.println("Player " + Thread.currentThread().threadId() + " wants to join the room");
                // Entry
                roomPermit.acquire();

                // Critical section

                synchronized(lock){intheRoom++;}
                System.out.println("Player " + Thread.currentThread().threadId() + " is in the room");
                System.out.println(intheRoom + " players in the room");
                SleepUtilities.nap();
                synchronized(lock){intheRoom--;}
                // Exit
                roomPermit.release();
            }
        };

        // Start the task with two players
        pool.execute(player);
        pool.execute(player);
        pool.execute(player);
        pool.execute(player);
        pool.execute(player);
        pool.shutdown(); // wait for termination

        try {
            if (!pool.awaitTermination(3600, TimeUnit.SECONDS)) {
                // wait for too long
                pool.shutdownNow();
                System.err.println("Tasks are not completed");
            } else {
                // done
                System.out.println("Simulation is over");
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
        }

    }
}