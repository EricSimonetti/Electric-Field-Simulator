package sample;
import javafx.scene.Cursor;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class Charge {
    private double charge, x, y;
    private Circle circle;

    Charge(double x, double y, double charge){
        this.charge = charge;
        this.x = x;
        this.y = y;
        //circle = new Circle(Math.abs(charge), getColor());
        circle = new Circle(5, getColor());
        circle.setCursor(Cursor.HAND);
        circle.setCenterX(x+350);
        circle.setCenterY(y+350);
        circle.setOnMousePressed(Main.circleOnMousePressedEventHandler);
        circle.setOnMouseDragged(Main.circleOnMouseDraggedEventHandler);
        circle.setOnMouseReleased(Main.circleOnMouseReleasedEventHandler);
    }

    double getX() {
        return x;
    }

    double getY() {
        return y;
    }

    double getCharge() {
        return charge;
    }
    Circle getCircle(){
        return circle;
    }

    Paint getColor() {
        if(charge > 0)
            return javafx.scene.paint.Color.RED;
        else if(charge < 0)
            return javafx.scene.paint.Color.BLUE;
        else
            return javafx.scene.paint.Color.GREY;
    }
}
