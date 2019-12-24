package aStar.util;

import aStar.controllers.MainController;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Cell
{
    private boolean traversal;
    private boolean start;
    private boolean end;
    private Point2D position;
    private double fCost;
    private Cell parent;

    public Cell(Paint paint, Point2D position)
    {
        traversal = false;
        start = false;
        end = false;
        fCost = 0;

        if(paint == MainController.addColor)
        {
            traversal = false;
        }
        else if (paint == MainController.startColor)
        {
            start = true;
            fCost = Integer.MAX_VALUE;
            traversal = true;
        }
        else if(paint == MainController.endColor)
        {
            end = true;
            traversal = true;
        }
        else{
            traversal = true;
        }

        this.position = position;
    }

    void setParent(Cell parent)
    {
        this.parent = parent;
    }

    Cell getParent()
    {
        return parent;
    }

    boolean isTraversal() {
        return traversal;
    }

    boolean isStart() {
        return start;
    }

    boolean isEnd() {
        return end;
    }

    Point2D getPosition() {
        return position;
    }

    void setFCost(Cell start, Cell end)
    {
        System.out.println("G: " + getGCost(start) + " H: " + getHCost(end));
        fCost = getGCost(start) + getHCost(end);
    }

    double getFCost()
    {
        return fCost;
    }

    private double getGCost(Cell start)
    {
        System.out.println("Current pos: " + position);
        System.out.println("Start pos: " + start.getPosition());
        return Math.sqrt(((position.getX() - start.getPosition().getX())*2) + ((position.getY()-start.getPosition().getY())*2));
    }

    private double getHCost(Cell end)
    {
        System.out.println("Current pos: " + position);
        System.out.println("End Pos: " + end.getPosition());
        return Math.max(Math.abs(position.getX() - end.getPosition().getX()), Math.abs(position.getY() - end.getPosition().getY()));
    }

    @Override
    public String toString()
    {
        return "X: " + position.getX() + " Y: " + position.getY();
    }
}
