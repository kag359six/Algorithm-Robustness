package main;

/**
 * Created by kristopherguzman on 11/19/16.
 */

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Controller {

    @FXML private AnchorPane controlPanel;
    @FXML private AnchorPane canvasPanel;
    @FXML private SplitPane rootPane;
    @FXML private Canvas canvas;
    @FXML private Slider offsetSlider;
    @FXML private Slider zoomSlider;
    @FXML private Label offsetLabel;
    @FXML private Label zoomLabel;

    @FXML private RadioButton example1;
    @FXML private RadioButton example2;
    @FXML private RadioButton example3;
    @FXML private RadioButton example4;
    @FXML private RadioButton example5;

    @FXML private TextField bxCoordinate;
    @FXML private TextField byCoordinate;
    @FXML private TextField cxCoordinate;
    @FXML private TextField cyCoordinate;

    private DragPoint point;
    private Line line;
    private GridStuff grid;


    private static ArrayList<Drawable> drawableObjects = new ArrayList<>();

    private double MIN_OFFSET = 0.000000000000000001; //10^-18
    private double MAX_OFFSET = 0.000000000000009; //9x10^-16
    private BigDecimal minAsBigDecimal = new BigDecimal("0.000000000000000001").setScale(18, BigDecimal.ROUND_HALF_EVEN);

    private int maxResolution = 100;

    private DecimalFormat df = new DecimalFormat(".##");
    private DecimalFormat offsetFormat = new DecimalFormat("0.0E0");


    @FXML private void initialize() {

        point = new DragPoint(canvas, 300, 300);
        line = new Line(canvas, 2, point);
        grid = new GridStuff(canvas, point);

        setRadioButtons();
        setSliderHandlers();
        setMouseEvents();
    }

    private void setRadioButtons() {

        ToggleGroup group = new ToggleGroup();
        group.getToggles().add(example1);
        group.getToggles().add(example2);
        group.getToggles().add(example3);
        group.getToggles().add(example4);
        group.getToggles().add(example5);

        group.selectToggle(example1);

        grid.setExample("Error 1");
        bxCoordinate.setText("12");
        byCoordinate.setText("12");
        cxCoordinate.setText("24");
        cyCoordinate.setText("24");

        group.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> ov,
                Toggle old_toggle, Toggle new_toggle) -> {

            RadioButton btn = (RadioButton) new_toggle;
            grid.setExample(btn.getText());
            setCoordinateLabels(btn.getText());

        });

    }

    private void setCoordinateLabels(String example) {

        if(example.equals("Error 1")) {

            bxCoordinate.setText("12");
            byCoordinate.setText("12");
            cxCoordinate.setText("24");
            cyCoordinate.setText("24");

        } else if(example.equals("Error 2")) {

            bxCoordinate.setText("17.300000000000001");
            byCoordinate.setText("17.300000000000001");
            cxCoordinate.setText("24.00000000000005");
            cyCoordinate.setText("24.0000000000000517765");

        } else if(example.equals("Error 3")) {

            bxCoordinate.setText("8.8000000000000007");
            byCoordinate.setText("8.8000000000000007");
            cxCoordinate.setText("12.1");
            cyCoordinate.setText("12.1");

        } else if(example.equals("Error 4")) {

            bxCoordinate.setText("12");
            byCoordinate.setText("12");
            cxCoordinate.setText("13");
            cyCoordinate.setText("13");

        }  else if(example.equals("Error 5")) {

            bxCoordinate.setText("12");
            byCoordinate.setText("12");
            cxCoordinate.setText("12.1");
            cyCoordinate.setText("12.1");

        }

    }

    private void setMouseEvents() {

        Controller.update(canvas.getGraphicsContext2D());

        canvas.setOnMousePressed((event) -> {

            if(point.inBoundsOf(event.getX(), event.getY())) {
                point.isDragging = true;
            }

        });

        canvas.setOnMouseDragged((event) -> {

            if(point.isDragging) {
                point.setPosition(event.getX(), event.getY());
                Controller.update(canvas.getGraphicsContext2D());
            }

        });

        canvas.setOnMouseReleased((event) -> {

            point.isDragging = false;

        });

    }

    private void setSliderHandlers() {

        offsetLabel.setText("Offset: " + offsetFormat.format(MIN_OFFSET));
        offsetSlider.valueProperty().setValue(MIN_OFFSET);
        grid.setOffset(minAsBigDecimal);

        offsetSlider.valueProperty().addListener(((observable, oldValue, newValue) -> {

            BigDecimal result = minAsBigDecimal.multiply(new BigDecimal(newValue.toString()));
            offsetLabel.setText("Offset: " + offsetFormat.format(result));
            grid.setOffset(result);

        }));

        zoomLabel.setText("0.1x");
        zoomSlider.setValue(100);
        grid.setZoom(1);

        zoomSlider.valueProperty().addListener(((observable, oldValue, newValue) -> {

            double val = newValue.doubleValue() / maxResolution;
            grid.setZoom(val);
            zoomLabel.setText("Zoom: " + df.format(val) + "x");

        }));

    }

    public static void addDrawable(Drawable d) {
        drawableObjects.add(d);
        drawableObjects.sort((Drawable a, Drawable b) -> a.drawOrder < b.drawOrder ? -1 : 1);

    }

    public static void update(GraphicsContext gc) {

        gc.clearRect(0, 0, gc.getCanvas().widthProperty().intValue(), gc.getCanvas().widthProperty().intValue());

        for(Drawable item : drawableObjects) {

            item.update();

        }

    }

    @FXML private void onReset(MouseEvent event) { //makes line collinear again

        point.setPosition(canvas.widthProperty().intValue() / 2, canvas.heightProperty().intValue() / 2);
        Controller.update(canvas.getGraphicsContext2D());

    }

    @FXML private void onSetCoordinates(MouseEvent event) { //makes line collinear again

        double bx = Double.parseDouble(bxCoordinate.getText());
        double by = Double.parseDouble(byCoordinate.getText());
        double cx = Double.parseDouble(cxCoordinate.getText());
        double cy = Double.parseDouble(cyCoordinate.getText());
        grid.setCoordinates(bx, by, cx, cy);

    }


}
