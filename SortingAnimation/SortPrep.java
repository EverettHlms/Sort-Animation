import java.util.*;

/**
   This runnable executes a sort algorithm.
   When two elements are compared, the algorithm
   pauses and updates a panel.
*/
public class SortPrep 
{
   private Double[] values;
   private ArrayComponent panel;
   private static final int DELAY = 100;
   /**
      Constructs the sorter.
      @param values the array to sort
      @param panel the panel for displaying the array
   */
   public SortPrep(Double[] values, ArrayComponent panel)
   {
      this.values = values;
      this.panel = panel;
   }

   public void prepareToSort()
   {
      // Comparator is a 'funtional interface' provided by Java that
      // has default methods that can be overridden.  A Functional Interface
      // can be thought of a group of methods that are not associated
      // with any class
      Comparator<Double> comp = new
         Comparator<Double>()
         {
            public int compare(Double d1, Double d2)
            {
               // Draw the bars with the current sorting of the arrays
               // and fill the ones whose values match d1 and d2.
               panel.setValues(values, d1, d2);
               // Sleep a while and then when the sleep returns an exception
               // interrupt who ever else is running and run me again.
               try
               {
                  Thread.sleep(DELAY);
               }
               catch (InterruptedException exception)
               {
                  Thread.currentThread().interrupt();
               }
               // Return 0 if d1 and d2 are equal.
               // negative integer if d1 is less than d2
               // positive integer if d2 is greater than d2
               return (d1).compareTo(d2);
            }
         };
      int quarter = values.length/4;
      int firstMid = quarter;
      int secondMid = quarter * 2;
      int thirdMid = quarter * 3;
      
      Runnable r1 = new MergeSorter<>(values,0,firstMid,comp);
      Runnable r2 = new MergeSorter<>(values,firstMid+1,secondMid,comp);
      Runnable r3 = new MergeSorter<>(values,secondMid+1,thirdMid,comp);
      Runnable r4 = new MergeSorter<>(values,thirdMid+1,values.length-1,comp);

      Thread t1 = new Thread(r1,"1st Quarter");      
      Thread t2 = new Thread(r2,"2nd Quarter");
      Thread t3 = new Thread(r3,"3rd Quarter");
      Thread t4 = new Thread(r4,"4th Quarter");

      t1.start();
      t2.start();
      t3.start();
      t4.start();
      try {
         t1.join();
         t2.join();
         t3.join();
         t4.join();
      } catch (InterruptedException e) {
         System.out.println("Warning: Could not join at least one thread: " + e);
      } finally {
         MergeSorter.merge(values, 0, firstMid, secondMid, comp);
         MergeSorter.merge(values, secondMid+1, thirdMid, values.length-1, comp);
         MergeSorter.merge(values, 0, secondMid, values.length-1, comp);
         panel.setValues(values, null, null);
      }
   }
      




}
