package aStar.controllers;

import aStar.util.PathFinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.net.URL;
import java.util.ArrayList;
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

    public static Paint addColor = Color.GRAY;
    public static Paint clearColor = Color.WHITE;
    public static Paint startColor = Color.RED;
    public static Paint endColor = Color.BLUE;

    public static ArrayList<AnchorPane> panes;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        add = false;
        clear = false;
        startLoc = new Point2D(-1,-1);
        endLoc = new Point2D(-1,-1);
        panes = new ArrayList<>();

        for(int i = 0; i < PathFinding.getColumnCount(grid); i++)
        {
            for(int j = 0; j < PathFinding.getRowCount(grid); j++)
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

    public void start()
    {
        new PathFinding(grid);
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
        panes = new ArrayList<>();
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
        int numCols = Integer.parseInt(columnField.getText());
        int numRows = Integer.parseInt(rowField.getText());

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

        for(int i = 0; i < PathFinding.getColumnCount(grid); i++)
        {
            for(int j = 0; j < PathFinding.getRowCount(grid); j++)
            {
                addPane(i, j);
            }
        }

        grid.setGridLinesVisible(true);
    }

    private void addPane(int colIndex, int rowIndex) {
        AnchorPane pane = new AnchorPane();
        pane.getStyleClass().add("cell");
        pane.setBackground(getBackground(clearColor));
        pane.setOnMouseClicked(e -> {
            if(add)
            {
                checkOverLap(colIndex, rowIndex, pane);
                pane.setBackground(getBackground(addColor));
            }
            if(clear)
            {
                checkOverLap(colIndex, rowIndex, pane);
                pane.setBackground(getBackground(clearColor));
            }
            if(startPlaced)
            {
                pane.setBackground(getBackground(startColor));
                Label s = new Label("S");
                s.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");
                checkOverLap(rowIndex, colIndex, pane);
                pane.getChildren().add(s);
                centerLabel(s, pane);
                startLoc = new Point2D(rowIndex, colIndex);
                startPlaced = false;
            }
            if(endPlaced)
            {
                checkOverLap(rowIndex, colIndex, pane);
                pane.setBackground(getBackground(endColor));
                Label end = new Label("E");
                end.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");
                pane.getChildren().add(end);
                centerLabel(end, pane);
                endLoc = new Point2D(rowIndex, colIndex);
                endPlaced = false;
            }
        });
        panes.add(pane);
        grid.add(pane, colIndex, rowIndex);
    }

    private Background getBackground(Paint paint)
    {
        return new Background(new BackgroundFill(paint, CornerRadii.EMPTY, Insets.EMPTY));
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

}
