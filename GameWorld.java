package mazejumergame2;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * Hold down an arrow key to have your hero move around the screen. Hold down
 * the shift key to have the hero run.
 */
public class GameWorld {

    static final double W = 700, H = 667;//sets the area limit for which the hero can move across the screen
    //this DOES NOT affect the size of the stage and background, but only 
    //which part of the stage the hero can access

    static final String HERO_IMAGE_LOC
            = "/Users/andrewdedich/Desktop/pixil-frame-0.png";

    Image heroImage;
    Node hero;
    Stage mainStage;

    boolean running, jump, right, left;

    /*double yInitVelocity = Math.sqrt(3600); // 40
    int gravityAcceleration = -9; //a deceleration of 5
    int xVelocity = 0; //
    int displacement = 0;*/
    double positionX = 100.0;
    double positionY = 175.0;
    double velocityX = 4.0;
    double velocityY = 0.0;
    double gravity = 0.5;
    boolean onGround = false;

    BackgroundController bc = new BackgroundController();

    Stage1 s1 = new Stage1();
    ImageView background1 = bc.getBackground1();
    ImageView background2 = bc.getBackground2();
    Group obstacleLayout = bc.getObstacles();
    ArrayList<Rectangle> obstacles = s1.rectangularObstaclesLayout();

    boolean canMoveRight; // a boolean variable created for the purpose and use in the moveHeroTo() method
    //whereby in this function the bounds of the hero are checked so the hero can only be moved
    //to points within the specified bounds of the scene

    //if the hero is within the far right bounds of the scene this variable is set to true, meaning 
    //the if statement moving the hero right can be used
    //but if not, this if statement cannot be used so the hero halts (although the boolean variable controlling
    //the right key can still be set a boolean value if the right key is pressed or not)
    //this variable was created so that the 'right' variable still can be used and still holds jurisdiction of 
    //whether the background animation moves or not depending on if it is pressed, even if the hero cannot move right across
    //the scene anymore
    boolean canJump, canMoveLeft, movingDown, movingUp;

    GameWorld() {
        buildAndSetGameLoop();
    }

    /**
     * Builds and sets the game loop ready to be started.
     */
    protected final void buildAndSetGameLoop() {

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // update actors
                //updateSprites();

                // check for collision
                checkCollisions();

                // removed dead things
                //cleanupSprites();
                heroControls();

                gravity();

                if (hero.getLayoutX() >= 580 && right == true) {//if the hero is after pixel 1000 of the background and the right key is being held or pressed, then and only then will the scrolling of the background start
                    bc.startAnimation();

                } else { //else, in any other case, the scrolling of the background will stop
                    bc.pauseAnimation();
                }

                /*if(right){
                bc.stopAnimation();
                bc.setBackgroundScrollingSpeed(9000);
                bc.startAnimation();
                }
                else{
                bc.stopAnimation();
                bc.setBackgroundScrollingSpeed((bc.getBackgroundScrollingSpeed()) * 0.5);
                bc.startAnimation();
                }*/
            }
        }; // oneFrame
        timer.start();
    }

    public void startMovement(Stage mainStage) throws Exception {
        FileInputStream heroInputstream = new FileInputStream(HERO_IMAGE_LOC);
        heroImage = new Image(heroInputstream);
        hero = new ImageView(heroImage);

        Group dungeon = new Group();

        moveHeroTo(hero.getBoundsInLocal().getWidth(), 800 - hero.getBoundsInLocal().getHeight() / 2);

        Scene scene = new Scene(dungeon, 4000, 800);
        //Stage1 s1 = new Stage1();
        dungeon.getChildren().addAll(background1, background2, hero, obstacleLayout);

        //anonymous inner class to be set for when the key is released
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) { //the handle method from the EventHandler class (with Keyevent parameter) 
                //KeyEvent could be any key pressed 
                switch (event.getCode()) { //get code for event that happened
                    case UP:
                        /*if (displacement == 0) { //you can only press the jump button at the point where
                        //displacement = 0 (at beginning of jump)
                        yInitVelocity = Math.sqrt(3600); //resets the initial velocity to its default value of 60 every
                        //time the hero is at ground level (when displacement = 0)
                        //so that you can perform another jump
                        jump = true;
                        //certain code generetad by event getcode() depending on what key is pressed
                        }*/
                        jump = true;
                        //movingUp = true;
                        startJump();
                        break;
                    case LEFT:
                        left = true;
                        break;
                    case RIGHT:
                        right = true;
                        break;
                    case SHIFT:
                        running = true;//in the case that the shift key is pressed, the running variable is set to true
                        bc.setIfRunning(running);//the value of this running variable is passed into a function of the backgroundController class
                        break;
                }
            }
        });

        //anonymous inner class to be set for when the key is released
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:
                        movingDown = true;
                        endJump();
                        break;
                    case LEFT:
                        left = false;
                        // xVelocity = 0;
                        break;
                    case RIGHT:
                        right = false;
                        //xVelocity = 0;
                        break;
                    case SHIFT:
                        running = false;//in the case that the shift key is released, the running variable is set to false
                        bc.setIfRunning(running);//the value of this running variable is passed into a function of the backgroundController class
                        break;
                }
            }
        });

        mainStage.setScene(scene);
        mainStage.show();

    }

    private void heroControls() {
        double dx = 0, dy = 0; //delta x
        //movement in x and movement in y
        /*if (displacement != 0) {
        if (yInitVelocity <= 0) {
        dy = gravity(dx, dy);
        }
        }*/
        //if (jump) { //if jump is true it is because we've pressed the UP key
        dy += update();
        //  }
        /*if (xVelocity >= 0) {
            goWest = false;
            }
            if (xVelocity <= 0) {
            goEast = false;
            }*/
        //  }

        if (canMoveRight) {
            if (right) {

                dx += 4;
                //xVelocity += 2;
            }
        }
        //if (canMoveLeft) {
        if (left) {
            dx -= 3;
            //xVelocity -= 2;
        }
        // }
        if (running) {

            dx *= 2.5;
            // dy *= 4;
        }

        // displacement -= dy;
        moveHeroBy(dx, dy);
    }

    private void gravity() {
        double dx = 0;
        double dy = 0;
        if (jump == false) {
            dy += 4.0;
        }
        moveHeroBy(dx, dy);
    }

    /*    //the yvelocity at certain intervals of the jump will become less and less, as
    //at the beginnning of the jump you will have a greater velocity than at the peak of the jump
    private int jump(int dy) {
    dy = 0;
    if (yInitVelocity == 0) {
    jump = false;
    } else {
    dy -= yInitVelocity;
    yInitVelocity += gravityAcceleration;
    }
    return dy;
    }*/
    private double update() {
        velocityY += gravity;
        positionY += velocityY;

        if (positionY > 175.0) {
            positionY = 175.0;
            velocityY = 0.0;
            onGround = true;
            jump = false;
        }

        return velocityY;
    }

    private void startJump() {
        //if (movingUp) {
        if (onGround) {
            velocityY = -12.0;
            onGround = false;
        }
        // }
    }

    private void endJump() {
        //if (movingDown) {
        if (velocityY < -6.0) {
            velocityY = -6.0;
        }
        // }
    }

    /*
    TOMORROW 
    Now that i have made a method in the Stage1() class that creates all the rectangular obstacles using an arrayList 
    (and then passing all those created rectangles onto a pane that is used in order to display these rectangles onto
    the animation loop and have them used in the translatetransition), now i need to use the rectangularObstaclesLayout()
    method within the stage class to use the arrayList with all the rectangles and find a way to make it so the hero cannot
    intersect each and every rectngle in the method below
     */
    private void checkCollisions() {
        for (int i = 1; i < obstacles.size(); i++) {
            Rectangle rect = obstacles.get(i);
            /*ObservableBooleanValue colliding = Bindings.createBooleanBinding(new Callable<Boolean>() {
            
            @Override
            public Boolean call() throws Exception {
            return hero.getBoundsInParent().intersects(rect.getBoundsInParent());
            }
            
            }, hero.boundsInParentProperty(), rect.boundsInParentProperty());
            
            colliding.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs,
            Boolean oldValue, Boolean newValue) {
            if (newValue) {
            System.out.println("Colliding");
            } else {
            System.out.println("Not colliding");
            }
            }
            });*/

 /*
            because the rectPane defined in the Stage1 class was intially created with a canvas node of a certain width and height
            (check Stage1 class), this was the first child node of the Pane rectPane, by which all the other child nodes were derived from
            (the other child nodes being all the rectangular blocks). So, when I looped throught the children of rectPane in the for loop defined 
            above, I noticed that at some points a collision was being detected even when the hero was not touching the blocks on the scene.
            This was because the for loop was looping through i = 0, and get(i).getBoundsInParent() when i = 0 was returnung the first child node
            , which was the canvas that spanned the whole area, and of course the hero was intersecting this.
            In order to solve this, all i needed to do was start the for loop at i=1, which was were the first rectangle node began...
             */
 
            if (hero.getBoundsInParent().intersects(rect.getBoundsInParent()) || rect.getBoundsInParent().intersects(hero.getBoundsInParent())) {
                System.out.println(rect.getBoundsInParent());
                hero.relocate(10, 10);

            } else {
            }
            
            /*if (hero.getBoundsInLocal().intersects(rect.getBoundsInLocal())) {
            if (movingDown) {
            if (hero.getLayoutY() + 40 == rect.getLayoutY()) {
            hero.setLayoutY(hero.getLayoutY() - 1);
            onGround = true;
            }
            }
            }*/
        }
        //return collisionDetected;

    }

    private void moveHeroBy(double dx, double dy) {
        // this function uses moveHeroTo, so it needs to obtain the centre point of the character
        if (dx == 0 && dy == 0) {
            return; //if horizontal and vertical distances for which the hero is to be moved 
        }                                        //= 0, then return nothing (i.e do nothing)

        //get the width and height of the sprite, and divide it by 2 to get the distance 
        //from the centre of the sprite to its edges 
        final double cx = hero.getBoundsInLocal().getWidth() / 2;
        final double cy = hero.getBoundsInLocal().getHeight() / 2;

        //x and y are the new coordinates. hero.getLayout() is a javaFX function so returns
        //the coordinate of the top left of the sprite, so we need to add cx in order to get
        //the coordinate of the sprite centre. We also add dx and dy to perform the move. 
        //Then we call moveHeroTo(x,y), which takes a desired centre point as its parametersm, 
        //and actually moves the sprite.
        double x = cx + hero.getLayoutX() + dx;
        double y = cy + hero.getLayoutY() + dy;

        moveHeroTo(x, y);
    }

    // the function below adds a delta to coordinate (x,y), so that the centre of the character will be at that
    //coordinate. This is needed because JavaFX drawssprites such that their top left corner is at the 
    //coordinate requested.
    private void moveHeroTo(double x, double y) {
        //the hero is represented as a rectangle with pixel dimensions
        //in java, an image/rectangle/screen is always loaded from the top left onwards

        //get the width and height of the sprite, and divide it by 2 to get the distance from the centre of the sprite to its edges
        final double cx = hero.getBoundsInLocal().getWidth() / 2;//get local bounds of hero image and half it
        final double cy = hero.getBoundsInLocal().getHeight() / 2;

        // need to check that all parts of the sprite rectangle are contained within the scene bounds. 
        //this checks the left, right top and bottom side by adding / subtracting cx and cy from the centre point
        if (x - cx >= 0
                && x + cx <= W
                && y - cy >= 0
                && y + cy <= H) {
            // ok, we're entirely within the scene, so let's set our characters top left such that it's centre is at (x,y)
            hero.relocate(x - cx, y - cy);

        }
        if (x + cx <= W - 10) { //if the hero is within the bounds of the far right of the scene then canMoveRight is true
            //(so the hero can press the right key and move right whilst this condintion is met
            //I minused 10 from W because W is the point right at the edge of the scene, and when
            //i was pressing the jump button at this point (while holding the right key down) it
            //was very laggy and slow framerate compared to the other jumps
            //So, i made it so that the check for the right bounds of the hero is shortened by a bit
            //and not checked for the hero to be in bounds at the W coordinate (the edge of the screen)
            //but checked a bit before this
            //This is so if the hero is after this specified point of W-10, then canMoveright is set to false
            //and the hero no longer moves right, but can still jump (but not right at the edge of the scene
            //,which created lag) with no lag

            //The reason it is minus of a small number like 3 is because the number still needs
            //to be small enough such that at the point that you can no longer mover right, the hero is
            //still within the coordinates such that the background loop animation is started
            canMoveRight = true;
        } else {
            canMoveRight = false;
        }

        /* //now do the same for left
        if (x - cx >= 0) {
        canMoveLeft = true;
        } else {
        canMoveLeft = false;
        }*/
    }
}
