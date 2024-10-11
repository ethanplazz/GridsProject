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

public class Horse extends View {

    private final Paint paint;
    private final List<GridButton> buttons;
    private final ArrayList<GuiToken> tokens;
    private boolean isXTokenNext = true;
    private final TokenHandler tokenHandler;

    /**
     * Constructor for Horse class
     * @param c
     */
    public Horse(Context c) {
        super(c);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(8f);
        buttons = new ArrayList<>();
        tokens = new ArrayList<>();
        tokenHandler = new TokenHandler(); // Initialize the TokenHandler
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

        // Draw tokens
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
                boolean buttonPressed = false;
                for (GridButton button : buttons) {
                    if (button.contains(x, y)) {
                        button.press();
                        buttonPressed = true;
                        invalidate();

                        // Add a new token behind the pressed button
                        float tokenLeft = button.getBounds().left;
                        float tokenTop = button.getBounds().top;
                        float tokenSize = button.getBounds().width();

                        // Alternate between X and O tokens
                        char tokenLabel = isXTokenNext ? 'X' : 'O';
                        int imageResIdX = R.drawable.xicon; // Replace with actual X image resource
                        int imageResIdO = R.drawable.oicon; // Replace with actual O image resource

                        // Create a new token
                        GuiToken newToken = new GuiToken(res, tokenLabel, tokenLeft, tokenTop, tokenSize, tokenSize, imageResIdX, imageResIdO);

                        // Set the velocity based on the button's position
                        if (button.getLabel() >= '1' && button.getLabel() <= '5') {
                            // Top row button: zero x-velocity, positive y-velocity
                            newToken.setVelocity(0, 10); // Adjust 10 to control speed
                        } else if (button.getLabel() >= 'A' && button.getLabel() <= 'E') {
                            // Left row button: positive x-velocity, zero y-velocity
                            newToken.setVelocity(10, 0); // Adjust 10 to control speed
                        }

                        tokens.add(newToken);

                        // Toggle for the next token
                        isXTokenNext = !isXTokenNext;

                        // Start the handler to update token movement
                        tokenHandler.sendEmptyMessage(0);

                        break;
                    }
                }
                if (!buttonPressed) {
                    Toast.makeText(getContext(), "Please click on a button", Toast.LENGTH_SHORT).show();
                }
                return true;

            case MotionEvent.ACTION_UP:
                for (GridButton button : buttons) {
                    button.release();
                }
                invalidate();
                return true;

            default:
                return super.onTouchEvent(event);
        }
    }

    //Inner Handler class
    private class TokenHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            //Update all tokens' positions based on their velocities
            for (GuiToken token : tokens) {
                token.move();
            }
            // Redraw the view
            invalidate();

            // Continue to send messages for movement
            sendEmptyMessageDelayed(0, 30); // Adjust 30ms for smoothness
        }
    }
}


