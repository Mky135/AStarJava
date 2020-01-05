package aStar.util;

import aStar.controllers.MainController;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;

public class Cell extends AnchorPane
{
    private boolean traversal;
    private boolean start;
    private boolean end;
    private Point2D position;
    double fCost;
    private double gCost;
    private Cell parentCell;
    private Paint paint;

    public Cell(Paint paint, Point2D position)
    {
        traversal = false;
        start = false;
        end = false;
        fCost = 0;
        gCost = 0;
        this.paint = paint;
        checkPaint();
        this.position = position;
    }

    public void checkPaint()
    {
        if(paint == MainController.addColor)
        {
            traversal = false;
        }
        else if (paint == MainController.startColor)
        {
            start = true;
            gCost = 0;
            fCost = Integer.MAX_VALUE;
            traversal = false;
        }
        else if(paint == MainController.endColor)
        {
            end = true;
            traversal = true;
        }
        else{
            traversal = true;
        }
    }

    public void setPaint(Paint paint)
    {
        this.paint = paint;
        setBackground(getBackground(paint));
    }

    void setParentCell(Cell parentCell)
    {
        this.parentCell = parentCell;
    }

    Cell getParentCell()
    {
        return parentCell;
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

    public Point2D getPosition() {
        return position;
    }

    private Background getBackground(Paint paint)
    {
        return new Background(new BackgroundFill(paint, CornerRadii.EMPTY, Insets.EMPTY));
    }

    void setFCost(Cell start, Cell end)
    {
//        System.out.println("G: " + getGCost(start) + " H: " + getHCost(end));
//        fCost = getGCost(start) + getHCost(end);
    }

    void setfCost(Cell end)
    {
//        System.out.println("G: " + gCost + " H: " + getHCost(end));
        fCost = gCost + getHCost(end);
    }

    void setgCost(double g)
    {
        gCost = g;
    }

    double getFCost()
    {
        return fCost;
    }

    public double getgCost()
    {
        return gCost;
    }

    private double getGCost(Cell start)
    {
        System.out.println("Current pos: " + position);
        System.out.println("Start pos: " + start.getPosition());
        return Math.max(Math.abs(position.getX() - start.getPosition().getX()), Math.abs(position.getY() - start.getPosition().getY()));
    }

    private double getHCost(Cell end)
    {

//        System.out.println("Current pos: " + position);
//        System.out.println("End Pos: " + end.getPosition());
        return Math.sqrt ((position.getY()-end.position.getY())*(position.getY()-end.position.getY())
                              + (position.getX()-end.position.getX())*(position.getX()-end.position.getX()));
//        return Math.sqrt((2 * (position.getX() - end.getPosition().getX())) + (2*(position.getY() - end.getPosition().getY())));
//        return Math.max(Math.abs(position.getX() - end.getPosition().getX()), Math.abs(position.getY() - end.getPosition().getY()));
    }

    @Override
    public String toString()
    {
        return "X: " + position.getX() + " Y: " + position.getY();
    }
}
