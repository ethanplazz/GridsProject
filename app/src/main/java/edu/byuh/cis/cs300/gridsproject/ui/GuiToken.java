package edu.byuh.cis.cs300.gridsproject.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;

import edu.byuh.cis.cs300.gridsproject.R;
import edu.byuh.cis.cs300.gridsproject.logic.Player;

/**
 * Represents a single X or O on the grid.
 * It is the graphical analog to the Player enum.
 */
public class GuiToken implements TickListener {
    private final RectF bounds;
    private final PointF velocity;
    private Bitmap image;
    private final GridPosition position;
    private static int movers = 0;
    private int stepCounter;
    private final int STEPS = 11;
    private boolean falling;

    public static class GridPosition {
        public char row;
        public char col;
    }

    /**
     * Create a new GuiToken object
     * @param p The Player (X or O) who created the token
     * @param parent which button was tapped to create the token
     * @param res the Resources object (used for loading image)
     */
    public GuiToken(Player p, GuiButton parent, Resources res) {
        position = new GridPosition();
        if (parent.isTopButton()) {
            position.row = 'A' - 1;
            position.col = parent.getLabel();
        } else {
            position.row = parent.getLabel();
            position.col = '1' - 1;
        }

        this.bounds = new RectF(parent.getBounds());
        velocity = new PointF();
        falling = false;
        if (p == Player.X) {
            image = BitmapFactory.decodeResource(res, R.drawable.appleicon);
        } else {
            image = BitmapFactory.decodeResource(res, R.drawable.huckleberryicon);
        }
        image = Bitmap.createScaledBitmap(image, (int)bounds.width(), (int)bounds.height(), true);
    }

    /**
     * Draw the token at the correct location, using the correct
     * image (X or O)
     * @param c The Canvas object supplied by onDraw
     */
    public void draw(Canvas c) {
        c.drawBitmap(image, bounds.left, bounds.top, null);
    }

    /**
     * Move the token by its current velocity.
     * Stop when it reaches its destination location.
     */
    public void move() {
        if (velocity.x != 0 || velocity.y != 0) {
            if (stepCounter >= STEPS) {
                velocity.x = 0;
                velocity.y = 0;
                movers--;
            } else {
                stepCounter++;
                bounds.offset(velocity.x, velocity.y);
            }
            if (position.row > 'E' || position.col > '5') {
                velocity.x = 0;
                velocity.y = 90; // Small positive y-velocity for falling effect
                falling = true;
            }
            if (falling) {
                velocity.y = velocity.y * 2; // Accelerate the falling speed
            }
        }
    }

    public boolean isInvisible(int screenHeight) {
        return bounds.top > screenHeight;
    }

    /**
     * Helper method for tokens created by the top row of buttons
     */
    public void startMovingDown() {
        startMoving(0, bounds.width()/STEPS);
        position.row++;
    }

    /**
     * Helper method for tokens created by the left column of buttons
     */
    public void startMovingRight() {
        startMoving(bounds.width()/STEPS, 0);
        position.col++;
    }

    private void startMoving(float vx, float vy) {
        velocity.set(vx, vy);
        movers++;
        stepCounter = 0;
    }

    /**
     * Is animation currently happening?
     * @return true if the token is currently moving (i.e. has a non-zero velocity); false otherwise.
     */
    public boolean isMoving() {
        return (velocity.x > 0 || velocity.y > 0);
    }

    public static boolean anyMovers() {
        return movers > 0;
    }

    @Override
    public void onTick() {
        move();
    }

    public boolean matches(char row, char col) {
        return (position.row == row && position.col == col);
    }
}