package edu.byuh.cis.cs300.gridsproject.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;

public class GridButton {
    private RectF bounds;
    private Bitmap image;
    private char label;

    // Constructor to accept label, position, size, and a Resources object for image loading
    public GridButton(Resources res, char label, float left, float top, float width, float height, int imageResId) {
        this.label = label;
        this.bounds = new RectF(left, top, left + width, top + height);
        this.image = BitmapFactory.decodeResource(res, imageResId);
        this.image = Bitmap.createScaledBitmap(image, (int) width, (int) height, true);
    }

    // Method to render the button at its correct position on the Canvas
    public void draw(Canvas canvas) {
        // Draw the button image at the specified bounds
        canvas.drawBitmap(image, null, bounds, null);
    }

    // Getter for the button's label
    public char getLabel() {
        return label;
    }

    // Check if a touch event falls within the button bounds
    public boolean contains(float x, float y) {
        return bounds.contains(x, y);
    }

    // Additional getter for bounds if needed
    public RectF getBounds() {
        return bounds;
    }
}