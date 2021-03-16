package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.lang.Math;
import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;


public class Main extends Application {
    private static double orgSceneX = 0, orgSceneY = 0;
    private static double orgTranslateX = 0, orgTranslateY = 0;
    private static ArrayList<Charge> charges = new ArrayList<>();
    private static ArrayList<RodCharge> rodCharges = new ArrayList<>();
    private static final int NUM_ARROWS = 50; //must be even
    private static Arrow[][] arrows = new Arrow[NUM_ARROWS][NUM_ARROWS];
    private static double xrange = 5*NUM_ARROWS, yrange = 5*NUM_ARROWS;
    int N = 1;

    @Override
    public void start(Stage primaryStage) throws Exception{
        calcAns(charges, arrows);

        final int WINDOW_WIDTH = 700, WINDOW_HEIGHT = 700; // width and height are in units of pixels

        primaryStage.setTitle("Electric Field");
        Group root = new Group();
        Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();


        Button button = new Button("Add Point Charge");
        button.setLayoutX(20);
        button.setLayoutY(20);

        Button button2 = new Button("Add Rod Charge");
        button2.setLayoutX(140);
        button2.setLayoutY(20);

        Text helper = new Text("with charge of");
        helper.setLayoutX(255);
        helper.setLayoutY(35);

        TextField chargeValue = new TextField("1");
        chargeValue.setLayoutX(350);
        chargeValue.setLayoutY(20);

        Button button3 = new Button("Reset");
        button3.setLayoutX(650);
        button3.setLayoutY(20);

        Button button4 = new Button("-");
        button4.setLayoutX(140);
        button4.setLayoutY(660);

        Slider slider = new Slider();
        slider.setMin(1);
        slider.setMax(100);
        slider.setValue(1);
        slider.setLayoutX(165);
        slider.setLayoutY(660);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(9);
        slider.setBlockIncrement(1);
        N = (int)slider.getValue();

        Button button5 = new Button("+");
        button5.setLayoutX(305);
        button5.setLayoutY(660);

        Text sliderText = new Text("N charges in each rod:");
        sliderText.setLayoutX(15);
        sliderText.setLayoutY(677);

        Text sliderText2 = new Text("N = 1");
        sliderText2.setLayoutX(340);
        sliderText2.setLayoutY(677);

        Text error = new Text("");
        error.setLayoutX(350);
        error.setLayoutY(70);

        EventHandler<ActionEvent> buttonAction = value ->  {
            try{
                Charge temp = new Charge(0,0,Integer.parseInt(chargeValue.getText())*1e-6);
                charges.add(temp);
                root.getChildren().add(temp.getCircle());
                calcAns(charges, arrows);
                error.setText("");
            }
            catch(NumberFormatException e){
                error.setText("Error: Please Enter an Integer Value");
            }
        };

        EventHandler<ActionEvent> buttonAction2 = value ->  {
            try{
                RodCharge temp = new RodCharge(0,0,Integer.parseInt(chargeValue.getText()));
                temp.changeNum(N);
                rodCharges.add(temp);
                charges.addAll(temp.getCharges());
                root.getChildren().add(temp.getRect());
                calcAns(charges, arrows);
                error.setText("");
            }
            catch(NumberFormatException e){
                error.setText("Error: Please Enter a Number");
            }
        };

        EventHandler<ActionEvent> buttonAction3 = value ->  {
            charges.clear();
            rodCharges.clear();
            root.getChildren().clear();
            root.getChildren().add(canvas);
            root.getChildren().add(button);
            root.getChildren().add(button2);
            root.getChildren().add(button3);
            root.getChildren().add(button4);
            root.getChildren().add(button5);
            root.getChildren().add(chargeValue);
            root.getChildren().add(helper);
            root.getChildren().add(sliderText);
            root.getChildren().add(sliderText2);
            N = 1;
            sliderText2.setText("N = 1");
            slider.setValue(1);
            root.getChildren().add(slider);
            error.setText("");
            root.getChildren().add(error);
            calcAns(charges, arrows);
        };

        EventHandler<ActionEvent> buttonAction4 = value ->  { slider.decrement(); };

        EventHandler<ActionEvent> buttonAction5 = value ->  { slider.increment(); };

        button.setOnAction(buttonAction);
        button2.setOnAction(buttonAction2);
        button3.setOnAction(buttonAction3);
        button4.setOnAction(buttonAction4);
        button5.setOnAction(buttonAction5);

        root.getChildren().add(button);
        root.getChildren().add(button2);
        root.getChildren().add(button3);
        root.getChildren().add(button4);
        root.getChildren().add(button5);
        root.getChildren().add(chargeValue);
        root.getChildren().add(helper);
        root.getChildren().add(error);
        root.getChildren().add(sliderText);
        root.getChildren().add(sliderText2);
        root.getChildren().add(slider);

        //start transforming coordinate system
        Affine t = new Affine();
        double xratio = xrange / WINDOW_WIDTH;
        double yratio = yrange / WINDOW_HEIGHT;
        t.appendTranslation( -xrange/2, -yrange/2 );
        t.appendScale( xratio, yratio );
        try {
            t.invert();
        }
        catch( NonInvertibleTransformException e ) {
            throw new RuntimeException( e.getMessage(), e );
        }
        gc.setTransform( t );                                 //end transforming coordinate system

        //test
        //Arrow[][] test = new Arrow[1][1];
        //double testAng = calcAng2(0,-1);
        //test[0][0] = new Arrow(10, 10, 6.5*.7853981634 );
        //test[0][0] = new Arrow(0, -2, 6.283185306825244);
        //addArrow(gc, test);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.clearRect(-yrange/2, -xrange/2, xrange, yrange);
                if(N!=slider.getValue()) {
                    sliderText2.setText("N = " + (int) slider.getValue());
                    N = (int) slider.getValue();
                    for (RodCharge r : rodCharges)
                        if (r.getNum() != N) {
                            charges.removeAll(r.getCharges());
                            r.changeNum(N);
                            charges.addAll(r.getCharges());
                            r.moveCharges();
                        }
                    calcAns(charges, arrows);
                }

                if(charges.size()!=0)
                    addArrow(gc, arrows);
            }
        }.start();

    }


    public static void main(String[] args) {
        launch(args);
    }

    static EventHandler<MouseEvent> circleOnMousePressedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    ((Circle)t.getSource()).setCursor(Cursor.CLOSED_HAND);
                    orgSceneX = t.getSceneX();
                    orgSceneY = t.getSceneY();
                    orgTranslateX = ((Circle)(t.getSource())).getCenterX();
                    orgTranslateY = ((Circle)(t.getSource())).getCenterY();
                }
            };
    static EventHandler<MouseEvent> circleOnMouseDraggedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    ((Circle)t.getSource()).setCursor(Cursor.CLOSED_HAND);
                    double offsetX = t.getSceneX() - orgSceneX;
                    double offsetY = t.getSceneY() - orgSceneY;
                    double newTranslateX = orgTranslateX + offsetX;
                    double newTranslateY = orgTranslateY + offsetY;
                    ((Circle)(t.getSource())).setCenterX(newTranslateX);
                    ((Circle)(t.getSource())).setCenterY(newTranslateY);
                    calcAns(charges, arrows);
                }
            };
    static EventHandler<MouseEvent> circleOnMouseReleasedEventHandler =  //literally just changes the cursor
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    ((Circle)t.getSource()).setCursor(Cursor.HAND);
                }
            };


    static EventHandler<MouseEvent> rectOnMousePressedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    ((Rectangle)t.getSource()).setCursor(Cursor.CLOSED_HAND);
                    orgSceneX = t.getSceneX();
                    orgSceneY = t.getSceneY();
                    orgTranslateX = ((Rectangle)(t.getSource())).getX();
                    orgTranslateY = ((Rectangle)(t.getSource())).getY();
                }
            };
    static EventHandler<MouseEvent> rectOnMouseDraggedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    ((Rectangle)t.getSource()).setCursor(Cursor.CLOSED_HAND);
                    double offsetX = t.getSceneX() - orgSceneX;
                    double offsetY = t.getSceneY() - orgSceneY;
                    double newTranslateX = orgTranslateX + offsetX;
                    double newTranslateY = orgTranslateY + offsetY;
                    ((Rectangle)(t.getSource())).setX(newTranslateX);
                    ((Rectangle)(t.getSource())).setY(newTranslateY);
                    for(RodCharge r:rodCharges)
                        r.moveCharges();
                    calcAns(charges, arrows);
                }
            };
    static EventHandler<MouseEvent> rectOnMouseReleasedEventHandler =  //literally just changes the cursor
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    ((Rectangle)t.getSource()).setCursor(Cursor.HAND);
                }
            };


    private static void calcAns(ArrayList<Charge> Q, Arrow[][] arrows) {
        if(Q.size()==0) {
            int xCount = 0, yCount = 0;
            for (int i = -NUM_ARROWS/2; i < NUM_ARROWS/2; i++){
                yCount = 0;
                for (int j = -NUM_ARROWS/2; j < NUM_ARROWS/2; j++) {
                    arrows[xCount][yCount] = new Arrow(5 * i, 5 * j, 0, 0);
                    arrows[xCount][yCount].setColor(javafx.scene.paint.Color.WHITE);
                    yCount++;
                }
                xCount++;
            }
        }
        else {
            realCalcAns(Q, arrows);
        }
    }
    private static void realCalcAns(ArrayList<Charge> Q, Arrow[][] arrows){
        double[][] Ex = new double[NUM_ARROWS][NUM_ARROWS], Ey = new double[NUM_ARROWS][NUM_ARROWS];
        int xCount = 0, yCount = 0;
        for(int i = -NUM_ARROWS/2; i<NUM_ARROWS/2; i++) {
            yCount = 0;
            for (int j = -NUM_ARROWS/2; j < NUM_ARROWS/2; j++) {               //loops through coordinates
                for (Charge q : Q) {    //calcs with each Q
                    double tempx = (5 * i - (.007135*NUM_ARROWS)*(q.getCircle().getCenterX()-350)), tempy = (5 * j - (.007135*NUM_ARROWS)*(q.getCircle().getCenterY()-350));

                    double tempAng = Math.atan2(tempx, tempy);
                    //double helperdeg = tempAng*(180/Math.PI);
                    if (q.getCharge() < 0)
                        tempAng += Math.PI;

                    double tempE = calcLinear(Math.sqrt(Math.pow(tempx, 2) + Math.pow(tempy, 2)), q.getCharge());
                    Ex[xCount][yCount] += tempE * Math.cos(tempAng);
                    Ey[xCount][yCount] += tempE * Math.sin(tempAng);
                }

                double ang = Math.atan2(Ex[xCount][yCount],Ey[xCount][yCount]);
                arrows[xCount][yCount].setAng(ang);
                if(Ex[xCount][yCount]==0&&Ey[xCount][yCount]==0){
                    arrows[xCount][yCount].setColor(javafx.scene.paint.Color.WHITE); //either this doesn't work for the if doesn't catch bc its never exactly 0
                    arrows[xCount][yCount].setLength(0);
                }
                else {
                    double E = Math.pow(Math.pow(Ex[xCount][yCount],2)+Math.pow(Ey[xCount][yCount],2),.5);

                    double max_length = 4, max_E = 50;
                    if(E>max_E) E = max_E;
                    arrows[xCount][yCount].setColor(javafx.scene.paint.Color.BLACK);
                    //arrows[xCount][yCount].setLength(4);
                    arrows[xCount][yCount].setLength((E/max_E)*max_length);
                    //System.out.println((E/max_E)*max_length);
                }
                yCount++;
            }
            xCount++;
        }

    }

    private static double calcLinear(double r, double Q){
    final double k = 8.99 * Math.pow(10, 9);
        return (k * Math.abs(Q)) / Math.pow(r, 2);
    }

    private static double calcAng(double x, double y){
        double tempAng = Math.atan(y/x);
        //fix on axis
        if (x>0){
            if(y>0)
                return tempAng;
            return tempAng+2*Math.PI;
        }
        if(y>0)
            return tempAng+Math.PI;
        return tempAng+Math.PI;

    }

    private void addArrow(GraphicsContext gc, Arrow[][] arrows){
        for (int i = 0; i < NUM_ARROWS; i++)
            for (int j = 0; j < NUM_ARROWS; j++){
                for(int k = 0; k<3; k++)
                    arrows[i][j].draw(gc, .1);
            }

    }
}
