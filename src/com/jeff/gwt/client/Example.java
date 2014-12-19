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
    Button[][] buttons = new Button[10][10];
    int count[][] = new int[10][10];
    Grid grid = new Grid();
    final int MINE = 10;
    RadioButton radioButton1 = new RadioButton("radioGroup", "Beginer");
    RadioButton radioButton2 = new RadioButton("radioGroup", "Master");
    RadioButton radioButton3 = new RadioButton("radioGroup", "Custom Mines");
//    boolean won = true;

    TextBox tb = new TextBox();

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

        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons.length; j++) {
                final int m = i;
                final int n = j;
                buttons[i][j] = new Button();
                buttons[i][j].setSize("30", "30");
                buttons[i][j].addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        clickMines(m, n);
                    }
                });
                grid.setWidget(m, n, buttons[i][j]);
            }
        }
        FlowPanel fPanel = new FlowPanel();
        fPanel.add(radioButton1);
        fPanel.add(radioButton2);
        fPanel.add(radioButton3);
        fPanel.add(tb);
        absolutePanel.add(fPanel);
        absolutePanel.add(grid);
        createRandomMines();
    }

    public void onModuleLoad() {
        new Example();
        RootPanel.get().add(absolutePanel);
    }

    public void createRandomMines() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int x = 0; x < count.length; x++) {
            for (int y = 0; y < count[0].length; y++) {
                list.add(x * 100 + y);
            }
        }
        count = new int[10][10];
        setLevel();
        for (int a = 0; a < mineNum; a++) {
            int choice = (int) (Math.random() * list.size());
            count[list.get(choice) / 100][list.get(choice) % 100] = MINE;
            list.remove(choice);
        }
        for (int x = 0; x < count.length; x++) {
            for (int y = 0; y < count[0].length; y++) {
                if (count[x][y] != MINE) {
                    int neighborcount = 0;
                    if (x > 0 && y > 0 && count[x - 1][y - 1] == MINE) {
                        neighborcount++;
                    }
                    if (y > 0 && count[x][y - 1] == MINE) {
                        neighborcount++;
                    }
                    if (x < count.length - 1 && y > 0 && count[x + 1][y - 1] == MINE) {
                        neighborcount++;
                    }
                    if (x > 0 && count[x - 1][y] == MINE) {
                        neighborcount++;
                    }
                    if (x < count.length - 1 && count[x + 1][y] == MINE) {
                        neighborcount++;
                    }
                    if (x > 0 && y < count[0].length - 1 && count[x - 1][y + 1] == MINE) {
                        neighborcount++;
                    }
                    if (y < count.length - 1 && count[x][y + 1] == MINE) {
                        neighborcount++;
                    }
                    if (x < count.length - 1 && y < count[0].length - 1 && count[x + 1][y + 1] == MINE) {
                        neighborcount++;
                    }
                    count[x][y] = neighborcount;
                }
            }
        }
    }

    public void loseGame() {

        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[0].length; j++) {

                if (buttons[i][j].isEnabled()) {
                    if (count[i][j] != MINE) {
                        buttons[i][j].setText(count[i][j] + "");
                        buttons[i][j].setEnabled(false);
                    } else {
                        buttons[i][j].setText("M");
                        buttons[i][j].setEnabled(false);
                    }
                }
            }
        }
        Window.alert("BOOM!!!!!!BOOM!!!!BOOM!!!!! TRY IT AGAIN!");
    }

    public void clickMines(int i, int j) {
        if (count[i][j] == MINE) {
            buttons[i][j].setText("M");
            loseGame();
        } else if (count[i][j] == 0) {
            buttons[i][j].setText(count[i][j] + "");
            buttons[i][j].setEnabled(false);
            ArrayList<Integer> toClear = new ArrayList<Integer>();
            toClear.add(i * 100 + j);
            autoFillEmptyCell(toClear);
            winGame();
        } else {
            buttons[i][j].setText(count[i][j] + "");
            buttons[i][j].setEnabled(false);
            winGame();
        }
    }

    public void resetGame() {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[0].length; j++) {
                buttons[i][j].setEnabled(true);
                buttons[i][j].setText("");
                createRandomMines();
                mineNum = 10;
            }
        }
    }

    public void winGame() {
        boolean won = false;

        for (int i = 0; i < count.length; i++) {
            for (int j = 0; j < count[0].length; j++) {
                if (count[i][j] != MINE && buttons[i][j].isEnabled()) {
                    won = false;
                }
            }
        }
        if (won = true) {
            Window.alert("Win!!!!!!!!");
        }
    }

    public void autoFillEmptyCell(ArrayList<Integer> toClear) {
        if (toClear.size() == 0) {

        } else {
            int a = toClear.get(0) / 100;
            int b = toClear.get(0) % 100;
            toClear.remove(0);

            if (a > 0 && b > 0 && buttons[a - 1][b - 1].isEnabled()) {
                buttons[a - 1][b - 1].setText(count[a - 1][b - 1] + "");
                buttons[a - 1][b - 1].setEnabled(false);
                if (count[a - 1][b - 1] == 0) {
                    toClear.add((a - 1) * 100 + (b - 1));
                }
            }

            if (b > 0 && buttons[a][b - 1].isEnabled()) {
                buttons[a][b - 1].setText(count[a][b - 1] + "");
                buttons[a][b - 1].setEnabled(false);
                if (count[a][b - 1] == 0) {
                    toClear.add(a * 100 + (b - 1));
                }
            }
            if (a < count.length - 1 && b > 0 && buttons[a + 1][b - 1].isEnabled()) {
                buttons[a + 1][b - 1].setText(count[a + 1][b - 1] + "");
                buttons[a + 1][b - 1].setEnabled(false);
                if (count[a + 1][b - 1] == 0) {
                    toClear.add((a + 1) * 100 + (b - 1));
                }
            }
            if (a > 0 && buttons[a - 1][b].isEnabled()) {
                buttons[a - 1][b].setText(count[a - 1][b] + "");
                buttons[a - 1][b].setEnabled(false);
                if (count[a - 1][b] == 0) {
                    toClear.add((a - 1) * 100 + b);
                }
            }

            if (a < count.length - 1 && buttons[a + 1][b].isEnabled()) {
                buttons[a + 1][b].setText(count[a + 1][b] + "");
                buttons[a + 1][b].setEnabled(false);
                if (count[a + 1][b] == 0) {
                    toClear.add((a + 1) * 100 + b);
                }
            }

            if (a > 0 && b < count[0].length - 1 && buttons[a - 1][b + 1].isEnabled()) {
                buttons[a - 1][b + 1].setText(count[a - 1][b + 1] + "");
                buttons[a - 1][b + 1].setEnabled(false);
                if (count[a - 1][b + 1] == 0) {
                    toClear.add((a - 1) * 100 + (b + 1));
                }
            }
            if (b < count[0].length - 1 && buttons[a][b + 1].isEnabled()) {
                buttons[a][b + 1].setText(count[a][b + 1] + "");
                buttons[a][b + 1].setEnabled(false);
                if (count[a][b + 1] == 0) {
                    toClear.add(a * 100 + (b + 1));
                }
            }
            if (a < count.length - 1 && b < count[0].length - 1 && buttons[a + 1][b + 1].isEnabled()) {
                buttons[a + 1][b + 1].setText(count[a + 1][b + 1] + "");
                buttons[a + 1][b + 1].setEnabled(false);
                if (count[a + 1][b + 1] == 0) {
                    toClear.add((a + 1) * 100 + (b + 1));
                }
            }
            autoFillEmptyCell(toClear);
        }
    }

    public void setLevel() {
        if (radioButton1.isChecked()) {
            mineNum = 1;
        } else if (radioButton2.isChecked()) {
            mineNum = 20;
        } else if (radioButton3.isChecked()) {
            mineNum = Integer.parseInt(tb.getText());
        }
    }
}



