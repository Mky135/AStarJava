package aStar.util;

import aStar.controllers.MainController;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class PathFinding {
    private ArrayList<Cell> closedCells;
    private ArrayList<Cell> openedCells;
    private Cell start;
    private Cell end;

    private GridPane gridPane;

    public PathFinding(GridPane gridPane) {
        this.gridPane = gridPane;
        closedCells = new ArrayList<>();
        openedCells = new ArrayList<>();
        for (int i = 0; i < MainController.panes.size(); i++) {
            int row    = (int)(i / getColumnCount(gridPane));
            int column = i % getColumnCount(gridPane);
            Cell cell = new Cell(MainController.panes.get(i).getBackground().getFills().get(0).getFill(), new Point2D(row, column));
            if(cell.isClosed())
            {
                closedCells.add(cell);
            }
            else if(cell.isStart())
            {
                start = cell;
            }
            else if(cell.isEnd())
            {
                end = cell;
            }
            else{
                openedCells.add(cell);
            }
        }
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

}
