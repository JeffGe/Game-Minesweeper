package com.jeff.gwt.client;

import com.google.gwt.user.client.ui.Button;

public class GridCell {
    static final int MINE = 10;

    public final int row;
    public final int column;

    private final Button button;
    private int count;

    public GridCell(int row, int column, Button button) {
        this.row = row;
        this.column = column;
        this.button = button;
    }

    public boolean isMine() {
        return count == MINE;
    }

    public void setMine(boolean b) {
        count = b ? MINE : 0;
    }

    public void showContents() {
        if (isMine()) {
            button.setText("M");
        } else {
            button.setText(count + "");
        }
        button.setEnabled(false);
    }

    public void hideContents() {
        button.setEnabled(true);
        button.setText("");
    }

    public boolean isEnabled() {
        return button.isEnabled();
    }

    public boolean hasNoSurroundingMines() {
        return count == 0;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
