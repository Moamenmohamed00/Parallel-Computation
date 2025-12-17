import java.util.concurrent.Semaphore;

public class TrafficIntersection {

    // عدد العربيات المسموح بيها داخل التقاطع
    private final Semaphore semaphore = new Semaphore(2);

    public void enter() {
        try {
            semaphore.acquire(); // P
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void exit() {
        semaphore.release(); // V
    }
}
