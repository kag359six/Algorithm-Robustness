package main;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by kristopherguzman on 12/2/16.
 */
public class Line extends Drawable {

    private GraphicsContext gc;
    private DragPoint p;
    private int thickness;
    private int screenWidth;

    public Line(Canvas canvas, int thickness, DragPoint p) {

        this.gc = canvas.getGraphicsContext2D();
        this.thickness = thickness;
        this.screenWidth = canvas.widthProperty().intValue();
        this.p = p;

        Controller.addDrawable(this);
        drawOrder = 2;
        Controller.update(gc);

    }

    public void update() {

        double py = screenWidth - p.getY();
        double m = (py - screenWidth) / (p.getX() - screenWidth); //  y2 - y1 / x2 - x2
        double b = m * (0 - screenWidth) + screenWidth; //  m(x - x1) = y intercept
        double y = b; // y = mx + b
        double x = 0;
        y = screenWidth - y; //invert y coordinate

        gc.beginPath();
        gc.moveTo(screenWidth, 0);
        gc.lineTo(x, y);
        gc.setLineWidth(thickness);
        gc.stroke();
        gc.closePath();

    }

}
