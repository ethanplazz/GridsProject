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
    private boolean pressed;
    private Bitmap image2;

    // Constructor to accept label, position, size, and a Resources object for image loading
    public GridButton(Resources res, char label, float left, float top, float width, float height, int imageResId, int imageResId2) {
        pressed = false;
        this.label = label;
        this.bounds = new RectF(left, top, left + width, top + height);
        this.image = BitmapFactory.decodeResource(res, imageResId);
        this.image = Bitmap.createScaledBitmap(image, (int) width, (int) height, true);
        this.image2 = BitmapFactory.decodeResource(res, imageResId2);
        this.image2 = Bitmap.createScaledBitmap(image2, (int) width, (int) height, true);
    }

    // Method to render the button at its correct position on the Canvas
    public void draw(Canvas canvas) {
        if (pressed) {
            canvas.drawBitmap(image2, null, bounds, null);
        } else {
            canvas.drawBitmap(image, null, bounds, null);
        }
    }

    public boolean finger(int x, int y) {
        return bounds.contains(x, y);
    }

    // Method to set the pressed state to true
    public void press() {
        pressed = true;
    }

    // Method to set the pressed state to false
    public void release() {
        pressed = false;
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