package edu.byuh.cis.cs300.gridsproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.graphics.Paint;

public class Horse extends View {

    private final Paint paint;

    public Horse(Context c) {
        super(c);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(8f);
    }

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        c.drawColor(Color.YELLOW);
        int width = getWidth();
        int height = getHeight();
        int gridSize = Math.min(width, height) * 4 / 5;
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
    }
}
