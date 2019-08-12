package mazejumergame2;

import java.util.ArrayList;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 *
 * @author andrewdedich
 */
public class BackgroundController {

    private final int BACKGROUND_WIDTH = 2000;
       
    Stage1 s2 = new Stage1();
    
    ImageView background1;

    ImageView background2;
    
    Group obstacles;
   
    private ParallelTransition parallelTransition;
    private TranslateTransition obstacleTranslateTransition;
    
    boolean running; //a bool variable that checks whether the hero is running. it is linked to the same variable in the GameWorld class
    
    BackgroundController(){
        this.background1 = s2.getBackdrop1();
        this.background2 = s2.getBackdrop2();
        this.obstacles = s2.obstacleLayout();
        initialize();
    }

    public ImageView getBackground1() {
        return background1;
    }

    public ImageView getBackground2() {
        return background2;
    }

    public Group getObstacles() {
        return obstacles;
    }

    public TranslateTransition getObstacleTranslateTransition() {
        return obstacleTranslateTransition;
    }
    
    

    
    

    public ParallelTransition getParallelTransition() {
        return parallelTransition;
    }
    
    




    public void setIfRunning(boolean ifRunning){ //this function is used to retrieve the value of the running variable in the
        this.running = ifRunning;                //GameWorld class. In that class, if the shift key is pressed or released,
    }                                            //then the bool value of the running variable changes accordingly in that class
                                                 // and so therefore will change in this class
    
    private void initialize() {

        TranslateTransition translateTransition
                = new TranslateTransition(Duration.millis(9000), background1); //the duration it takes to cover the distance specified below is set and the image that is being animated is set as parameters of a translatetransition object
        translateTransition.setFromX(0);         //sets the distance the animation has to cover before it loops over
        translateTransition.setToX(-1 * BACKGROUND_WIDTH); // ditto as above
        translateTransition.setInterpolator(Interpolator.LINEAR);

        TranslateTransition translateTransition2
                = new TranslateTransition(Duration.millis(9000), background2);
        translateTransition2.setFromX(2000);
        translateTransition2.setToX(0);
        translateTransition2.setInterpolator(Interpolator.LINEAR);
        
        parallelTransition
                = new ParallelTransition(translateTransition, translateTransition2);
        parallelTransition.setCycleCount(Animation.INDEFINITE);
        
        /*
        seperate translateTransition for the obstacles in the level because it will be travilling different
        distance and different duration to the other two translatetransitions above, and thus wouldnt work
        in the parallelTransition
        */
        
        obstacleTranslateTransition
                = new TranslateTransition(Duration.millis(36000), obstacles);
        obstacleTranslateTransition.setFromX(0);
        obstacleTranslateTransition.setToX(-8000);//the miliseconds and the distacne travelled all need to line up for each translation transition in order for them to all move at the same rate
                                            //if the distance is changed to a greater distance travelled within the same amound of time, the transition will move at a faster pace,
                                            //because speed = distance/time
        obstacleTranslateTransition.setInterpolator(Interpolator.LINEAR);
        obstacleTranslateTransition.setCycleCount(Animation.INDEFINITE); //THIS transition DOESNT necessarily have to loop
        
        

    }

    public void startAnimation() {
        parallelTransition.play();
        obstacleTranslateTransition.play();
        if (running == true){ //if the bool value of the running variable is true (meaning that the shift key is being pressed while in the animationTimer), 
                              //then the rate at which the transition runs through the distance (and duration that had been set to take however long to cover this distance set) it has to travel
                              //will be doubled, so the loop will appear to be twice as fast
            parallelTransition.setRate(2);
            obstacleTranslateTransition.setRate(2);
        }
        else{                             //else if the bool value is false (meaining the runnning key is not being pressed), then set the rate to the normal                       
            parallelTransition.setRate(1);//rate that was initially set in the initialize method 
                                          //i.e. dont do anything to the speed at which the transition runs through the distance set.
            obstacleTranslateTransition.setRate(1);
        }
        
    }

    public void pauseAnimation() {
        parallelTransition.pause();
        obstacleTranslateTransition.pause();
        
    }
    

    /* public void controlPressed() {
    if (parallelTransition.getStatus() == Animation.Status.RUNNING) {
    pauseAnimation();
    } else {
    startAnimation();
    }
    }*/
    
    //the above function was deemed unnecessary as before, i was calling bc.controlPressed() in the main animation timer
    //but this was introducing errors for me when trying to include the conditional if statements (to make it so that 
    //only when the hero is past a certain coordinate would it scroll) such as the background scrolling sporratically
    //and not when the right key was being pressed/held
    //So i changed it so that the method startAnimation() and stopAnimation() would be called instead. 
    //this got rid of the errors that were occuring
}
