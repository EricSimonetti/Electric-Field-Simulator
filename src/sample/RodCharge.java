package sample;

import javafx.scene.Cursor;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

class RodCharge {
    private double charge, length, width;
    private int num;
    private ArrayList<Charge> charges = new ArrayList<>();
    private Rectangle rect;

    RodCharge(double x, double y, double charge){
        this.charge = charge*1e-6;
        length = 100;
        width = 20;
        num = 1;
        rect = new Rectangle(x-length/2+350, y-width/2+350, length, width);
        rect.setCursor(Cursor.HAND);
        rect.setFill(getColor());
        rect.setOnMousePressed(Main.rectOnMousePressedEventHandler);
        rect.setOnMouseDragged(Main.rectOnMouseDraggedEventHandler);
        rect.setOnMouseReleased(Main.rectOnMouseReleasedEventHandler);
        calcCharges();
    }

    void moveCharges(){
        int q = 0;
        double offset = length/(num+1);
        for(double i = rect.getX()+offset; i<rect.getX()+length-.00000009; i+=offset) {
            charges.get(q).getCircle().setCenterX(i);
            charges.get(q).getCircle().setCenterY(rect.getY()+width/2);
            q++;
        }
    }

    void changeNum(int inc){
        this.num = inc;
        calcCharges();
    }

    ArrayList<Charge> getCharges(){ return charges; }

    Rectangle getRect() { return rect; }

    int getNum(){ return num;}

    Paint getColor() {
        if(charge > 0)
            return javafx.scene.paint.Color.RED;
        else if(charge < 0)
            return javafx.scene.paint.Color.BLUE;
        else
            return javafx.scene.paint.Color.GREY;
    }

    private void calcCharges(){
        charges.clear();
        double offset = length/(num+1);
        for(double i = rect.getX()+offset-350; i < rect.getX()+length-350-.00000009; i+=offset)
            charges.add(new Charge(i, rect.getY()+width/2-350, charge/num));
    }





}
