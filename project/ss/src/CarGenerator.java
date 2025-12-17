import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CarGenerator {

    private final ExecutorService pool = Executors.newFixedThreadPool(5);
    private final TrafficIntersection intersection;
    private int carId = 1;
    private final Random random = new Random();

    public CarGenerator(TrafficIntersection intersection) {
        this.intersection = intersection;
    }

    public void generateCar() {
        Direction dir = Direction.values()[random.nextInt(4)];
        Car car = new Car(carId++, dir, intersection);
        pool.execute(car);
    }

    public void shutdown() {
        pool.shutdown();
    }
}
