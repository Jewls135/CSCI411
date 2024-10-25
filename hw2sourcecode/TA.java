import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TA implements Runnable {

    private dashboard board;
    private String name;
    private Lock doorLock;
    private Semaphore hallwayLock;
    private boolean sleeping = false;

    public TA(dashboard board, String name, Lock officeLock, Semaphore hLock) {
        this.name = name;
        this.board = board;
        this.hallwayLock = hLock;
        this.doorLock = officeLock;
    }

    public void run() {
        while (true) {
			// Checks if hallways is empty and no current student in the office (tryLock)
            if (hallwayLock.availablePermits() >= 3) {
                if (doorLock.tryLock()) {
                    try {
						doorLock.unlock();  
                        board.officeMessage("TA " + name + " is sleeping");
                        sleep();  // Go to sleep
                    } catch(Exception e) {
						
					}
                }
            }

            // Work with a student if one is present
            board.officeMessage("TA " + name + " is working with students");
            synchronized (this) {
                try {
                    wait();  // Wait for a student to notify that they are done
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private synchronized void sleep() {
        sleeping = true;
        try {
            wait();  // Wait to be woken up by a student
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Woken up");
        sleeping = false;
    }

    public boolean isSleeping() {
        return this.sleeping;
    }

    public synchronized void wakeUp() {
        notify();  // Notify the TA to wake up
    }
}
