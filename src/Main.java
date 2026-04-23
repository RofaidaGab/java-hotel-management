import javafx.application.Application;
import javafx.stage.Stage;
import java.util.ArrayList;
import javafx.scene.Scene;

public class Main extends Application {

  @Override
  public void start(Stage stage) {
 
    ArrayList<Employee> employees = HotelData.getEmployees();
    LoginAgent.giveEmployees(employees);
    SceneManager.setStage(stage);

    Scene loginScene = new Scene(new LoginRoot(), 500, 500);
    stage.setScene(loginScene);
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }

}
