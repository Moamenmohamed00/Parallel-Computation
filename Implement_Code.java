public class Implement_Code {
    public static void main(String[] args) {
    Thread t=new Th("MyThread");
    Thread t2=new Thread("MyThread");
    System.out.println(t2.getState());  
    t.setDaemon(true);   
    t.start();
    t2.start();
    System.out.println(t2.getState());
    System.out.println("Thread1 started "+t.getName());
    t.setName("fghjkl;");
    System.out.println("Thread name changed to "+Thread.currentThread().getName());
    System.out.println("Thread1 name changed to "+t.getName());
  //Thread.sleep(600);
  System.out.println("Thread name after sleep "+t.getName());
   try {
            Thread.sleep(1500); 
            System.out.println("Thread2 name after sleep " + t2.getName());
            System.out.println(t2.getState());
        } catch (InterruptedException e) {
            e.printStackTrace(); 
        }
        t.setDaemon(true);
        System.out.println("Thread1 ended " + t.getName());
        System.out.println(Thread.currentThread().getName());
        //Runnable r1=new r();
         Runnable r=()->System.out.println("Runnable thread is running");
    Thread t3=new Thread(r,"MyRunnableThread");
    t3.start();
    Runnable backgroundTask = () -> {
            try {
                while (true) {
                    System.out.println("... Daemon thread ...");
                    Thread.sleep(1000); 
                }
            } catch (InterruptedException e) {
            }
        };

        Thread t4 = new Thread(backgroundTask);
        
        t4.setDaemon(true); 
        t4.start();

        System.out.println("...Main thread run ...");
        try {
            Thread.sleep(3000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println(".. Main thread close..");
    }

}

 class Th extends Thread {
    String name;
    Th(String name){
       // this.name=name;
        super(name);
    }
    
    @Override
    public void run() {
        System.out.println("Thread is running");
    }
}
    class r implements Runnable {
        @Override
        public void run() {
            System.out.println("Runnable thread is running");
        }
    }