/**
 * tutorRoom.java
 *
 *
 */
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class tutorRoom
{
   public static void main(String args[]) {
   	dashboard board = new dashboard();
      String[] names = {"Mary", "Emma","Jennifer","Mike","Alan","Bruce","Tom"};
      Thread[] collegestudents = new Thread[7];
      // Lock mutexBoardLock = new ReentrantLock(); // Remove if you use other tools
      Semaphore hallwayLock = new Semaphore(3);
      
      ReentrantLock officeLock = new ReentrantLock();
      String  TAname= "David";
      TA ta = new TA(board,TAname,  officeLock, hallwayLock);
      Thread  teachingAssistant = new Thread(ta);
      for(int i = 0; i < 7; i++)
         collegestudents[i] = new Thread(new students(board,hallwayLock, officeLock, names[i], ta));

      teachingAssistant.start();
      for (int i = 0; i < 7; i++)
         collegestudents[i].start();
   }
}

