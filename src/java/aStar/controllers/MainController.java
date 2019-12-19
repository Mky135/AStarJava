package aStar.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable
{
    @FXML
    Button refreshButton;

    @FXML
    Button addButton;

    @FXML
    Button clearButton;

    @FXML
    Button startButton;

    @FXML
    Button endButton;

    @FXML
    TextField columnField;

    @FXML
    TextField rowField;

    private boolean add;
    private boolean clear;
    private boolean startPlaced;
    private boolean endPlaced;
    private Point2D startLoc;
    private Point2D endLoc;

    @FXML
    GridPane grid;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        add = false;
        clear = false;
        startLoc = new Point2D(-1,-1);
        endLoc = new Point2D(-1,-1);

        for(int i = 0; i < getColumnCount(); i++)
        {
            for(int j = 0; j < getRowCount(); j++)
            {
                addPane(i, j);
            }
        }
    }

    public void add()
    {
        clear = false;
        clearButton.setStyle("-fx-background-color: gray;");

        add = !add;
        if(add)
        {
            addButton.setStyle("-fx-background-color: yellow;");
        }
        else
        {
            addButton.setStyle("-fx-background-color: gray;");
        }
    }

    public void clear()
    {
        clear = !clear;
        if(clear)
        {
            clearButton.setStyle("-fx-background-color: yellow;");
        }
        else
        {
            clearButton.setStyle("-fx-background-color: gray;");
        }

        add = false;
        addButton.setStyle("-fx-background-color: gray;");
    }


    public void placeS()
    {
        startPlaced = true;
        startButton.setDisable(true);
        clear = false;
        add = false;
    }

    public void placeE()
    {
        endPlaced = true;
        endButton.setDisable(true);
        clear = false;
        add = false;
        grayBothButton();
    }

    public void refresh()
    {
        clear = false;
        add = false;
        startPlaced = false;
        endPlaced = false;
        startButton.setDisable(false);
        endButton.setDisable(false);
        grayBothButton();
        grid.getChildren().clear();
        grid.getColumnConstraints().clear();
        grid.getRowConstraints().clear();
        int numCols = Integer.valueOf(columnField.getText());
        int numRows = Integer.valueOf(rowField.getText());

        for (int i = 0; i < numCols; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / numCols);
            grid.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < numRows; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / numRows);
            grid.getRowConstraints().add(rowConst);
        }

        for(int i = 0; i < getColumnCount(); i++)
        {
            for(int j = 0; j < getRowCount(); j++)
            {
                addPane(i, j);
            }
        }

        grid.setGridLinesVisible(true);
    }

    private void addPane(int colIndex, int rowIndex) {
        AnchorPane pane = new AnchorPane();
        pane.getStyleClass().add("cell");
        pane.setOnMouseClicked(e -> {
            if(add)
            {
                checkOverLap(colIndex, rowIndex, pane);
                pane.setStyle("-fx-background-color: grey;");
            }
            if(clear)
            {
                checkOverLap(colIndex, rowIndex, pane);
                pane.setStyle("-fx-background-color: white;");
            }
            if(startPlaced)
            {
                pane.setStyle("-fx-background-color: blue");
                Label s = new Label("S");
                checkOverLap(colIndex, rowIndex, pane);
                pane.getChildren().add(s);
                centerLabel(s, pane);
                startLoc = new Point2D(colIndex, rowIndex);
                startPlaced = false;
            }
            if(endPlaced)
            {
                checkOverLap(colIndex, rowIndex, pane);
                pane.setStyle("-fx-background-color: red;");
                Label end = new Label("E");
                pane.getChildren().add(end);
                centerLabel(end, pane);
                endLoc = new Point2D(colIndex, rowIndex);
                endPlaced = false;
            }
        });
        grid.add(pane, colIndex, rowIndex);
    }

    private void grayBothButton()
    {
        addButton.setStyle("-fx-background-color: gray;");
        clearButton.setStyle("-fx-background-color: gray;");
    }

    private void checkOverLap(int colIndex, int rowIndex, AnchorPane pane)
    {
        overLapWithStart(colIndex, rowIndex, pane);
        overLapWithEnd(colIndex, rowIndex, pane);
    }

    private void overLapWithStart(int colIndex, int rowIndex, AnchorPane pane)
    {
        if(startLoc.getX() == colIndex && startLoc.getY() == rowIndex)
        {
            startLoc = new Point2D(-1, -1);
            startButton.setDisable(false);
            startPlaced = false;
            pane.getChildren().clear();
        }
    }

    private void overLapWithEnd(int colIndex, int rowIndex, AnchorPane pane)
    {
        if(endLoc.getX() == colIndex && endLoc.getY() == rowIndex)
        {
            endLoc = new Point2D(-1, -1);
            endPlaced = false;
            endButton.setDisable(false);
            pane.getChildren().clear();
        }
    }

    private void centerLabel(Label label, AnchorPane pane)
    {
        label.setLayoutX(pane.getWidth()/2);
        label.setLayoutY(pane.getHeight()/2);
    }
    private int getRowCount() {
        int numRows = grid.getRowConstraints().size();
        for (int i = 0; i < grid.getChildren().size(); i++) {
            Node child = grid.getChildren().get(i);
            if (child.isManaged()) {
                Integer rowIndex = GridPane.getRowIndex(child);
                if(rowIndex != null){
                    numRows = Math.max(numRows,rowIndex+1);
                }
            }
        }
        return numRows;
    }

    private int getColumnCount()
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
                    numColumns = Math.max(numColumns, columnIndex+1);
                }
            }
        }
        return numColumns;
    }
}
