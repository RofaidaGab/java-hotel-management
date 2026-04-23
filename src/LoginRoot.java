import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LoginRoot extends StackPane {

    private TextField usernameTf = new TextField();
    private PasswordField passwordTf = new PasswordField();
    private Label errLabel = new Label("");

    public LoginRoot() {
        makeRoot();
    }

    private void makeRoot() {
        VBox vBox = new VBox(20);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(10));

        Label hotelName = new Label("Hotel De Luna");
        hotelName.setTextFill(Color.NAVY);
        hotelName.setFont(Font.font("Calibri", FontWeight.BOLD, 30));

        // Show the clock:
        ClockPane clock = new ClockPane();

        GridPane grid = new GridPane(20, 20);
        grid.setAlignment(Pos.CENTER);
        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameTf, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordTf, 1, 1);

        Button loginBtn = new Button("Login");
        loginBtn.setOnAction(e -> doneLogin());

        // Focus When enter key is pressed
        usernameTf.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                passwordTf.requestFocus();
        });
        passwordTf.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                doneLogin();
        });

        errLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        errLabel.setTextFill(Color.RED);

        vBox.getChildren().addAll(
                hotelName,
                clock,
                grid,
                loginBtn,
                errLabel);

        getChildren().add(vBox);
    }

    private void doneLogin() {
        String username = usernameTf.getText();
        String password = passwordTf.getText();

        boolean success = LoginAgent.validate(username, password);

        if (success) {
            errLabel.setText(""); // Clear error on success
            SceneManager.clearHistory();
            Scene homeScene = new Scene(new HomePageRoot());
            SceneManager.switchToWithoutHistory(homeScene);
        } else {

            errLabel.setText("Invalid Login Attempt!");
        }
    }
}