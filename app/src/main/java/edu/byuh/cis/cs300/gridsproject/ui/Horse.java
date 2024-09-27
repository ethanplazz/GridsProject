package edu.byuh.cis.cs300.gridsproject.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

import edu.byuh.cis.cs300.gridsproject.R;

public class Horse extends View {

    private final Paint paint;
    private final List<GridButton> buttons;
    private final int gridSize = 5;

    /**
     * This is my horse constructor
     * @param c
     */
    public Horse(Context c) {
        super(c);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(8f);
        buttons = new ArrayList<>();
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
            buttons.add(new GridButton(res, label, left, top, cellSize, cellSize, R.drawable.unpressed_button));
        }
        for (int i = 0; i < 5; i++) {
            char label = (char) ('A' + i);
            float left = (width - gridSize) / 2f - cellSize;
            float top = (height - gridSize) / 2f + i * cellSize;
            buttons.add(new GridButton(res, label, left, top, cellSize, cellSize, R.drawable.unpressed_button));
        }
    }

    /**
     * This is my ondraw method where the grid is drawn and scaled
     * @param c
     */
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
    }
}
