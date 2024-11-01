package edu.byuh.cis.cs300.gridsproject.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import edu.byuh.cis.cs300.gridsproject.logic.GameBoard;
import edu.byuh.cis.cs300.gridsproject.logic.Player;

public class Horse extends View implements TickListener {

    private Grid grid;
    private boolean firstRun;
    private final GuiButton[] buttons;
    private final List<GuiToken> tokens;
    private final GameBoard engine;
    private final Timer tim;

    public Horse(Context context) {
        super(context);
        firstRun = true;
        buttons = new GuiButton[10];
        tokens = new ArrayList<>();
        engine = new GameBoard();
        tim = new Timer();
        tim.register(this);
    }

    @Override
    public void onDraw(@NonNull Canvas c) {
        super.onDraw(c);
        c.drawColor(Color.YELLOW);
        if (firstRun) {
            init();
            firstRun = false;
        }
        grid.draw(c);
        for (GuiToken tok : tokens) {
            tok.draw(c);
        }
        for (GuiButton b : buttons) {
            b.draw(c);
        }
    }

    private void init() {
        float w = getWidth();
        float h = getHeight();
        float unit = w/16f;
        float gridX = unit * 2.5f;
        float cellSize = unit * 2.3f;
        float gridY = unit * 9;
        grid = new Grid(gridX, gridY, cellSize);
        float buttonTop = gridY - cellSize;
        float buttonLeft = gridX - cellSize;
        //top buttons
        buttons[0] = new GuiButton('1', this, buttonLeft + cellSize*1, buttonTop, cellSize);
        buttons[1] = new GuiButton('2', this, buttonLeft + cellSize*2, buttonTop, cellSize);
        buttons[2] = new GuiButton('3', this, buttonLeft + cellSize*3, buttonTop, cellSize);
        buttons[3] = new GuiButton('4', this, buttonLeft + cellSize*4, buttonTop, cellSize);
        buttons[4] = new GuiButton('5', this, buttonLeft + cellSize*5, buttonTop, cellSize);
        //left buttons
        buttons[5] = new GuiButton('A', this, buttonLeft, buttonTop + cellSize*1, cellSize);
        buttons[6] = new GuiButton('B', this, buttonLeft, buttonTop + cellSize*2, cellSize);
        buttons[7] = new GuiButton('C', this, buttonLeft, buttonTop + cellSize*3, cellSize);
        buttons[8] = new GuiButton('D', this, buttonLeft, buttonTop + cellSize*4, cellSize);
        buttons[9] = new GuiButton('E', this, buttonLeft, buttonTop + cellSize*5, cellSize);
    }

    @Override
    public boolean onTouchEvent(MotionEvent m) {
        float x = m.getX();
        float y = m.getY();

        if (m.getAction() == MotionEvent.ACTION_DOWN) {
            for (GuiButton b : buttons) {
                if (b.contains(x, y)) {
                    b.press();
                }
            }
        } else if (m.getAction() == MotionEvent.ACTION_UP) {
            if (!GuiToken.anyMovers()) {
                boolean missed = true;
                for (GuiButton b : buttons) {
                    if (b.contains(x, y)) {
                        b.press();
                        GuiToken tok = new GuiToken(engine.getCurrentPlayer(), b, getResources());
                        engine.submitMove(b.getLabel());
                        tokens.add(tok);
                        tim.register(tok);
                        setupAnimation(b, tok);
                        missed = false;
                        List<GuiToken> tokensToRemove = new ArrayList<>();
                        for (GuiToken token : tokens) {
                            if (tok.isInvisible(getHeight())) {
                                tokensToRemove.add(token);
                            }
                        }
                        for (GuiToken token : tokensToRemove) {
                            tim.unregister(token);
                        }
                        Player winner = engine.checkForWin();
                        if (winner != Player.BLANK) {
                            showEndGameDialog(winner);
                        }
                    }
                    b.release();
                }
                if (missed) {
                    Toast t = Toast.makeText(getContext(), "Please press a button", Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        }
        return true;
    }


    private void setupAnimation(GuiButton b, GuiToken tok) {
        List<GuiToken> neighbors = new ArrayList<>();
        neighbors.add(tok);
        if (b.isTopButton()) {
            char col = b.getLabel();
            for (char row = 'A'; row <= 'E'; row++) {
                GuiToken other = findTokenAt(row, col);
                if (other != null) {
                    neighbors.add(other);
                } else {
                    break;
                }
            }
            for (GuiToken n : neighbors) {
                n.startMovingDown();
            }
        } else {
            char row = b.getLabel();
            for (char col = '1'; col <= '5'; col++) {
                GuiToken other = findTokenAt(row, col);
                if (other != null) {
                    neighbors.add(other);
                } else {
                    break;
                }
            }
            for (GuiToken n : neighbors) {
                n.startMovingRight();
            }
        }
    }

    private void showEndGameDialog(Player winner) {
        String message;
        if (winner == Player.TIE) {
            message = "It's a tie!";
        } else {
            message = "Player " + winner + " wins!";
        }

        new AlertDialog.Builder(getContext())
                .setTitle("Congratulations!")
                .setMessage(message)
                //lambda syntax
                .setPositiveButton("PLAY AGAIN", (dialog, which) -> {
                    engine.clear();
                    tokens.clear();
                    invalidate();
                })
                //anonymous syntax
                .setNegativeButton("QUIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int j) {
                        ((Activity)getContext()).finish();
                    }
                })
                .show();
    }

    private GuiToken findTokenAt(char row, char col) {
        for (GuiToken t : tokens) {
            if (t.matches(row,col)) {
                return t;
            }
        }
        return null;
    }

    @Override
    public void onTick() {
        invalidate();
    }
}