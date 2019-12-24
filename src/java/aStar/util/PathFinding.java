package aStar.util;

import aStar.controllers.MainController;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class PathFinding {
    private ArrayList<Cell> cells;
    private Cell start;
    private Cell end;

    private GridPane gridPane;

    public PathFinding(GridPane gridPane) {
        this.gridPane = gridPane;
        cells = new ArrayList<>();

        for (int i = 0; i < MainController.panes.size(); i++) {
            int row    = (i / getColumnCount(gridPane));
            int column = i % getColumnCount(gridPane);
            Cell cell = new Cell(MainController.panes.get(i).getBackground().getFills().get(0).getFill(), new Point2D(column, row));
            cells.add(cell);
            if(cell.isStart())
            {
                start = cell;
            }
            else if(cell.isEnd())
            {
                end = cell;
            }
        }
        aStarPathFinding();
    }

    public static int getRowCount(GridPane grid) {
        int numRows = grid.getRowConstraints().size();
        for (int i = 0; i < grid.getChildren().size(); i++) {
            Node child = grid.getChildren().get(i);
            if (child.isManaged()) {
                Integer rowIndex = GridPane.getRowIndex(child);
                if (rowIndex != null) {
                    numRows = Math.max(numRows, rowIndex + 1);
                }
            }
        }
        return numRows;
    }

    public static int getColumnCount(GridPane grid) {
        int numColumns = grid.getColumnConstraints().size();
        for (int i = 0; i < grid.getChildren().size(); i++) {
            Node child = grid.getChildren().get(i);
            if (child.isManaged()) {
                Integer columnIndex = GridPane.getColumnIndex(child);
                if (columnIndex != null) {
                    numColumns = Math.max(numColumns, columnIndex + 1);
                }
            }
        }
        return numColumns;
    }

    private void aStarPathFinding()
    {
        boolean endFound = false;
        ArrayList<Cell> open = new ArrayList<>();
        ArrayList<Cell> closed = new ArrayList<>();
        Cell current = start;
        open.add(current);
        System.out.println("Start Parent: " + current.getParent());
        while(!endFound)
        {
            for(Cell cell : open)
            {
                if(cell.getFCost() <= current.getFCost())
                {
                    current = cell;
                }
            }

            open.remove(current);
            closed.add(current);

            for(Cell cell: getNeighbours(current))
            {

                if(cell.isEnd())
                {
                    endFound = true;
                    cell.setParent(current);
                    current = cell;
                    break;
                }

                if(closed.contains(cell) || !cell.isTraversal())
                {
                    continue;
                }

                cell.setFCost(start, end);
                if(!open.contains(cell))
                {
                    cell.setParent(current);
                    if(!open.contains(cell))
                    {
                        open.add(cell);
                    }
                }
            }
        }
        System.out.println("End Found");
        ArrayList<Cell> path = new ArrayList<>();

        while(true)
        {
            path.add(current);
            if(current.getParent() != null)
            {
                current = current.getParent();
            }
            else
            {
                break;
            }
        }

        System.out.println(path);
    }

    private ArrayList<Cell> getNeighbours(Cell parent)
    {
        ArrayList<Cell> neighbours = new ArrayList<>();
        double i = parent.getPosition().getY(); //row
        double j = parent.getPosition().getX(); //column
        Cell cell;
        //North
        cell = addThatCell(i-1, j);
        if(cell != null)
         neighbours.add(cell);
        //South
        cell = addThatCell(i+1, j);
        if(cell != null)
            neighbours.add(cell);
        //East
        cell = addThatCell(i, j+1);
        if(cell != null)
            neighbours.add(cell);
        //West
        cell = addThatCell(i, j-1);
        if(cell != null)
            neighbours.add(cell);
        //NE
        cell = addThatCell(i-1, j+1);
        if(cell != null)
            neighbours.add(cell);
        //NW
        cell = addThatCell(i-1, j-1);
        if(cell != null)
            neighbours.add(cell);
        //SE
        cell = addThatCell(i+1, j+1);
        if(cell != null)
            neighbours.add(cell);
        //SW
        cell = addThatCell(i+1, j-1);
        if(cell != null)
            neighbours.add(cell);

        return neighbours;
    }

    private Cell addThatCell(double row, double column)
    {
        int index;
        if(row >= 0 && column >= 0)
        {
            index = (int) getIndex(row, column);
            System.out.println(index);
            if(index <= cells.size() - 1 && index >= 0)
            {
                return cells.get(index);
            }
        }
        return null;
    }

    private boolean isOutOfBounds(double row, double columns)
    {
        Cell lastCell = cells.get(cells.size()-1);
        if(row < 0 || columns < 0)
        {
            return true;
        }
       return row>lastCell.getPosition().getY() || columns > lastCell.getPosition().getX();
    }

    private boolean isBetterPath(Cell current, Cell cell)
    {
        return current.getFCost() >= cell.getFCost();
    }

    private double getIndex(double row, double column)
    {
        return (row * getColumnCount(gridPane)) + column;
    }
}
