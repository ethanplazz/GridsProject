package edu.byuh.cis.cs300.gridsproject.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.View;

import edu.byuh.cis.cs300.gridsproject.R;

public class GuiButton {

    private final RectF bounds;
    private Bitmap unpressedButton, pressedButton;
    private final char label;
    private boolean pressed;

    public GuiButton(char name, View parent, float x, float y, float width) {
        label = name;
        bounds = new RectF(x, y, x+width, y+width);
        unpressedButton = BitmapFactory.decodeResource(parent.getResources(), R.drawable.unpressed_button);
        unpressedButton = Bitmap.createScaledBitmap(unpressedButton, (int)width, (int)width, true);
        pressedButton = BitmapFactory.decodeResource(parent.getResources(), R.drawable.pressedbutton);
        pressedButton = Bitmap.createScaledBitmap(pressedButton, (int)width, (int)width, true);
        pressed = false;

    }

    public void draw(Canvas c) {
        if (pressed) {
            c.drawBitmap(pressedButton, bounds.left, bounds.top, null);
        } else {
            c.drawBitmap(unpressedButton, bounds.left, bounds.top, null);
        }
    }

    public boolean contains(float x, float y) {
        return bounds.contains(x,y);
    }

    public void press() {
        pressed = true;
    }

    public void release() {
        pressed = false;
    }

    public char getLabel() {
        return label;
    }

    public boolean isTopButton() {
        return (label >= '1' && label <= '5');
    }

    public boolean isLeftButton() {
        return (label >= 'A' && label <= 'E');
    }

    public RectF getBounds() {
        return bounds;
    }
}