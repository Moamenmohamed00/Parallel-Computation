import java.util.ArrayList;
import java.util.List;
public class Lab_Section3 {
    public static void main(String[] args) {
    List<Runnable> runnables = new ArrayList<>();
runnables.add(() -> {
    Thread t1=new Thread("Thread-1");
    try {//sleep t1
        Thread.sleep(1000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    System.out.println(t1.getName() + " is running");
});
runnables.add(() -> {
    Thread t2=new Thread("Thread-2");
    System.out.println(t2.getName() + " is running");});
     try {//that sleep main thread
        Thread.sleep(1000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
runnables.add(() -> {
    Thread t3=new Thread("Thread-3");
    try {//sleep t3
       Thread.sleep(1000);
   } catch (InterruptedException e) {
       e.printStackTrace();
   }
    System.out.println(t3.getName() + " is running");
});
MultiExecutorthat multiExecutor = new MultiExecutorthat(runnables);
multiExecutor.executeAll();
 }
}
 class MultiExecutorthat extends Thread{
 private List<Runnable> tasks;
  MultiExecutorthat(List<Runnable> tasks){
    this.tasks=tasks;
  }
  public void executeAll(){
    for(Runnable r:tasks){
      Thread t=new Thread(r);
      t.start();
    }
  }
}

    
