package sample;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;


class Arrow {
    private Line[] lines = new Line[3];
    private double startX, startY;
    private double angle;
    private double length;
    private Color color;

    Arrow(int x, int y, double ang, double length){
        startX = x;
        startY = y;
        angle = ang;
        color = javafx.scene.paint.Color.BLACK;
        this.length = length;
        doCalc();
    }

    Arrow(int x, int y, Color color){
        startX = x;
        startY = y;
        this.color = color;
        length = 4;
        doCalc();
    }

    void setAng(double ang){
        angle = ang;
        doCalc();
    }

    void setLength(double length){
        this.length = length;
        doCalc();
    }

    void setColor(Color color){
        this.color = color;
    }

    private void doCalc(){
        double lsin = length*Math.sin(angle), lcos = length*Math.cos(angle);
        double endX = startX+lcos, endY = startY+lsin;

        lines[0] = new Line(startX, startY, endX, endY);
        lines[1] = new Line(endX, endY, endX + .2*(lsin-lcos), endY - .2*(lsin+lcos)); // math magic
        lines[2] = new Line(endX, endY, endX - .2*(lsin+lcos), endY + .2*(lcos-lsin));
    }

    void draw(GraphicsContext gc, double width){
        for(int k = 0; k<3; k++) {
            gc.setLineWidth(width);
            gc.setStroke(color);
            gc.strokeLine(lines[k].getStartX(),
                          lines[k].getStartY(),
                          lines[k].getEndX(),
                          lines[k].getEndY());
        }
    }

    Color getColor() {return color;}

    Line[] getLines() {
        return lines;
    }
}
