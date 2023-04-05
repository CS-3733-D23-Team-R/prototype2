package edu.wpi.teamR.pathfinding;

import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
public class Line extends Path{
    private double startX, startY, endX, endY;

    public Line(double startX, double startY, double endX, double endY) {
        super();

        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;

        // Draw Line
        strokeProperty().bind(fillProperty());
        setFill(Color.RED);
        setStrokeWidth(6);
        getElements().add(new MoveTo(startX, startY));
        getElements().add(new LineTo(endX, endY));
    }
}
