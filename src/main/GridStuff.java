package main;


import main.Controller;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.math.BigDecimal;


public class GridStuff extends Drawable {

	private GraphicsContext gc;

	private DragPoint dragPoint;
	private Point a;
	private Point b;
	private Point c;

	private double canvasWidth = 768;
	private double canvasHeight = 768;

	private double n;
	private double m;

	double getWidth;
	double getHeight;
	private BigDecimal offset;

	GridStuff [][] map = new GridStuff[(int) n][(int) m];

	private class Point {

		public double x;
		public double y;

		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}

	}

	public void setZoom(double val) {

		n = m = 10 / val;
		getHeight = canvasHeight / m;
		getWidth = canvasWidth / n;
		Controller.update(gc);

	}

	public void setOffset(BigDecimal offset) {

		this.offset = offset;
		Controller.update(gc);

	}

	public void setExample(String exampleID) {

		if(exampleID.equals("Error 1")) {

			a = new Point(0.5, 0.5);
			b = new Point(12, 12); //upper left hand corner
			c = new Point(24, 24); //24 times point b (so they are collinear)
			dragPoint.setPosition(384, 384);


		} else if(exampleID.equals("Error 2")) {

			a = new Point(0.50000000000002531, 0.5000000000000171);
			b = new Point(17.300000000000001, 17.300000000000001); //upper left hand corner
			c = new Point(24.00000000000005, 24.0000000000000517765); //24 times point b (so they are collinear)
			dragPoint.setPosition(384, 384);

		} else if(exampleID.equals("Error 3")) {

			a = new Point(0.5, 0.5);
			b = new Point(8.8000000000000007, 8.8000000000000007); //upper left hand corner
			c = new Point(12.1, 12.1); //24 times point b (so they are collinear)
			dragPoint.setPosition(384, 384);

		}  else if(exampleID.equals("Error 4")) {

			a = new Point(0.5, 0.5);
			b = new Point(12, 12); //upper left hand corner
			c = new Point(13, 13); //24 times point b (so they are collinear)
			dragPoint.setPosition(384, 384);

		} else {

			a = new Point(0.5, 0.5);
			b = new Point(12, 12); //upper left hand corner
			c = new Point(12.1, 12.1); //24 times point b (so they are collinear)
			dragPoint.setPosition(384, 384);

		}

		Controller.update(gc);

	}

	public void setCoordinates(double bx, double by, double cx, double cy) {

		b.x = bx;
		b.y = by;
		c.x = cx;
		c.y = cy;
		dragPoint.setPosition(384, 384);

		Controller.update(gc);

	}

	public void draw() {

		System.out.println(b.x + " " + b.y);
		System.out.println(c.x + " " + c.y);

		for (int i = 0; i < n; i++) {

			for (int j = (int) Math.ceil(m); j >= 0; j--) {

				BigDecimal bigN = new BigDecimal(i).setScale(18, BigDecimal.ROUND_HALF_EVEN);
				BigDecimal bigM = new BigDecimal(j).setScale(18, BigDecimal.ROUND_HALF_EVEN);

				Point aTemp = new Point(a.x, a.y);

				if(dragPoint.getX() != 384) {
					aTemp = new Point(dragPoint.getX(), canvasWidth - dragPoint.getY()); //P's coordinates
					System.out.println(aTemp.x + " " + aTemp.y);
				}


				aTemp.x = aTemp.x + (offset.multiply(bigN)).doubleValue(); // a.x = a.x + (x * u)
				aTemp.y = aTemp.y + (offset.multiply(bigM)).doubleValue();// a.y = a.y + (y * u)
//				System.out.println("offset: " + offset.toPlainString());
//				System.out.println("x * u: " + offset.multiply(bigN).toPlainString());
//				System.out.println("x * u (double): " + offset.multiply(bigN).doubleValue());
//				System.out.println("a.x: " + a.x);
//				System.out.println("a.y: " + a.y);
				int ccwResult = ccw(aTemp, b, c);
				//System.out.println("ccw: " + ccwResult);
				Color cellColor = getColor(ccwResult);

				gc.setFill(Color.BLACK);
				gc.fillRect(i * getWidth, (m - j) * getHeight, getWidth, getHeight);
				gc.setFill(cellColor);
				gc.fillRect(i * getWidth, (m - j) * getHeight, getWidth - 1, getHeight - 1);

			}
		}
	}

	private Color getColor(int val) {
		if(val > 0) {
			return Color.rgb(230, 151, 133);
		} else if(val < 0) {
			return Color.rgb(230, 215, 133);
		} else {
			return Color.WHITE;
		}
	}

	private int ccw(Point p1, Point p2, Point p3) {

		double result = ((p2.x - p1.x) * (p3.y - p1.y)) - ((p2.y - p1.y) * (p3.x - p1.x));

		if(result > 0) {
			return 1;
		} else if(result < 0) {
			return -1;
		} else {
			return 0;
		}

	}


	public GridStuff(Canvas canvas, DragPoint dragPoint) {
		this.gc = canvas.getGraphicsContext2D();
		this.dragPoint = dragPoint;
		Controller.addDrawable(this);
		drawOrder = 1;

    }

    public void update() {
		draw();
	}
}
