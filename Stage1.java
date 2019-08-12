package mazejumergame2;

import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Stage1 {

   
    Stage1() {
    }

    public ImageView getBackdrop1() {
        Image backdropImage = new Image("file:/Users/andrewdedich/Desktop/pixil-frame-1.png", 2000, 800, false, true);//declare it as a file: as it tells the program that it is a file on the system and not a url
        ImageView imageView = new ImageView(backdropImage);

        return imageView;
    }

    public ImageView getBackdrop2() {
        Image backdropImage = new Image("file:/Users/andrewdedich/Desktop/pixil-frame-1.png", 2000, 800, false, true);
        ImageView imageView2 = new ImageView(backdropImage);

        return imageView2;
    }

    public Group obstacleLayout() {
        Canvas canvas = new Canvas(4000, 800); //I create a canvas node so that i can specify a width and height by which 
                                             //i can add rectangles to certain coordinates on this canvas
                                             
        Group groupTranslate = new Group(canvas);//i add this canvas node to a Group root node so that i can use the 
                                     //getChildren() method and add the rectangles onto it and so these rectangles' coordinates can
                                     //Actually be TRANSLATED along the scene. The coordinates wont be translated if i used Pane root node 
                                     //for instance

        
        for (Rectangle rectList : rectangularObstaclesLayout()) {
            groupTranslate.getChildren().add(rectList);
        }

        return groupTranslate;
    }

    public ArrayList<Rectangle> rectangularObstaclesLayout() {
        ArrayList<Rectangle> rectArrayList = new ArrayList<>(); //i used an arrayList because it means i can keep on adding rectangles and dont have to specify a fixed length
        rectArrayList.add(new Rectangle(700, 600, 50, 50));
        rectArrayList.add(new Rectangle(750, 550, 50, 50));
        rectArrayList.add(new Rectangle(800, 500, 50, 50));
        return rectArrayList;
    }

}
