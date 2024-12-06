package edu.byuh.cis.cs300.gridsproject.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import edu.byuh.cis.cs300.gridsproject.R;
import edu.byuh.cis.cs300.gridsproject.logic.GameBoard;
import edu.byuh.cis.cs300.gridsproject.logic.GameMode;
import edu.byuh.cis.cs300.gridsproject.logic.Player;

public class Horse extends androidx.appcompat.widget.AppCompatImageView implements TickListener {

    private Grid grid;
    private boolean firstRun;
    private final GuiButton[] buttons;
    private final List<GuiToken> tokens;
    private final GameBoard engine;
    private final Timer tim;
    private MediaPlayer music;
    private final MediaPlayer buttonClickSound;
    public static GameMode mode;
    private static final Player COMPUTER = Player.X;

    public Horse(Context context) {
        super(context);
        mode = GameMode.ONE_PLAYER;
        firstRun = true;
        buttons = new GuiButton[10];
        tokens = new ArrayList<>();
        engine = new GameBoard();
        tim = new Timer();
        tim.register(this);
        buttonClickSound = MediaPlayer.create(getContext(), R.raw.buttonclick);
        music = MediaPlayer.create(getContext(), R.raw.danceofdevils);
        music.setLooping(true);
        if (SettingsActivity.getMusicPref(context)) {
            music.start();
        } else {
            music.pause();
        }
    }

    public void setGameMode(GameMode mode) {
        Horse.mode = (mode != null) ? mode : GameMode.ONE_PLAYER;
        reset();
        if (mode == GameMode.ONE_PLAYER) {
            setupSinglePlayer();
        } else if (mode == GameMode.TWO_PLAYER) {
            setupTwoPlayer();
        }
    }

    private void setupSinglePlayer() {
        engine.setGameMode(Player.X);
        Toast.makeText(getContext(), getContext().getString(R.string.one_player), Toast.LENGTH_SHORT).show();
    }

    private void setupTwoPlayer() {
        engine.setStartingPlayer(Player.X);
        Toast.makeText(getContext(), getContext().getString(R.string.two_player), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDraw(@NonNull Canvas c) {
        super.onDraw(c);
        setImageResource(R.drawable.pavilionbackground);
        setScaleType(ScaleType.FIT_XY);
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
        cleanupFallenTokens();
        float x = m.getX();
        float y = m.getY();

        if (m.getAction() == MotionEvent.ACTION_DOWN) {
            for (GuiButton b : buttons) {
                if (b.contains(x, y)) {
                    b.press();
                    buttonClickSound.start();
                }
            }
        } else if (m.getAction() == MotionEvent.ACTION_UP) {
            if (!GuiToken.anyMovers()) {
                boolean missed = true;
                for (GuiButton b : buttons) {
                    if (b.contains(x, y)) {
                        b.press();
                        handleButtonPress(b);
                        missed = false;
                    }
                    b.release();
                }
                if (missed) {
                    Toast t = Toast.makeText(getContext(), getContext().getString(R.string.please_press), Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        }
        return true;
    }

    private void cleanupFallenTokens() {
        List<GuiToken> toRemove = new ArrayList<>();
        for (GuiToken t : tokens) {
            if (t.isInvisible(getHeight())) {
                tim.unregister(t);
                toRemove.add(t);
            }
        }
        tokens.removeAll(toRemove);
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
        if (!GuiToken.anyMovers()) {
            Player winner = engine.checkForWin();
            if (winner != Player.BLANK) {
                tim.pause();
                String message;
                if (winner == Player.X) {
                    message = getContext().getString(R.string.apple_wins);
                } else if (winner == Player.O) {
                    message = getContext().getString(R.string.huckleberry_wins);
                } else {
                    message = getContext().getString(R.string.tie);
                }
                AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
                ab.setMessage(message)
                        .setTitle(getContext().getString(R.string.game_over))
                        .setCancelable(false)
                        .setPositiveButton(getContext().getString(R.string.play_again), (d,i) -> reset())
                        .setNegativeButton(getContext().getString(R.string.quit), (d, i) -> ((Activity)getContext()).finish());
                AlertDialog box = ab.create();
                box.show();
            } else {
                if (mode == GameMode.ONE_PLAYER
                        && engine.getCurrentPlayer() == COMPUTER) {
                    post(() -> {
                        int r = (int)(Math.random() * 10);
                        handleButtonPress(buttons[r]);
                    });
                }
            }
        }
        invalidate();
    }

    private void handleButtonPress(GuiButton b) {
        GuiToken tok = new GuiToken(engine.getCurrentPlayer(), b, getResources());
        engine.submitMove(b.getLabel());
        tokens.add(tok);
        tim.register(tok);
        setupAnimation(b, tok);
    }

    private void reset() {
        engine.clear();
        tokens.clear();
        tim.restart();
        tokens.forEach(tim::unregister);
        firstRun = true;
        mode = mode != null ? mode : GameMode.ONE_PLAYER;
        invalidate();
    }

    public void pauseMusic() {
        if (music != null && music.isPlaying()) {
            music.pause();
        }
    }

    public void resumeMusic() {
        if (music != null) {
            music.start();
        }
    }

    public void unloadMusic() {
        if (music != null) {
            music.stop();
            music.release();
            music = null;
        }
    }

}
