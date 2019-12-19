package aStar.util;

import aStar.controllers.MainController;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Cell
{
    private boolean closed;
    private boolean start;
    private boolean end;
    private Point2D position;

    public Cell(Paint paint, Point2D position)
    {
        closed = false;
        start = false;
        end = false;
        if(paint == MainController.addColor)
        {
            closed = true;
        }
        else if (paint == MainController.startColor)
        {
            start = true;
        }
        else if(paint == MainController.endColor)
        {
            end = true;
        }
        else{
            closed = false;
        }

        this.position = position;
    }

    boolean isClosed() {
        return closed;
    }

    public boolean isStart() {
        return start;
    }

    public boolean isEnd() {
        return end;
    }

    public Point2D getPosition() {
        return position;
    }

    @Override
    public String toString()
    {
        return "X: " + position.getX() + " Y: " + position.getY();
    }
}
