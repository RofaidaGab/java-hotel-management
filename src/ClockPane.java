import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Calendar;

public class ClockPane extends Pane {

  private int hour;
  private int minute;
  private int second;

  private Timeline timeline;

  public ClockPane() {
    setPrefSize(250, 250);
    setMinSize(250, 250);

    widthProperty().addListener(e -> repaint());
    heightProperty().addListener(e -> repaint());

    setCurrentTime();
    startClock();
}


  // =====  CLOCK ENGINE =====
  private void startClock() {
    timeline = new Timeline(
        new KeyFrame(Duration.seconds(1), e -> {
          setCurrentTime();
          repaint();
        }));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
  }

  // =====  TIME LOGIC =====
  public void setCurrentTime() {
    Calendar calendar = Calendar.getInstance();
    hour = calendar.get(Calendar.HOUR_OF_DAY);
    minute = calendar.get(Calendar.MINUTE);
    second = calendar.get(Calendar.SECOND);
  }

  // ===== DRAWING  =====
  private void repaint() {
    double clockRadius = Math.min(getWidth(), getHeight()) * 0.8 * 0.5;
    double centerX = getWidth() / 2;
    double centerY = getHeight() / 2;

    // Draw circle
    Circle circle = new Circle(centerX, centerY, clockRadius);
    circle.setFill(Color.WHITE);
    circle.setStroke(Color.BLACK);
    Text t1 = new Text(centerX - 5, centerY - clockRadius + 12, "12");
    Text t2 = new Text(centerX - clockRadius + 3, centerY + 5, "9");
    Text t3 = new Text(centerX + clockRadius - 10, centerY + 3, "3");
    Text t4 = new Text(centerX - 3, centerY + clockRadius - 3, "6");

    // Draw second hand
    double sLength = clockRadius * 0.8;
    double secondX = centerX + sLength *
        Math.sin(second * (2 * Math.PI / 60));
    double secondY = centerY - sLength *
        Math.cos(second * (2 * Math.PI / 60));
    Line sLine = new Line(centerX, centerY, secondX, secondY);
    sLine.setStroke(Color.RED);

    // Draw minute hand
    double mLength = clockRadius * 0.65;
    double xMinute = centerX + mLength *
        Math.sin(minute * (2 * Math.PI / 60));
    double minuteY = centerY - mLength *
        Math.cos(minute * (2 * Math.PI / 60));
    Line mLine = new Line(centerX, centerY, xMinute, minuteY);
    mLine.setStroke(Color.BLUE);

    // Draw hour hand
    double hLength = clockRadius * 0.5;
    double hourX = centerX + hLength *
        Math.sin((hour % 12 + minute / 60.0) * (2 * Math.PI / 12));
    double hourY = centerY - hLength *
        Math.cos((hour % 12 + minute / 60.0) * (2 * Math.PI / 12));
    Line hLine = new Line(centerX, centerY, hourX, hourY);
    hLine.setStroke(Color.GREEN);

    getChildren().clear(); // Clear the pane
    getChildren().addAll(circle, t1, t2, t3, t4, sLine, mLine, hLine);
  }

  

  // ===== GETTERS  ======
  public int getHour() {
    return hour;
  }

  public int getMinute() {
    return minute;
  }

  public int getSecond() {
    return second;
  }

  // ===== OPTIONAL CONTROLS  =======
  public void start() {
    timeline.play();
  }

  public void stop() {
    timeline.stop();
  }
}
