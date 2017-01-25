package main;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Created by kristopherguzman on 12/2/16.
 */
public class DragPoint extends Drawable {

    public boolean isDragging = false;

    private GraphicsContext gc;

    private int screenWidth;
    private double x;
    private double y;
    private int size = 20;

    public double getX() { return x + (size / 2); }
    public double getY() { return y + (size / 2); }


    public DragPoint(Canvas canvas, double x, double y) {

        this.gc = canvas.getGraphicsContext2D();
        screenWidth = canvas.widthProperty().intValue();
        this.x = x;
        this.y = y;

        Controller.addDrawable(this);
        drawOrder = 3;

    }

    public void setPosition(double x, double y) {

        if(x > screenWidth - 25 || x < 25 || y > screenWidth - 25 || y < 25) {
            return;
        }

        this.x = x - (size / 2);
        this.y = y - (size / 2);

    }

    public boolean inBoundsOf(double mx, double my) {

        //checks if point of mouse click is in bounds of drag point
        return (mx > x && mx < x + size && my > y && my < y + size);

    }

    public void update() {

        gc.setFill(Color.BLACK);
        gc.fillOval(x, y, size, size);

    }


}
