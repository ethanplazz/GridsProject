package edu.byuh.cis.cs300.gridsproject.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;

import edu.byuh.cis.cs300.gridsproject.logic.Player;

public class GuiToken {

    private Bitmap ex;
    private Bitmap oh;
    private final RectF bounds;
    private Player player;
    private final PointF velocity;
    private int stepsTaken;       // Track the number of steps taken
    private static final int MAX_STEPS = 15; // Maximum steps before stopping

    public GuiToken(Resources res, char label, float left, float top, float width, float height, int imageResIdX, int imageResIdO) {
        this.bounds = new RectF(left, top, left + width, top + height);
        this.ex = BitmapFactory.decodeResource(res, imageResIdX);
        this.ex = Bitmap.createScaledBitmap(ex, (int) width, (int) height, true);
        this.oh = BitmapFactory.decodeResource(res, imageResIdO);
        this.oh = Bitmap.createScaledBitmap(oh, (int) width, (int) height, true);
        this.velocity = new PointF(0, 0); // Initialize velocity to zero
        this.stepsTaken = 0; // Initialize steps taken
        // Set the player type based on the label ('X' or 'O')
        this.player = label == 'X' ? Player.X : Player.O;
    }

    // Set velocity
    public void setVelocity(float vx, float vy) {
        this.velocity.set(vx, vy);
    }

    // Method to draw the token on the canvas
    public void draw(Canvas canvas) {
        if (player == Player.X) {
            canvas.drawBitmap(ex, null, bounds, null);
        } else {
            canvas.drawBitmap(oh, null, bounds, null);
        }
    }

    public RectF getBounds() {
        return bounds;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void move() {
        // If the maximum number of steps is reached, stop moving
        if (stepsTaken >= MAX_STEPS) {
            velocity.set(0, 0); // Stop the token
            return;
        }

        // Offset the bounds by the current velocity
        bounds.offset(velocity.x, velocity.y);
        stepsTaken++; // Increment the steps taken

        // Determine if the token is close to its destination
        if (Math.abs(velocity.x) > 0 && Math.abs(bounds.left - bounds.right) < 10) {
            // Snap to grid on the x-axis and stop movement
            bounds.offsetTo(bounds.left, bounds.top);
            velocity.set(0, 0);
        }
        if (Math.abs(velocity.y) > 0 && Math.abs(bounds.top - bounds.bottom) < 10) {
            // Snap to grid on the y-axis and stop movement
            bounds.offsetTo(bounds.left, bounds.top);
            velocity.set(0, 0);
        }
    }
}
