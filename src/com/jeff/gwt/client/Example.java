package com.jeff.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.Button;

import java.util.ArrayList;


public class Example implements EntryPoint {

    AbsolutePanel absolutePanel = new AbsolutePanel();
    Button reset = new Button("Reset");
    ArrayList<ArrayList<GridCell>> cells = new ArrayList<ArrayList<GridCell>>();
    Grid grid = new Grid();
    RadioButton radioButton1 = new RadioButton("radioGroup", "Beginer");
    RadioButton radioButton2 = new RadioButton("radioGroup", "Master");
    RadioButton radioButton3 = new RadioButton("radioGroup", "Custom Mines");

    TextBox customMineCountTextbox = new TextBox();

    private int mineNum = 10;

    public Example() {
        absolutePanel.setSize("1500", "1500");
        absolutePanel.add(reset);
        reset.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                resetGame();
            }
        });
        grid = new Grid(10, 10);
        grid.setBorderWidth(1);
        grid.setSize("1", "1");

        for (int i = 0; i < grid.getRowCount(); i++) {
            ArrayList<GridCell> curentRow = new ArrayList<GridCell>();
            cells.add(curentRow);
            for (int j = 0; j < grid.getColumnCount(); j++) {
                final int m = i;
                final int n = j;
                Button btn = new Button();
                btn.setSize("30", "30");
                btn.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        clickMines(m, n);
                    }
                });
                grid.setWidget(m, n, btn);

                GridCell cell = new GridCell(i, j, btn);
                curentRow.add(cell);
            }
        }
        FlowPanel fPanel = new FlowPanel();
        fPanel.add(radioButton1);
        fPanel.add(radioButton2);
        fPanel.add(radioButton3);
        fPanel.add(customMineCountTextbox);
        absolutePanel.add(fPanel);
        absolutePanel.add(grid);
        createRandomMines();
    }

    public void onModuleLoad() {
        RootPanel.get().add(absolutePanel);
    }

    public void createRandomMines() {
        ArrayList<CellLookup> list = new ArrayList<CellLookup>();
        for (int x = 0; x < cells.size(); x++) {
            for (int y = 0; y < cells.get(x).size(); y++) {
                list.add(new CellLookup(x, y));
            }
        }
        updateMineNumber();
        for (int a = 0; a < mineNum; a++) {
            int choice = (int) (Math.random() * list.size());
            cells.get(list.get(choice).x).get(list.get(choice).y).setMine(true);
            list.remove(choice);
        }
        for (int x = 0; x < cells.size(); x++) {
            for (int y = 0; y < cells.get(x).size(); y++) {
                GridCell currentCell = cells.get(x).get(y);
                if (!currentCell.isMine()) {
                    ArrayList<GridCell> neighboringCells = getNeighboringCells(x, y);
                    int neighborcount = 0;
                    for (GridCell c : neighboringCells) {
                        if(c.isMine()){
                            neighborcount++;
                        }
                    }
                    currentCell.setCount(neighborcount);
                }
            }
        }
    }

    public ArrayList<GridCell> getNeighboringCells(int x, int y){
        ArrayList<GridCell> result = new ArrayList<GridCell>();
        if (x > 0 && y > 0) {
            result.add(cells.get(x - 1).get(y - 1));
        }
        if (y > 0) {
            result.add(cells.get(x).get(y-1));
        }
        if (x < cells.size() - 1 && y > 0) {
            result.add(cells.get(x+1).get(y-1));
        }
        if (x > 0) {
            result.add(cells.get(x-1).get(y));
        }
        if (x < cells.size() - 1) {
            result.add(cells.get(x+1).get(y));
        }
        if (x > 0 && y < cells.get(0).size() - 1) {
            result.add(cells.get(x-1).get(y+1));
        }
        if (y < cells.size() - 1) {
            result.add(cells.get(x).get(y+1));
        }
        if (x < cells.size() - 1 && y < cells.get(0).size() - 1) {
            result.add(cells.get(x+1).get(y+1));
        }
        return result;
    }

    public void loseGame() {
        for (ArrayList<GridCell> cellRow : cells) {
            for (GridCell cell : cellRow) {
                cell.showContents();
            }
        }
        Window.alert("BOOM!!!!!!BOOM!!!!BOOM!!!!! TRY IT AGAIN!");
    }

    public void clickMines(int i, int j) {
        GridCell cell = cells.get(i).get(j);
        cell.showContents();
        if (cell.isMine()) {
            loseGame();
        } else {
            if (cell.hasNoSurroundingMines()) {
                ArrayList<CellLookup> toClear = new ArrayList<CellLookup>();
                toClear.add(new CellLookup(i, j));
                autoFillEmptyCell(toClear);
            }
            checkIfGameWon();
        }
    }

    public void resetGame() {
        for (ArrayList<GridCell> cellRow : cells) {
            for (GridCell cell : cellRow) {
                cell.hideContents();
                createRandomMines();
            }
        }
    }

    public void checkIfGameWon() {
        boolean won = true;
        for (ArrayList<GridCell> cellRow : cells) {
            for (GridCell cell : cellRow) {
                if (!cell.isMine() && cell.isEnabled()) {
                    won = false;
                }
            }
        }

        if (won) {
            Window.alert("Win!!!!!!!!");
        }
    }

    public void autoFillEmptyCell(ArrayList<CellLookup> toClear) {
        if (toClear.isEmpty()) return;

        int a = toClear.get(0).x;
        int b = toClear.get(0).y;
        toClear.remove(0);

        ArrayList<GridCell> neighboringCells = getNeighboringCells(a, b);
        for(GridCell c : neighboringCells){
            if(c.isEnabled()){
                c.showContents();
                if(c.hasNoSurroundingMines()){
                    toClear.add(new CellLookup(c.row, c.column));
                }
            }
        }
        autoFillEmptyCell(toClear);
    }

    public void updateMineNumber() {
        if (radioButton1.isChecked()) {
            mineNum = 1;
        } else if (radioButton2.isChecked()) {
            mineNum = 20;
        } else if (radioButton3.isChecked()) {
            mineNum = Integer.parseInt(customMineCountTextbox.getText());
        }
    }
}



