public class Car implements Runnable {

    private final int id;
    private final Direction direction;
    private final TrafficIntersection intersection;

    public Car(int id, Direction direction, TrafficIntersection intersection) {
        this.id = id;
        this.direction = direction;
        this.intersection = intersection;
    }

    @Override
    public void run() {
        intersection.enterIntersection(this);
    }

    @Override
    public String toString() {
        return "Car-" + id + " [" + direction + "]";
    }
}
