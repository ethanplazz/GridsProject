package edu.byuh.cis.cs300.gridsproject.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Paint;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.byuh.cis.cs300.gridsproject.R;

public class Horse extends View implements TickListener {

    private final Paint paint;
    private final List<GridButton> buttons;
    private final ArrayList<GuiToken> tokens;
    private boolean isXTokenNext = true;
    private final TokenHandler tokenHandler;

    public Horse(Context c) {
        super(c);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(8f);
        buttons = new ArrayList<>();
        tokens = new ArrayList<>();
        tokenHandler = new TokenHandler();
        tokenHandler.registerTickListener(this);
        tokenHandler.sendEmptyMessageDelayed(0, 30);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        buttons.clear();
        createButtons(getResources());
    }

    private void createButtons(Resources res) {
        int width = getWidth();
        int height = getHeight();
        int gridSize = Math.min(width, height) * 7 / 10;
        int cellSize = gridSize / 5;
        for (int i = 0; i < 5; i++) {
            char label = (char) ('1' + i);
            float left = (width - gridSize) / 2f + i * cellSize;
            float top = (height - gridSize) / 2f - cellSize;
            buttons.add(new GridButton(res, label, left, top, cellSize, cellSize, R.drawable.unpressed_button, R.drawable.pressedbutton));
        }
        for (int i = 0; i < 5; i++) {
            char label = (char) ('A' + i);
            float left = (width - gridSize) / 2f - cellSize;
            float top = (height - gridSize) / 2f + i * cellSize;
            buttons.add(new GridButton(res, label, left, top, cellSize, cellSize, R.drawable.unpressed_button, R.drawable.pressedbutton));
        }
    }

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        c.drawColor(Color.YELLOW);
        int width = getWidth();
        int height = getHeight();
        int gridSize = Math.min(width, height) * 7 / 10;
        int cellSize = gridSize / 5;
        int startX = (width - gridSize) / 2;
        int startY = (height - gridSize) / 2;
        for (int i = 0; i <= 5; i++) {
            float x = startX + i * cellSize;
            c.drawLine(x, startY, x, startY + gridSize, paint);
        }
        for (int i = 0; i <= 5; i++) {
            float y = startY + i * cellSize;
            c.drawLine(startX, y, startX + gridSize, y, paint);
        }
        for (GridButton button : buttons) {
            button.draw(c);
        }

        for (GuiToken token : tokens) {
            token.draw(c);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        Resources res = getResources();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (GridButton button : buttons) {
                    if (button.contains(x, y)) {
                        button.press();
                        invalidate();
                        break;
                    }
                }
                return true;

            case MotionEvent.ACTION_UP:
                boolean buttonPressed = false;
                for (GridButton button : buttons) {
                    if (button.contains(x, y)) {
                        button.release();
                        buttonPressed = true;
                        invalidate();

                        char column = button.getLabel();

                        int buttonIndex = buttons.indexOf(button);
                        char row = (char) ('A' + (buttonIndex >= 5 ? buttonIndex - 5 : 0)); // Adjust index for row calculation

                        float spawnY = button.getBounds().top;
                        GuiToken newToken = new GuiToken(res, isXTokenNext ? 'X' : 'O', button.getBounds().left, spawnY, button.getBounds().width(), button.getBounds().height(), R.drawable.huckleberryicon, R.drawable.appleicon);
                        newToken.setPosition(row, column); // Set position based on the button tapped
                        tokenHandler.registerTickListener(newToken);
                        moveExistingTokens(column, row);

                        tokens.add(newToken);

                        if (buttonIndex < 5) {
                            newToken.setVelocity(0, 10); // Move down
                        } else {
                            newToken.setVelocity(10, 0); // Move right
                        }

                        isXTokenNext = !isXTokenNext;
                        tokenHandler.sendEmptyMessage(0);
                        break;
                    }
                }

                if (!buttonPressed) {
                    Toast.makeText(getContext(), "Please click on a button", Toast.LENGTH_SHORT).show();
                }
                return true;

            default:
                return super.onTouchEvent(event);
        }
    }

    // Method to find a token at a given position
    private GuiToken findTokenAtPosition(GuiToken.GridPosition pos) {
        for (GuiToken token : tokens) {
            if (token.getPosition().row == pos.row && token.getPosition().column == pos.column) {
                return token;
            }
        }
        return null;
    }

    private void moveExistingTokens(char column, char row) {
        // Gather all tokens in the same column and sort them by their row positions in descending order
        ArrayList<GuiToken> columnTokens = new ArrayList<>();
        for (GuiToken token : tokens) {
            if (token.getPosition().column == column && token.getPosition().row >= row) {
                columnTokens.add(token);
            }
        }

        columnTokens.sort((t1, t2) -> Character.compare(t2.getPosition().row, t1.getPosition().row));

        for (GuiToken token : columnTokens) {
            char currentRow = token.getPosition().row;
            char newRow = (char) (currentRow + 1);

            if (newRow <= 'E' && findTokenAtPosition(new GuiToken.GridPosition(newRow, column)) == null) {
                token.setPosition(newRow, column);
                token.setVelocity(0, 10); // Move down
            }
        }
    }



    @Override
    public void onTick() {
        invalidate();
    }
}


