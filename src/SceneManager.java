import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Stack;

public class SceneManager {
    public static final int listViewMaxWidth = 500;
    public static final int listViewMaxHeight = 300;
    public static final int maxWidth = 500;
    public static final int maxHeight = 700;
    private static Stage stage;
    private static Stack<Scene> sceneHistory = new Stack<>();
    private static HomePageRoot homePageRoot; // Store reference to home page

    public static void setStage(Stage s) {
        stage = s;
        stage.setTitle("Hotel Management System");
        stage.setHeight(maxHeight);
        stage.setWidth(maxWidth);
    }

    // Switch to a new scene and add current scene to history
    public static void switchTo(Scene scene) {
        if (stage.getScene() != null) {
            sceneHistory.push(stage.getScene());
        }
        stage.setScene(scene);
    }

    // Go back to the previous scene
    public static void goBack() {
        if (!sceneHistory.isEmpty()) {
            Scene previousScene = sceneHistory.pop();
            stage.setScene(previousScene);
        }
    }

    // Check if we can go back
    public static boolean canGoBack() {
        return !sceneHistory.isEmpty();
    }

    // Clear history (for logout)
    public static void clearHistory() {
        sceneHistory.clear();
        homePageRoot = null; // Clear home page reference on logout
    }

    // Switch without adding to history (for replacing scenes)
    public static void switchToWithoutHistory(Scene scene) {
        stage.setScene(scene);
    }
    
    // Set the home page root to be able to refresh
    public static void setHomePageRoot(HomePageRoot root) {
        homePageRoot = root;
    }
    
    // Refresh the home page display if it exists
    public static void refreshHomePage() {
        if (homePageRoot != null) {
            homePageRoot.refreshDisplay();
        }
    }
}