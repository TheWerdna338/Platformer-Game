package mazejumergame2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Screens {

    static Scene scene1, scene2;

    Screens() {
    }

    //method cannot be static as we want to call it from another class
    public void getStartScreen(Stage primaryStage) throws FileNotFoundException {
        //Scene 1 - Startup Screen
        Button button1 = new Button("START"); //creating the start button
        Button button2 = new Button("EXIT");  //creating the exit button

        Pane layout1 = new Pane();  //create a pane (a platform) from which i can perform various transformations

        button1.setLayoutX(242);
        button1.setLayoutY(165);        // translating the buttons across the pane
        button2.setLayoutX(242);
        button2.setLayoutY(315);

        button1.setMinSize(200, 100);
        Font button1Font = Font.font("Times New Roman", FontWeight.BOLD, 30);
        button1.setFont(button1Font);                                 //increasing the size of the buttons
        button2.setMinSize(200, 100);                                 //and setting the fonts and sizes
        Font button2Font = Font.font("Times New Roman", FontWeight.BOLD, 30);
        button2.setFont(button2Font);

        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) { //i have used an anyonymous inner class to handle this action
                primaryStage.setX(0);           //which means i can perform actions before setting the scene
                primaryStage.setY(0);           //such as positioning the coordinates for which the scene appears

                primaryStage.setScene(scene2); //upon an action on this start button, transition over to the next scene

                PauseTransition delay = new PauseTransition(Duration.seconds(3));//
                delay.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent f) {
                        primaryStage.hide();
                        GameWorld mainScreen = new GameWorld();
                        try {
                            mainScreen.startMovement(primaryStage);
                        } catch (Exception ex) {
                            Logger.getLogger(Screens.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });//
                delay.play();

                /*
                prior to using the pausetransition class to cause a delay before hiding the stage, i was using the TimeUnit class
                which actually halted the entire execution in place for a certain time and then hid the stage, so what was happening was
                that as soon as i clicked start, the system would halt for 5 seconds and then exit, so i never actually
                got to the second scene.
                With the pauseTransition class, essentially what happens is a timer is made (or a delay in time) and this timer starts
                as soon as i click the start button. So the system exuction is not halted, and continues to carry on until the time
                is up, then the stage is hidden.
                 */
            }
        });
        button2.setOnAction(e -> System.exit(0)); //upon an action on this exit button, exit out the scene
        //i could have used an anyonymous inner class here but there is no need 
        //as i there is not another screen after the exit screen and i neeed to place no coordinates

        Canvas canvas1 = new Canvas(710, 515); //creating a canvas 
        GraphicsContext gc1 = canvas1.getGraphicsContext2D(); //for which to include graphics
        Image backgroundImage = new Image("https://d2gg9evh47fn9z.cloudfront.net/800px_COLOURBOX2344112.jpg");
        gc1.drawImage(backgroundImage, 0, 0);
        gc1.setFill(Color.RED);//sets colour of the written text
        gc1.setStroke(Color.GREEN); //sets the colour of the border around the writing (like bubble writing)
        gc1.setLineWidth(2); //sets the width of this border (how thick it is)
        Font theFont = Font.font("Times New Roman", FontWeight.BOLD, 90);
        gc1.setFont(theFont); //font and font size and font style is set
        gc1.fillText("Maze Jumper", 95, 100); //writes the text and positions it
        gc1.strokeText("Maze Jumper", 95, 100); //writes the border around the text and positions it

        layout1.getChildren().addAll(canvas1, button1, button2);

        /*
        Originally i did this -> ' layout1.getChildren().addAll(button1, button2, canvas); '
        I had problems with the click detection of the buttons, as after i had translated them to their 
        corresponding positions, when i clicked them, nothing happened.
        After some research, i found out this was because The order of nodes in a StackPane (and other panes, in some circumstances) 
        is determined by the order of the nodes in the children list. So, in my code, the buttons were 
        "behind" the canvas. This means mouse clicks are targeted to the canvas, so nothing happened.
         */
        scene1 = new Scene(layout1); //creating the first scene with the specified layout1 and all its modifications

        //Scene 2 - Prelimnary Screen
        Pane layout2 = new Pane();
        Canvas canvas2 = new Canvas(1280, 800);
        GraphicsContext gc2 = canvas2.getGraphicsContext2D();
        gc2.setFill(Color.BLACK);
        gc2.fillRect(0, 0, 1280, 800);
        gc2.setFill(Color.WHITE);
        Font text1Font = Font.font("Times New Roman", FontWeight.BOLD, 40);
        gc2.setFont(text1Font);
        gc2.fillText("Stage 1", 95, 90);
        gc2.fillText("LIVES - 3", 584, 360);

        FileInputStream inputstream = new FileInputStream("/Users/andrewdedich/Desktop/pixil-frame-0.png");
        Image sprite = new Image(inputstream);
        gc2.drawImage(sprite, 494, 295);

        layout2.getChildren().addAll(canvas2);
        scene2 = new Scene(layout2);

        primaryStage.setScene(scene1);
        primaryStage.show();
        
        //created a fade in animation for the second screen (layout2)
        FadeTransition ft = new FadeTransition(Duration.millis(4000), layout2);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    public static void getEndScreen() {
        //code for endScreen whene character dies
    }
}
