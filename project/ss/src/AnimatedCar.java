import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class AnimatedCar implements Runnable {

    private final Pane pane;
    private final TrafficIntersection intersection;

    public AnimatedCar(Pane pane, TrafficIntersection intersection) {
        this.pane = pane;
        this.intersection = intersection;
    }

    @Override
    public void run() {

        // شكل العربية
        Rectangle car = new Rectangle(30, 15, Color.RED);
        car.setX(285);
        car.setY(0);

        // إضافة العربية للـ GUI
        Platform.runLater(() -> pane.getChildren().add(car));

        // قبل التقاطع
        move(car, 0, 200);

        // انتظار الإشارة (Semaphore)
        intersection.enter();

        // داخل التقاطع
        move(car, 200, 300);

        // الخروج من التقاطع
        intersection.exit();

        // بعد التقاطع
        move(car, 300, 520);

        // إزالة العربية بعد ما تخرج
        Platform.runLater(() -> pane.getChildren().remove(car));
    }

    private void move(Rectangle car, double fromY, double toY) {
        try {
            Platform.runLater(() -> {
                TranslateTransition tt =
                        new TranslateTransition(Duration.seconds(2), car);
                tt.setFromY(fromY);
                tt.setToY(toY);
                tt.play();
            });
            Thread.sleep(2000); // تزامن مع الأنيميشن
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
