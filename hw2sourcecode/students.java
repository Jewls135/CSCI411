import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class students implements Runnable {

    private dashboard board;
    private Semaphore hallwayLock;
    private Lock officeLock;
    private String name;
    private TA ta;

    public students(dashboard board, Semaphore lock, ReentrantLock doorLock, String name, TA assignedTA) {
        this.name = name;
        this.board = board;
        this.hallwayLock = lock;
        this.officeLock = doorLock;
        this.ta = assignedTA;
    }

    public void run() {
        while (true) {
            SleepUtilities.nap(60);		
            board.postMessage(this.name + " needs help"); 

            // Attempt to go into the office
            try {
                hallwayLock.acquire();  
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); 
                return; 
            }

            board.waitHallway(this.name);

            // Attempt to enter the TA's office
            officeLock.lock();
            hallwayLock.release();
            board.enterRoom(this.name);
            board.leaveHallway(this.name);
            try {
                if (ta.isSleeping()) {
                    ta.wakeUp();  // Wake up the TA if they're sleeping
                }
            } catch(Exception e) {

            }
            SleepUtilities.nap();

            // Leave the office
            board.leaveRoom(this.name);
            officeLock.unlock();
            
            synchronized (ta) {
                ta.notify();  // Notify TA that we are finished
            }
           
        }
    }
}
