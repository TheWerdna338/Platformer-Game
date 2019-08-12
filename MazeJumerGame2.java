package mazejumergame2;

import java.io.FileNotFoundException;
import javafx.application.Application;
import javafx.stage.Stage;

public class MazeJumerGame2 extends Application {
    
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        Screens startupScreens = new Screens();
        startupScreens.getStartScreen(primaryStage);
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
