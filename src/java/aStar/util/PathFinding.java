package aStar.util;

import aStar.controllers.MainController;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class PathFinding
{
    private ArrayList<Cell> cells;
    private Cell start;
    private Cell end;

    private GridPane gridPane;

    public PathFinding(GridPane gridPane, ArrayList<Cell> cells)
    {
        this.gridPane = gridPane;
        this.cells = cells;

        for(Cell cell : cells)
        {
            cell.checkPaint();
            if(cell.isStart())
            {
                start = cell;
            }
            if(cell.isEnd())
            {
                end = cell;
            }
//            cell.getChildren().add(new Label(cell.toString()));
        }

//        for(Cell cell : cells)
//        {
//            if(!cell.isStart() && !cell.isEnd())
//            {
//                Label label = new Label("F: " + cell.getFCost());
//                label.setLayoutY(30);
//                cell.getChildren().add(label);
//            }
//        }
    }

    public static int getRowCount(GridPane grid)
    {
        int numRows = grid.getRowConstraints().size();
        for(int i = 0; i < grid.getChildren().size(); i++)
        {
            Node child = grid.getChildren().get(i);
            if(child.isManaged())
            {
                Integer rowIndex = GridPane.getRowIndex(child);
                if(rowIndex != null)
                {
                    numRows = Math.max(numRows, rowIndex + 1);
                }
            }
        }
        return numRows;
    }

    public static int getColumnCount(GridPane grid)
    {
        int numColumns = grid.getColumnConstraints().size();
        for(int i = 0; i < grid.getChildren().size(); i++)
        {
            Node child = grid.getChildren().get(i);
            if(child.isManaged())
            {
                Integer columnIndex = GridPane.getColumnIndex(child);
                if(columnIndex != null)
                {
                    numColumns = Math.max(numColumns, columnIndex + 1);
                }
            }
        }
        return numColumns;
    }

    public void aStarPathFinding()
    {
        boolean endFound = false;
        ArrayList<Cell> open = new ArrayList<>();
        ArrayList<Cell> closed = new ArrayList<>();
        Cell current = start;
        open.add(current);
//        System.out.println("Start Parent: " + start);
        while(!endFound)
        {
            Cell lowestCell = new Cell(null, null);
            lowestCell.fCost = Integer.MAX_VALUE;

            for(Cell cell : open)
            {
                if(cell.getFCost() <= lowestCell.getFCost())
                {
                    lowestCell = cell;
                }
            }
            current = lowestCell;

            open.remove(current);
            closed.add(current);

            for(Cell cell : getNeighbours(current))
            {
                if(cell.isEnd())
                {
                    endFound = true;
                    cell.setParentCell(current);
                    current = cell;
                    break;
                }

                if(closed.contains(cell) || !cell.isTraversal())
                {
                    continue;
                }

//                cell.setFCost(start, end);
                if(!open.contains(cell))
                {
                    cell.setParentCell(current);
                    if(!open.contains(cell))
                    {
                        open.add(cell);
                        cell.setPaint(Paint.valueOf("Yellow"));
                    }
                }
            }
        }
        System.out.println("End Found");
        ArrayList<Cell> path = new ArrayList<>();

        while(true)
        {
            path.add(current);
            if(current.getParentCell() != null)
            {
                current = current.getParentCell();
            }
            else
            {
                break;
            }
        }

        System.out.println(path);
        Platform.runLater(() -> drawPath(path));
    }

    private ArrayList<Cell> getNeighbours(Cell parent)
    {
        ArrayList<Cell> neighbours = new ArrayList<>();
        double i = parent.getPosition().getY(); //row
        double j = parent.getPosition().getX(); //column

        Cell cell;
        //North
        cell = addThatCell(i - 1, j);
        if(cell != null)
        {
            cell.setgCost(parent.getgCost() + 1);
            cell.setfCost(end);
            neighbours.add(cell);
        }
        //South
        cell = addThatCell(i + 1, j);
        if(cell != null)
        {
            cell.setgCost(parent.getgCost() + 1);
            cell.setfCost(end);
            neighbours.add(cell);
        }
        //East
        cell = addThatCell(i, j + 1);
        if(cell != null)
        {
            cell.setgCost(parent.getgCost() + 1);
            cell.setfCost(end);
            neighbours.add(cell);
        }
        //West
        cell = addThatCell(i, j - 1);
        if(cell != null)
        {
            cell.setgCost(parent.getgCost() + 1);
            cell.setfCost(end);
            neighbours.add(cell);
        }
        //NE
        cell = addThatCell(i - 1, j + 1);
        if(cell != null)
        {
            cell.setgCost(parent.getgCost() + 1.414);
            cell.setfCost(end);
            neighbours.add(cell);
        }
        //NW
        cell = addThatCell(i - 1, j - 1);
        if(cell != null)
        {
            cell.setgCost(parent.getgCost() + 1.414);
            cell.setfCost(end);
            neighbours.add(cell);
        }
        //SE
        cell = addThatCell(i + 1, j + 1);
        if(cell != null)
        {
            cell.setgCost(parent.getgCost() + 1.414);
            cell.setfCost(end);
            neighbours.add(cell);
        }
        //SW
        cell = addThatCell(i + 1, j - 1);
        if(cell != null)
        {
            cell.setgCost(parent.getgCost() + 1.414);
            cell.setfCost(end);
            neighbours.add(cell);
        }

        return neighbours;
    }

    private Cell addThatCell(double row, double column)
    {
        Cell lastCell = new Cell(null, new Point2D(0, 0));
        for(Cell cell : cells)
        {
            if(cell.getPosition().getX() > lastCell.getPosition().getX() || cell.getPosition().getY() > lastCell.getPosition().getY())
            {
                lastCell = cell;
            }
        }

        if(row >= 0 && column >= 0)
        {
            if(row <= lastCell.getPosition().getY() && column <= lastCell.getPosition().getX())
            {
                for(Cell cell : cells)
                {
                    if(cell.getPosition().equals(new Point2D(column, row)))
                    {
                        return cell;
                    }
                }
            }
        }
        return null;
    }

    private void drawPath(ArrayList<Cell> path)
    {
        for(Cell cell : path)
        {
            if(!cell.isEnd() && !cell.isStart())
            {
                cell.setPaint(Paint.valueOf("GREEN"));
            }
        }
    }

    private boolean isBetterPath(Cell current, Cell cell)
    {
        return current.getFCost() >= cell.getFCost();
    }
}
